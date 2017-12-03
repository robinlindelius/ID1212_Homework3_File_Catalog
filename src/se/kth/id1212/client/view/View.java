package se.kth.id1212.client.view;

import se.kth.id1212.common.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Robin on 2017-12-01.
 */
public class View implements Runnable {
    private final Scanner reader = new Scanner(System.in);
    private final StdOut out = new StdOut();

    private final String PROMPT = ">> ";

    private Catalog catalog;
    private long userID;

    private boolean running = false;

    /**
     * Starts the view
     */
    public void start(Catalog catalog) {
        this.catalog = catalog;
        if (running) return;
        running = true;
        new Thread(this).start();
    }

    /**
     * Interpret user input and executes it
     */
    @Override
    public void run() {
        println("**************************************************************");
        println("*                 WELCOME TO THE FILE CATALOG                *");
        println("*                 WRITE HELP FOR INSTRUCTIONS                *");
        println("**************************************************************");
        println();
        while (running) {
            try {
                ClientInput input = new ClientInput(readLine());
                switch (input.getCommand()) {
                    case REGISTER:
                        catalog.register(
                                new UserDTO(input.getUsername(),
                                        input.getPassword()));
                        break;
                    case UNREGISTER:
                        catalog.unregister(userID);
                        userID = 0;
                        break;
                    case LOGIN:
                        userID = catalog.login(new UserDTO(input.getUsername(),
                                        input.getPassword()), new NotificationListener());
                        break;
                    case LOGOUT:
                        catalog.logout(userID);
                        userID = 0;
                        break;
                    case UPLOAD:
                        Path path = Paths.get(input.getFilePath());
                        catalog.upload(userID, new ClientFile(path.getFileName().toString(),
                                Files.size(path),
                                input.isPrivateAccess(),
                                input.isWritePermission(),
                                input.isNotify()));
                        break;
                    case DOWNLOAD:
                        FileDTO fileDTO = catalog.download(userID, input.getFileName());
                        println("Downloaded file: " + fileDTO.getInfo());
                        break;
                    case DELETE:
                        catalog.delete(userID, input.getFileName());
                        break;
                    case UPDATE:
                        catalog.update(userID, input.getFileName(),input.isPrivateAccess(),
                                input.isWritePermission(),input.isNotify());
                        break;
                    case LIST:
                        List<? extends FileDTO> files = catalog.list(userID);
                        if (files.isEmpty()) println("Catalog is empty.");
                        else {
                            for (FileDTO file : files) {
                                println(file.getInfo());
                            }
                        }
                        break;
                    case HELP:
                        println("**************************************************************");
                        println("* USER GUIDE:                                                *");
                        println("**************************************************************");
                        println("* REGISTER [USERNAME] [PASSWORD]                             *");
                        println("* UNREGISTER                                                 *");
                        println("* LOGIN [USERNAME] [PASSWORD]                                *");
                        println("* LOGOUT                                                     *");
                        println("* UPLOAD [FILE PATH] [PRIVATE/PUBLIC] [READ/WRITE] [NOTIFY]  *");
                        println("* DOWNLOAD [FILE]                                            *");
                        println("* DELETE [FILE]                                              *");
                        println("* UPDATE [FILE] [PRIVATE/PUBLIC] [READ/WRITE] [NOTIFY]       *");
                        println("* LIST                                                       *");
                        println("* HELP                                                       *");
                        println("**************************************************************");
                        println();
                        break;
                    case QUIT:
                        if (userID != 0) catalog.logout(userID);
                        running = false;
                        println("**************************************************************");
                        println("* THANKS FOR USING THE FILE CATALOG                          *");
                        println("* Disconnecting catalog...                                   *");
                        println("* Quitting application...                                    *");
                        println("**************************************************************");
                        break;
                    default:
                        println("Input: " + '"' + input.getInput() + '"' +
                                " is not a valid command, write HELP for help.");
                        break;
                }
            }
            catch (Exception e) {
                println("Operation failed");
            }
        }
    }

    private String readLine() {
        print();
        return reader.nextLine();
    }

    private void print() {
        out.print(PROMPT);
    }

    private void println() {
        println("");
    }

    private void println(String string) {
        out.println(PROMPT + string);
    }

    private class NotificationListener extends UnicastRemoteObject implements Listener {

        NotificationListener() throws RemoteException {

        }

        @Override
        public void notify(String notification) throws RemoteException{
            out.println("** Notification: " + notification + " **");
            print();
        }
    }
}

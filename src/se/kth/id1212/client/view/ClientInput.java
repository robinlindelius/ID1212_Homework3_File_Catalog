package se.kth.id1212.client.view;

/**
 * Created by Robin on 2017-12-01.
 */
public class ClientInput {
    private final String input;
    private Command command;
    private String username;
    private String password;
    private String filePath;
    private String fileName;
    private boolean privateAccess;
    private boolean writePermission;
    private boolean notify;

    /**
     * Constructs a ClientInput for the given input
     * @param input The input to construct a ClientInput from
     */
    public ClientInput(String input) {
        this.input = input;
        parseInput(input);
    }

    private void parseInput(String input) {
        try {
            String[] inputElements = input.split("\\s+");
            command = Command.valueOf(inputElements[0].toUpperCase());

            switch (command) {
                case REGISTER:
                    username = inputElements[1];
                    password = inputElements[2];
                    break;
                case LOGIN:
                    username = inputElements[1];
                    password = inputElements[2];
                    break;
                case UPLOAD:
                    filePath = inputElements[1];
                    privateAccess = inputElements[2].equalsIgnoreCase("PRIVATE");
                    writePermission = inputElements[3].equalsIgnoreCase("WRITE");
                    if (inputElements.length > 4) notify = inputElements[4].equalsIgnoreCase("NOTIFY");
                    break;
                case DOWNLOAD:
                    fileName = inputElements[1];
                    break;
                case DELETE:
                    fileName = inputElements[1];
                    break;
                case UPDATE:
                    fileName = inputElements[1];
                    privateAccess = inputElements[2].equalsIgnoreCase("PRIVATE");
                    writePermission = inputElements[3].equalsIgnoreCase("WRITE");
                    if (inputElements.length > 4) notify = inputElements[4].equalsIgnoreCase("NOTIFY");
                    break;
            }
        }
        catch (Exception e) {
            command = Command.NO_COMMAND;
        }
    }

    /**
     * Returns the request
     * @return request
     */
    public String getInput() {
        return input;
    }

    /**
     * Returns the command
     * @return command
     */
    public Command getCommand() {
        return command;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isPrivateAccess() {
        return privateAccess;
    }

    public boolean isWritePermission() {
        return writePermission;
    }

    public boolean isNotify() {
        return notify;
    }
}

package se.kth.id1212.server.controller;

import se.kth.id1212.common.*;
import se.kth.id1212.server.integration.CatalogDAO;
import se.kth.id1212.server.model.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Robin on 2017-12-02.
 */
public class Controller extends UnicastRemoteObject implements Catalog {
    private final CatalogDAO catalogDAO;

    private final Map<Long, UserHandler> loggedInUsers = new ConcurrentHashMap<>();

    public Controller() throws RemoteException {
        super();
        catalogDAO = new CatalogDAO();
    }

    private User authenticateUser(long id) throws UserException {
        if (loggedInUsers.containsKey(id)) return loggedInUsers.get(id).getUser();
        else throw new UserException("User not signed in");
    }

    @Override
    public void register(UserDTO userDTO) throws RemoteException, UserException {
        try {
            if (catalogDAO.findUserByUsername(userDTO.getUsername(), true) != null) {
                throw new UserException("User with username: " + userDTO.getUsername() + " already exists");
            }
            catalogDAO.registerUser(new User(userDTO));
        } catch (Exception e) {
            throw new UserException("Could not create user for: " + userDTO.getUsername(), e);
        }
    }

    @Override
    public void unregister(long userID) throws RemoteException, UserException {
        try {
            if (authenticateUser(userID) != null) {
                String username = loggedInUsers.get(userID).getUsername();
                if (catalogDAO.findUserByUsername(username, false) != null) {
                    catalogDAO.unregisterUser(username);
                    loggedInUsers.remove(userID);
                }
            }
        } catch (Exception e) {
            throw new UserException("Could not unregister user", e);
        }
    }


    @Override
    public long login(UserDTO userDTO, Listener notificationListener) throws RemoteException, UserException {
        try {
            User user = catalogDAO.findUserByUsername(userDTO.getUsername(), true);
            if (user == null) {
                throw new UserException("User with username: " + userDTO.getUsername() + " does not exist");
            }
            else if (!user.getPassword().equals(userDTO.getPassword())) {
                throw new UserException("Wrong password for user: " + userDTO.getUsername());
            }
            loggedInUsers.put(user.getUserID(), new UserHandler(user, notificationListener));
            return user.getUserID();
        } catch (Exception e) {
            throw new UserException("Could not sign in user: " + userDTO.getUsername(), e);
        }
    }

    @Override
    public void logout(long userID) throws RemoteException, UserException {
        try {
            if (authenticateUser(userID) != null) loggedInUsers.remove(userID);
        } catch (Exception e) {
            throw new UserException("Could not sign out user", e);
        }
    }

    @Override
    public void upload(long userID, FileDTO fileDTO) throws RemoteException, FileException{
        try {
            User user = authenticateUser(userID);
            if (catalogDAO.findFileByName(fileDTO.getName(), true) != null) {
                throw new FileException("File with name: " + fileDTO.getName() + " already exists");
            }
            catalogDAO.uploadFile(new CatalogFile(fileDTO, user));
        } catch (Exception e) {
            throw new FileException("Could not upload file: " + fileDTO.getName(), e);
        }
    }

    @Override
    public FileDTO download(long userID, String file) throws RemoteException, FileException, UserException {
        try {
            if(authenticateUser(userID) != null) {
                CatalogFile catalogFile = catalogDAO.findFileByName(file, false);
                long ownerID = catalogFile.getOwner().getUserID();
                if (!catalogFile.isPrivateAccess() || userID == ownerID) {
                    if (loggedInUsers.containsKey(ownerID) && catalogFile.isNotify()) {
                        Listener notificationListener = loggedInUsers.get(ownerID).getNotificationListener();
                        String user = loggedInUsers.get(userID).getUsername();
                        notificationListener.notify("File: " + catalogFile.getName() + " was downloaded by user: " + user);
                    }
                    return catalogFile;
                }
            }
            return null;
        } catch (Exception e) {
            throw new FileException("Could not download file: " + file, e);
        }
    }

    @Override
    public void delete(long userID, String file) throws RemoteException, FileException, UserException {
        try {
            if(authenticateUser(userID) != null) {
                CatalogFile catalogFile = catalogDAO.findFileByName(file, false);
                long ownerID = catalogFile.getOwner().getUserID();
                if (!catalogFile.isPrivateAccess() || userID == ownerID) {
                    if (loggedInUsers.containsKey(ownerID) && catalogFile.isNotify()) {
                        Listener notificationListener = loggedInUsers.get(ownerID).getNotificationListener();
                        String user = loggedInUsers.get(userID).getUsername();
                        notificationListener.notify("File: " + catalogFile.getName() + " was deleted by user: " + user);
                    }
                    catalogDAO.deleteFile(file);
                }
            }
        } catch (Exception e) {
            throw new FileException("Could not delete file: " + file, e);
        }
    }

    @Override
    public void update(long userID, String file, boolean privateAccess, boolean writePermission, boolean notify)
            throws RemoteException, FileException, UserException {
        try {
            if(authenticateUser(userID) != null) {
                CatalogFile catalogFile = catalogDAO.findFileByName(file, false);
                long ownerID = catalogFile.getOwner().getUserID();
                if (!catalogFile.isPrivateAccess() && catalogFile.isWritePermission() || userID == ownerID) {
                    if (loggedInUsers.containsKey(ownerID) && userID != ownerID && catalogFile.isNotify()) {
                        Listener notificationListener = loggedInUsers.get(ownerID).getNotificationListener();
                        String user = loggedInUsers.get(userID).getUsername();
                        notificationListener.notify("File: " + catalogFile.getName() + " was updated by user: " + user);
                    }
                    catalogFile.setPrivateAccess(privateAccess);
                    catalogFile.setWritePermission(writePermission);
                    catalogFile.setNotify(notify);
                    catalogDAO.updateFile();
                }
            }
        } catch (Exception e) {
            throw new FileException("Could not update file: " + file, e);
        }
    }

    @Override
    public List<? extends FileDTO> list(long userID) throws RemoteException, FileException, UserException {
        try {
            if(authenticateUser(userID) != null) {
                List<CatalogFile> files = new ArrayList<>();
                for (CatalogFile catalogFile :catalogDAO.findAllFiles()) {
                    if (!catalogFile.isPrivateAccess() || userID == catalogFile.getOwner().getUserID()) {
                        files.add(catalogFile);
                    }
                }
                return files;
            }
            return null;
        } catch (Exception e) {
            throw new FileException("Unable to list files.", e);
        }
    }
}

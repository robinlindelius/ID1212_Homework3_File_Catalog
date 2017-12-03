package se.kth.id1212.common;

import se.kth.id1212.server.model.FileException;
import se.kth.id1212.server.model.UserException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Robin on 2017-12-01.
 */
public interface Catalog extends Remote {
    /**
     * The default URI of the catalog server in the RMI registry.
     */
    String CATALOG_NAME_IN_REGISTRY = "CATALOG";

    void register(UserDTO userDTO) throws RemoteException, UserException;

    void unregister(long userID) throws RemoteException, UserException;

    long login(UserDTO userDTO, Listener listener) throws RemoteException, UserException;

    void logout(long userID) throws RemoteException, UserException;

    void upload(long userID, FileDTO fileDTO) throws RemoteException, FileException, UserException;

    FileDTO download(long userID, String file) throws RemoteException, FileException, UserException;

    void delete(long userID, String file) throws RemoteException, FileException, UserException;

    void update(long userID, String file, boolean privateAccess, boolean writePermission, boolean notify)
            throws RemoteException, FileException, UserException;

    List<? extends FileDTO> list(long userID) throws RemoteException, FileException, UserException;

}

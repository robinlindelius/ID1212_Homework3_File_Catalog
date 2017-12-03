package se.kth.id1212.common;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Robin on 2017-12-03.
 */
public interface Listener extends Remote, Serializable {

    void notify(String notification) throws RemoteException;
}

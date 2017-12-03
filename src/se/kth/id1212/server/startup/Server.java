package se.kth.id1212.server.startup;

import se.kth.id1212.common.Catalog;
import se.kth.id1212.server.controller.Controller;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Robin on 2017-12-02.
 */
public class Server {
    private String catalogName = Catalog.CATALOG_NAME_IN_REGISTRY;

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startRMIServant();
            System.out.println("Catalog server started.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Failed to start catalog server.");
        }
    }

    private void startRMIServant() throws RemoteException, MalformedURLException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        Controller controller = new Controller();
        Naming.rebind(catalogName, controller);
    }
}

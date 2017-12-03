package se.kth.id1212.client.startup;

import se.kth.id1212.client.view.View;
import se.kth.id1212.common.Catalog;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Robin on 2017-12-01.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Catalog catalog = (Catalog) Naming.lookup(Catalog.CATALOG_NAME_IN_REGISTRY);
            new View().start(catalog);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.out.println("Client could not be started.");
        }
    }
}

package apps.client;

import bilboards.IClient;
import bilboards.IManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Client implements IClient {

    private List<Integer> orderIDs = new ArrayList<>();

    public static void main(String[] args) throws RemoteException, NotBoundException {


        Registry registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
        IManager manager = (IManager) registry.lookup(args[2]);
        Client client = new Client();
        IClient iClient = (IClient) UnicastRemoteObject.exportObject(client,0);
        ClientWindow.createWindow(iClient,manager,client.orderIDs);

    }

    @Override
    public void setOrderId(int orderId) throws RemoteException {
        orderIDs.add(orderId);
    }
}

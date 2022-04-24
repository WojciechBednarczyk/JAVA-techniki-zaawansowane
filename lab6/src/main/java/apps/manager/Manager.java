package apps.manager;

import bilboards.IBillboard;
import bilboards.IManager;
import bilboards.Order;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Manager implements IManager {

    private int bilboardID = 0;
    private int orderID = 0;
    private Map<Integer,IBillboard> billboards = new HashMap<>();
    private Map<Integer,Order> orders = new HashMap<>();

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {

        Manager manager = new Manager();
        Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
        registry.bind(args[1], UnicastRemoteObject.exportObject(manager, 0));
        ManagerWindow.createWindow(manager.billboards,manager.orders);
    }

    @Override
    public int bindBillboard(IBillboard billboard) throws RemoteException {
        billboards.put(bilboardID,billboard);
        int idToReturn = bilboardID;
        bilboardID++;
        return idToReturn;
    }

    @Override
    public boolean unbindBillboard(int billboardId) throws RemoteException {
        billboards.remove(billboardId);
        return true;
    }

    @Override
    public boolean placeOrder(Order order) throws RemoteException {

        if (billboards.size()==0)
        {
            System.out.println("Brak dostepnych bilbordow");
            return false;
        }
        else {
            order.client.setOrderId(orderID);
            orders.put(orderID,order);
            orderID++;
            return true;
        }
    }

    @Override
    public boolean withdrawOrder(int orderId) throws RemoteException {

        orders.remove(orderID);
        return true;
    }
}

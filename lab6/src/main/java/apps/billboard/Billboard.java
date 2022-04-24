package apps.billboard;

import bilboards.IBillboard;
import bilboards.IManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Billboard implements IBillboard {

    private Map<Integer,DisplayedAdvertisement> adverts = new HashMap<>();
    private int capacity;
    private Duration displayInterval;

    public static void main(String[] args) throws NotBoundException, RemoteException {

        Registry registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
        IManager manager = (IManager) registry.lookup(args[2]);
        Billboard billboard = new Billboard();
        billboard.displayInterval=Duration.ofSeconds(Long.parseLong(args[4]));
        IBillboard iBillboard = (IBillboard) UnicastRemoteObject.exportObject(billboard,0);
        billboard.capacity= Integer.parseInt(args[3]);
        BillboardWindow.createWindow(manager,iBillboard,billboard.adverts,billboard.displayInterval);
    }

    @Override
    public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException {

        if (adverts.size()==capacity)
        {
            return false;
        }
        else {
            DisplayedAdvertisement displayedAdvertisement = new DisplayedAdvertisement(advertText, displayPeriod);
            adverts.put(orderId, displayedAdvertisement);
            return true;
        }
    }


    @Override
    public boolean removeAdvertisement(int orderId) throws RemoteException {
        adverts.remove(orderId);
        return true;
    }

    @Override
    public int[] getCapacity() throws RemoteException {
        return new int[]{capacity, capacity - adverts.size()};
    }

    @Override
    public void setDisplayInterval(Duration displayInterval) throws RemoteException {
        this.displayInterval=displayInterval;
    }

    @Override
    public boolean start() throws RemoteException {
        return false;
    }

    @Override
    public boolean stop() throws RemoteException {
        return false;
    }
}

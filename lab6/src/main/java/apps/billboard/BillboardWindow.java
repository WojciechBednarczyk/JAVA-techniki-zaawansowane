package apps.billboard;

import bilboards.IBillboard;
import bilboards.IManager;

import javax.swing.*;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;

public class BillboardWindow {

    private JPanel mainPanel;
    private JLabel advertismentTextField;

    private IManager manager;
    private IBillboard billboard;
    private Map<Integer, DisplayedAdvertisement> adverts;
    private Duration displayInterval;

    public static void createWindow(IManager manager, IBillboard iBillboard, Map<Integer, DisplayedAdvertisement> adverts, Duration displayInterval) throws RemoteException {
        JFrame frame = new JFrame("Bilboard");
        frame.setContentPane(new BillboardWindow(manager,iBillboard,adverts,displayInterval).mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(900,300);
    }

    public BillboardWindow(IManager manager, IBillboard billboard, Map<Integer, DisplayedAdvertisement> adverts, Duration displayInterval) throws RemoteException {
        this.manager = manager;
        this.billboard = billboard;
        this.adverts = adverts;
        this.displayInterval=displayInterval;
        manager.bindBillboard(billboard);

        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()){

                for (Iterator<Map.Entry<Integer,DisplayedAdvertisement>> it = adverts.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<Integer,DisplayedAdvertisement> advert = it.next();
                    advertismentTextField.setText(advert.getValue().advertText);
                    if (displayInterval.toMillis() >= advert.getValue().displayPeriod.toMillis())
                    {
                        it.remove();
                    }
                    else{
                        advert.getValue().displayPeriod=advert.getValue().displayPeriod.minus(displayInterval);
                        System.out.println(advert.getKey() + " " + advert.getValue().displayPeriod.toString());
                    }
                    try {
                        Thread.sleep(displayInterval.toMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        thread.start();
        }
    }

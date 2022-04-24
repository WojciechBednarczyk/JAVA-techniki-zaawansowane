package apps.manager;

import bilboards.IBillboard;
import bilboards.Order;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Map;

public class ManagerWindow {


    private JPanel mainPanel;
    private JList bilboardsList;
    private JButton refreshButton;
    private JList ordersList;
    private JButton assignAdvertisement;
    private Map<Integer, IBillboard> billboards;
    private Map<Integer, Order> orders;

    public ManagerWindow(Map<Integer, IBillboard> billboards, Map<Integer, Order> orders) {

        this.billboards=billboards;
        this.orders=orders;
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel ordersModel = new DefaultListModel();
                for (Map.Entry<Integer,Order> entry : orders.entrySet())
                {
                    ordersModel.addElement(entry.getKey() + ":" + entry.getValue().advertText);
                }

                ordersList.setModel(ordersModel);
                ordersList.clearSelection();

                DefaultListModel bilboardsModel = new DefaultListModel();
                for (Map.Entry<Integer, IBillboard> entry : billboards.entrySet())
                {
                    try {
                        bilboardsModel.addElement(entry.getKey() + ":" + Arrays.toString(entry.getValue().getCapacity()));
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }

                bilboardsList.setModel(bilboardsModel);
                bilboardsList.clearSelection();
            }
        });
        assignAdvertisement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bilboard = bilboardsList.getSelectedValue().toString();
                IBillboard iBillboard = billboards.get(Integer.parseInt(bilboard.substring(0,bilboard.indexOf(':'))));
                String orderString = ordersList.getSelectedValue().toString();
                int orderID = Integer.parseInt(orderString.substring(0,orderString.indexOf(':')));
                Order order = orders.get(orderID);

                try {
                    iBillboard.addAdvertisement(order.advertText,order.displayPeriod,orderID);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }

                DefaultListModel bilboardsModel = new DefaultListModel();
                for (Map.Entry<Integer, IBillboard> entry : billboards.entrySet())
                {
                    try {
                        bilboardsModel.addElement(entry.getKey() + ":" + Arrays.toString(entry.getValue().getCapacity()));
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }

                bilboardsList.setModel(bilboardsModel);
                bilboardsList.clearSelection();
            }
        });
    }

    public static void createWindow(Map<Integer, IBillboard> billboards, Map<Integer, Order> orders) {
        JFrame frame = new JFrame("Manager");
        frame.setContentPane(new ManagerWindow(billboards,orders).mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
    }
}

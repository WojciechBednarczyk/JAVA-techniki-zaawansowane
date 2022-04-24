package apps.client;

import bilboards.IClient;
import bilboards.IManager;
import bilboards.Order;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.List;

public class ClientWindow {
    private JButton sendOrderButton;
    private JTextField advertTextField;
    private JTextField durationTextField;
    private JPanel mainPanel;
    private JButton withdrawOrderButton;
    private JTextField withdrawOrderTextField;
    private JList ordersList;
    private IClient client;
    private IManager manager;
    private List<Integer> orderIDs;


    public static void createWindow(IClient iClient, IManager manager, List<Integer> orderIDs) {
        JFrame frame = new JFrame("Client");
        frame.setContentPane(new ClientWindow(iClient,manager,orderIDs).mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
    }

    public ClientWindow(IClient iClient, IManager iManager, List<Integer> orderIDs){

        client=iClient;
        manager=iManager;
        this.orderIDs=orderIDs;

        DefaultListModel orders = new DefaultListModel();
        for (Integer orderId : orderIDs)
        {
            orders.addElement(orderId);
        }

        ordersList.setModel(orders);
        ordersList.clearSelection();

        sendOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Order order = new Order();
                order.advertText=advertTextField.getText();
                order.displayPeriod = Duration.ofSeconds(Long.parseLong(durationTextField.getText()));
                order.client=iClient;

                try {
                    if (manager.placeOrder(order)==false)
                    {
                        JOptionPane.showMessageDialog(null,"Nie udalo sie dodac zamowienia");
                    }
                    else {
                        DefaultListModel orders = new DefaultListModel();
                        for (Integer orderId : orderIDs)
                        {
                            orders.addElement(orderId);
                        }

                        ordersList.setModel(orders);
                        ordersList.clearSelection();

                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
        withdrawOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    manager.withdrawOrder(Integer.parseInt(withdrawOrderTextField.getText()));
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}

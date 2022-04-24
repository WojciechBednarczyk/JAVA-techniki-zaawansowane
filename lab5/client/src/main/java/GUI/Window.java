package GUI;

import CSVReader.Reader;
import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ServiceLoader;

public class Window {
    private JTable dataTable;
    private JPanel mainPanel;
    private JLabel resultLabel;
    private JList list1;
    private JLabel algorithmDescriptionLabel;
    private JButton calculateButton;

    public static void createWindow() {
        JFrame frame = new JFrame("Application");
        frame.setContentPane(new Window().mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
    }

    private void createTable(String [][] data,String[] headers)
    {
        dataTable.setModel(new DefaultTableModel(
                data,
                headers
        ));
        dataTable.setDefaultEditor(Object.class,null);
    }
    public Window(){
        DataSet dataSet = Reader.readData();
        createTable(dataSet.getData(),dataSet.getHeader());
        DefaultListModel loadedAlgorithms = new DefaultListModel();
        ServiceLoader<ClusterAnalysisService> loader = ServiceLoader.load(ClusterAnalysisService.class);
        for (ClusterAnalysisService service : loader) {
            loadedAlgorithms.addElement(service.getClass().getName());
        }

        list1.setModel(loadedAlgorithms);
        list1.clearSelection();

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                for (ClusterAnalysisService service : loader) {
                    if (service.getClass().getName().equals(list1.getSelectedValue().toString()))
                    {
                        algorithmDescriptionLabel.setText(service.getName());
                    }
                }

            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ClusterAnalysisService service : loader) {
                    if (service.getClass().getName().equals(list1.getSelectedValue().toString()))
                    {
                        try {
                            service.submit(dataSet);
                        } catch (ClusteringException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            resultLabel.setText(service.retrieve(true));
                        } catch (ClusteringException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }


}

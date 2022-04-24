package GUI;

import PersonalData.PersonalData;
import PersonalData.PersonalDataLoader;
import Reader.DirectoriesReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Window {
    private JPanel mainPanel;
    private JList<String> listOfDirectories;
    private JSplitPane splitPane;
    private JPanel personalDataPanel;
    private JPanel directoriesPanel;
    private JLabel zawartoscLabel;
    private JLabel peselLabel;
    private JLabel dataUrodzeniaLabel;
    private JLabel nazwiskoLabel;
    private JLabel imieLabel;
    private JLabel idLabel;
    private JLabel wiekLabel;
    private JPanel imagePanel;
    private JLabel imageLabel;


    public static void createWindow() throws IOException {
        JFrame frame = new JFrame("Application");
        frame.setContentPane(new Window().mainPanel);
        frame.setVisible(true);
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public Window() throws IOException {

        PersonalDataLoader personalDataLoader = new PersonalDataLoader();
        splitPane.setDividerLocation(0.5);
        splitPane.getLeftComponent().setMinimumSize(new Dimension(400,300));
        splitPane.getRightComponent().setMinimumSize(new Dimension(400,300));
        DefaultListModel<String> model = new DefaultListModel<>();
        listOfDirectories.setModel(model);

        DirectoriesReader directoriesReader = new DirectoriesReader();
        model.addAll(List.of(directoriesReader.readDirectories()));


        listOfDirectories.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                PersonalData personalData = null;
                if (!e.getValueIsAdjusting()){
                    try {
                        personalData = personalDataLoader.loadPersonalData(listOfDirectories.getSelectedValue());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    if (personalData!=null)
                    {
                        idLabel.setText(personalData.getId());
                        imieLabel.setText(personalData.getName());
                        nazwiskoLabel.setText(personalData.getSurname());
                        wiekLabel.setText(String.valueOf(personalData.getAge()));
                        dataUrodzeniaLabel.setText(personalData.getDateOfBirth().toString());
                        peselLabel.setText(personalData.getPesel());
                        imageLabel.setIcon(new ImageIcon(personalData.getImage()));
                        zawartoscLabel.setText(personalData.getStatus());
                    }
                }
            }
        });
    }
}

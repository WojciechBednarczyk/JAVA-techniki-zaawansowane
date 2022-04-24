package app;

import library.MD5StatusChecker;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class Window {
    private JButton chooseFileToCheckButton;
    private JPanel mainPanel;
    private JTextField filePathField;
    private JLabel statusLabel;

    public static void createWindow(){
        JFrame frame = new JFrame("Application");
        frame.setContentPane(new Window().mainPanel);
        frame.setVisible(true);
        frame.setSize(400,300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public Window() {
        MD5StatusChecker md5StatusChecker = new MD5StatusChecker();
        filePathField.setEditable(false);
        chooseFileToCheckButton.addActionListener(new ActionListener() {

            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                Path filePath = null;
                if (jFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
                {
                    filePath = Path.of(jFileChooser.getSelectedFile().getPath());
                    md5StatusChecker.load();
                    filePathField.setText(filePath.toString());
                    statusLabel.setText(md5StatusChecker.checkStatus(filePath).toString());
                    md5StatusChecker.save();

                }

            }
        });
    }
}

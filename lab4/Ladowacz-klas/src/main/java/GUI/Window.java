package GUI;

import Application.MyStatusListener;
import Loader.MyClassLoader;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import processing.StatusListener;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Window {
    private JPanel mainPanel;
    private JButton chooseDirectoryButton;
    private JTextField defineTaskTextField;
    private JLabel getInfoLabel;
    private JList list1;
    private JButton doTaskButton;
    private JButton loadClassButton;
    private JList list2;
    private JLabel resultLabel;
    private JLabel progressLabel;
    private ArrayList<Class<?>> classes = new ArrayList<>();
    private ArrayList<Constructor<?>> constructors = new ArrayList<>();
    private ArrayList<MyClassLoader> classLoaders = new ArrayList<>();
    private File[] files;
    private String currentDirectory;

    public static void createWindow() {
        JFrame frame = new JFrame("Application");
        frame.setContentPane(new Window().mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
    }

    private void initialize() {

    }

    public Window() {

        final MyClassLoader[] myClassLoader = {new MyClassLoader(Path.of("C:\\Users\\48795\\Desktop\\JAVA\\lab4\\Klasy-ladowane\\target\\classes"))};

        chooseDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Test");
                chooser.setMultiSelectionEnabled(true);
                chooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public String getDescription() {
                        return "Java Class files (*.class)";
                    }

                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        } else {
                            return f.getName().toLowerCase().endsWith(".class");
                        }
                    }
                });

                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
                    currentDirectory= String.valueOf(chooser.getCurrentDirectory());
                    while (currentDirectory.charAt(currentDirectory.length()-1)!='\\')
                    {
                        currentDirectory= StringUtils.chop(currentDirectory);
                    }
                    currentDirectory=StringUtils.chop(currentDirectory);
                    files = chooser.getSelectedFiles();
                    DefaultListModel dlm = new DefaultListModel();
                    for (File file : files) {
                        dlm.addElement(Files.getNameWithoutExtension(file.getName()));
                    }
                    list1.setModel(dlm);
                } else {
                    System.out.println("No Selection ");
                }
            }
        });

        loadClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myClassLoader[0] = new MyClassLoader(Path.of(currentDirectory));
                classLoaders.add(myClassLoader[0]);
                String choosenClass = (String) list1.getSelectedValue();
                System.out.println(choosenClass);
                Path pathToClass = null;
                for (File file : files) {
                    if (choosenClass.equals(Files.getNameWithoutExtension(String.valueOf(file))))
                    {
                        pathToClass=file.toPath();
                    }
                }
                System.out.println(pathToClass.toString());
                try {
                    classes.add(myClassLoader[0].findClass("processors." + choosenClass ));
//                    constructors.add(myClassLoader.findClass("processors.AdderProcessor").getConstructor());
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                catch (LinkageError error)
                {
                    JOptionPane.showMessageDialog(null,"Ta klasa zostala juz zaladowana!");
                }

                DefaultListModel loadedClassesModel = new DefaultListModel();
                for (Class<?> loadedClass : classes)
                {
                    Object o = null;
                    try {
                        o = loadedClass.getConstructor().newInstance();
                    } catch (InstantiationException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                    Method getInfoMethod = null;
                    try {
                        getInfoMethod = loadedClass.getDeclaredMethod("getInfo");
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }

                    try {
                        loadedClassesModel.addElement(loadedClass.getName() + ": " + (String) getInfoMethod.invoke(o));
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
                list2.setModel(loadedClassesModel);
            }
        });

        doTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object o = null;
                Class<?> toRemove = null;
                for(Class<?> choosenClass : classes)
                {
                    String className = list2.getSelectedValue().toString().substring(0,list2.getSelectedValue().toString().indexOf(':'));
                    System.out.println(className);
                    if (choosenClass.getName().equals(className)) {
                        Method submitTaskMethod = null;
                        try {
                            submitTaskMethod = choosenClass.getDeclaredMethod("submitTask", String.class, StatusListener.class);
                        } catch (NoSuchMethodException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            o = choosenClass.getConstructor().newInstance();
                            boolean b = (boolean) submitTaskMethod.invoke(o, defineTaskTextField.getText(), new MyStatusListener());
                            if (b)
                                System.out.println("Processing started correctly");
                            else
                                System.out.println("Processing ended with an error");

                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace();
                        } catch (InstantiationException ex) {
                            ex.printStackTrace();
                        } catch (NoSuchMethodException ex) {
                            ex.printStackTrace();
                        }

                        Method getResultMethod = null;
                        try {
                           getResultMethod = choosenClass.getDeclaredMethod("getResult");
                        } catch (NoSuchMethodException ex) {
                            ex.printStackTrace();
                        }

                        ExecutorService executor = Executors.newSingleThreadExecutor();

                        Method finalGetResultMethod = getResultMethod;
                        Object finalO = o;
                        executor.submit(() -> {
                            String result = null;
                            while (true) {

                                try {
                                    Thread.sleep(800);
                                    result = (String) finalGetResultMethod.invoke(finalO);
                                } catch (InterruptedException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                    ex.printStackTrace();
                                }
                                if (result != null) {
                                    resultLabel.setText(result);
                                    executor.shutdown();
                                    break;
                                }
                            }
                        });
                        toRemove=choosenClass;
                        break;
                    }

//                    //wyladowanie klasy




                }
                classes.remove(toRemove);
                toRemove=null;
                classLoaders.remove(list2.getSelectedIndex());
                System.gc();

                DefaultListModel loadedClassesModel = new DefaultListModel();
                for (Class<?> loadedClass : classes)
                {
                    Object object = null;
                    try {
                        o = loadedClass.getConstructor().newInstance();
                    } catch (InstantiationException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                    Method getInfoMethod = null;
                    try {
                        getInfoMethod = loadedClass.getDeclaredMethod("getInfo");
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }

                    try {
                        loadedClassesModel.addElement(loadedClass.getName() + ": " + (String) getInfoMethod.invoke(o));
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
                list2.setModel(loadedClassesModel);

            }
        });
    }
}

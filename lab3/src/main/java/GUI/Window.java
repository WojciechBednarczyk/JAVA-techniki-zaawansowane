package GUI;

import Logic.AnswerRepo;
import Logic.QuestionsRepo;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Window {
    private JPanel mainPanel;
    private JComboBox questionComboBox;
    private JLabel questionLabel;
    private JLabel answerLabel;
    private JButton languageButton;
    private JLabel correctAnswerLabel;
    private JLabel correctAnswerVerifyLabel;
    private JButton nextQuestionButton;
    private JTextField answerTextField;
    private JButton verifyAnswerButton;
    private JLabel showCorrectAnswerLabel;
    private ResourceBundle resourceBundle;
    String language = "en";
    String country = "EN";
    Locale locale = new Locale(language, country);

    public static void createWindow() throws IOException, ParseException {
        JFrame frame = new JFrame("Application");
        frame.setContentPane(new Window().mainPanel);
        frame.setVisible(true);
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initialize() throws IOException, ParseException {

        resourceBundle = ResourceBundle.getBundle("Bundle", locale);

        QuestionsRepo.changeQuestion(questionLabel, questionComboBox, resourceBundle);
        AnswerRepo.changeAnswer(answerLabel, resourceBundle);
        languageButton.setText(resourceBundle.getString("Change_language"));
        nextQuestionButton.setText(resourceBundle.getString("Next_question"));
        verifyAnswerButton.setText(resourceBundle.getString("Check_answer"));
        correctAnswerLabel.setText(resourceBundle.getString("The_correct_answer_is"));
    }

    public Window() throws IOException, ParseException {

        this.initialize();

        nextQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showCorrectAnswerLabel.setText("");
                    correctAnswerVerifyLabel.setText("");
                    answerTextField.setText("");
                    QuestionsRepo.changeQuestion(questionLabel, questionComboBox, resourceBundle);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                AnswerRepo.changeAnswer(answerLabel, resourceBundle);
            }
        });
        verifyAnswerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    QuestionsRepo.checkAnswer(questionComboBox.getSelectedItem().toString(), correctAnswerVerifyLabel, answerTextField.getText(), showCorrectAnswerLabel, resourceBundle);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });
        languageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (language.equals("en")) {
                    language = "pl";
                    country = "PL";
                    locale = new Locale(language, country);
                } else {
                    language = "en";
                    country = "EN";
                    locale = new Locale(language, country);
                }
                resourceBundle = ResourceBundle.getBundle("Bundle", locale);
                languageButton.setText(resourceBundle.getString("Change_language"));
                nextQuestionButton.setText(resourceBundle.getString("Next_question"));
                verifyAnswerButton.setText(resourceBundle.getString("Check_answer"));
                correctAnswerLabel.setText(resourceBundle.getString("The_correct_answer_is"));
                AnswerRepo.refreshAnswer(answerLabel, resourceBundle);
                QuestionsRepo.refreshAnswer(questionLabel, resourceBundle, correctAnswerVerifyLabel);
            }
        });

    }
}

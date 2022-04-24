package Logic;

import Api.HTTPUrlConnector;
import GUI.Window;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QuestionsRepo {

    static String questions[] = {
            "How_many_countries_are_in_continent",
            "On_what_continent_is_located_country",
            "How_many_first-level_administrative_units_are_there_in_the_country",
    };

    static int current_question = -1;

    public static void changeQuestion(JLabel questionLabel, JComboBox questionComboBox, ResourceBundle resourceBundle) throws IOException, ParseException {

        current_question++;
        if (current_question >= questions.length)
            current_question = 0;
        questionLabel.setText(resourceBundle.getString(questions[current_question]));
        ArrayList<String> result = HTTPUrlConnector.sendGetForQuestions(current_question);
        questionComboBox.removeAllItems();
        for (String item : result) {
            questionComboBox.addItem(item);
        }
    }

    public static void checkAnswer(String question, JLabel correctAnswerVerifyLabel, String answer, JLabel showCorrectAnswerLabel, ResourceBundle resourceBundle) throws IOException, ParseException {
        String correctAnswer = HTTPUrlConnector.sendGetForAnswer(current_question, question);
        showCorrectAnswerLabel.setText(correctAnswer);
        if (answer.equals(correctAnswer)) {
            correctAnswerVerifyLabel.setText(resourceBundle.getString("Correct_answer!"));
        } else
            correctAnswerVerifyLabel.setText(resourceBundle.getString("Incorrect_answer!"));
    }

    public static void refreshAnswer(JLabel questionLabel, ResourceBundle resourceBundle, JLabel correctAnswerVerifyLabel) {
        questionLabel.setText(resourceBundle.getString(questions[current_question]));
        if (correctAnswerVerifyLabel.getText().length() > 1) {
            correctAnswerVerifyLabel.setText(resourceBundle.getString(correctAnswerVerifyLabel.getText().replaceAll("\\s+", "_")));
        }
    }
}

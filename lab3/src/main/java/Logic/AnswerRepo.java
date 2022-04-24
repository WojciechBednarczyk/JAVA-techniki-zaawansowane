package Logic;

import javax.swing.*;
import java.util.ResourceBundle;

public class AnswerRepo {

    static String answers[] = {
            "The_number_of_countries_on_the_selected_continent_is",
            "The_selected_country_is_on_the_continent",
            "The_number_of_first_level_administrative_units_in_the_selected_country_is",
    };

    static int current_answer = -1;

    public static void changeAnswer(JLabel answerLabel, ResourceBundle resourceBundle) {

        current_answer++;
        if (current_answer >= answers.length)
            current_answer = 0;
        answerLabel.setText(resourceBundle.getString(answers[current_answer]));

    }

    public static void refreshAnswer(JLabel answerLabel, ResourceBundle resourceBundle) {
        answerLabel.setText(resourceBundle.getString(answers[current_answer]));

    }
}

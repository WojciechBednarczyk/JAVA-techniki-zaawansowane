package processing;

import javax.swing.*;

public interface StatusListener {
    /**
     * Metoda słuchacza
     * @param s - status przetwarzania zadania
     */
    void statusChanged(Status s);
}

package Application;

import processing.Status;
import processing.StatusListener;

import javax.swing.*;

public class MyStatusListener implements StatusListener {
    // listener jest znany podczas kompilacji projektu
    // można więc wiązać się w nim z interfejsem użytkownika
    @Override
    public void statusChanged(Status s) {
        if (s.getProgress()%10==0)
        {
            System.out.println("Progress:"+s.getProgress()+"% TaskId:" +s.getTaskId());
        }
    }

}

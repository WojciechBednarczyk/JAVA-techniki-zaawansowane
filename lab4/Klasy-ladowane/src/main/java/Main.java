import processing.Status;
import processing.StatusListener;
import processors.AdderProcessor;

public class Main {

    public static void main(String[] args) {
        AdderProcessor adderProcessor = new AdderProcessor();

        adderProcessor.submitTask("5 + 2", new StatusListener() {
            @Override
            public void statusChanged(Status s) {

            }


        });
    }
}

package apps.billboard;

import java.time.Duration;

public class DisplayedAdvertisement {

    String advertText;
    Duration displayPeriod;

    public DisplayedAdvertisement(String advertText, Duration displayPeriod) {
        this.advertText = advertText;
        this.displayPeriod = displayPeriod;
    }
}

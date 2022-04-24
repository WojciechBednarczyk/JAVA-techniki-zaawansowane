package PersonalData;

import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.WeakHashMap;

@NoArgsConstructor
public class PersonalDataLoader {

    WeakHashMap<String, PersonalData> weakHashMap = new WeakHashMap<String, PersonalData>();

    public PersonalData loadPersonalData(String id) throws IOException {
        PersonalData result = weakHashMap.get(id);
        if (result==null)
        {
            BufferedImage image = ImageIO.read(new File("Data/" + id + "/image.png"));
            File file = new File("Data/" + id + "/record.txt");
            Scanner scanner = new Scanner(file);
            scanner.nextLine();
            result = new PersonalData(id,scanner.nextLine(), scanner.nextLine(), LocalDate.parse(scanner.nextLine()),scanner.nextLine(),image,"Z pliku");
            weakHashMap.put(new String(id),result);
            return result;
        }
        else
        {
            result.setStatus("Z pamieci");
            return result;
        }
    }
}

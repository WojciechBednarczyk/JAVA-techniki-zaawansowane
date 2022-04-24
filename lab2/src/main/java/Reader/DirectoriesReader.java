package Reader;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor
public class DirectoriesReader {

    @Getter
    private File directoryPath = new File("Data");

    public String[] readDirectories(){
        String[] readedDirectories = directoryPath.list();
        return readedDirectories;
    }
}

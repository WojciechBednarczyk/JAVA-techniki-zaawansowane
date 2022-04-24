package library;

import library.Status;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@Getter @Setter
public class ExaminedFile{

    private Path filePath;
    private Status status;
    private String MD5Code;

    public ExaminedFile(Path filePath) {
        this.filePath = filePath;
    }

}

package library;

import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@NoArgsConstructor
public class MD5StatusChecker {

    private String previousMD5Code;
    private Map<Path,String> MD5Codes = new HashMap<>();

    public Status checkStatus(Path filePath) throws NoSuchAlgorithmException, IOException {
        ExaminedFile examinedFile = new ExaminedFile(filePath);
        previousMD5Code = MD5Codes.get(filePath);
        String resultOfCalculate = MD5Calculate.calculate(examinedFile);
        if (previousMD5Code==null) {
            examinedFile.setStatus(Status.NEW);
        }
        else if (previousMD5Code.equals(resultOfCalculate.toString())){
            examinedFile.setStatus(Status.NOT_CHANGED);
        }
        else {
            examinedFile.setStatus(Status.CHANGED);
        }
        MD5Codes.put(filePath,resultOfCalculate.toString());

        return examinedFile.getStatus();
    }

    //load from file
    public void load() throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("MD5Codes"));
        }catch (FileNotFoundException e)
        {
            File file = new File("MD5Codes");
            file.createNewFile();
        }

        for (String key : properties.stringPropertyNames()) {
            MD5Codes.put(Paths.get(key), properties.get(key).toString());
        }
    }

    //save to file
    public void save() throws IOException {
        Properties properties = new Properties();

        for (Map.Entry<Path,String> entry : MD5Codes.entrySet()) {
            properties.put(entry.getKey().toString(), entry.getValue().toString());
        }
        properties.store(new FileOutputStream("MD5Codes"), null);
    }
}

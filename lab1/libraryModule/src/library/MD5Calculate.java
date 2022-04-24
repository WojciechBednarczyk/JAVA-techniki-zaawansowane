package library;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Calculate {

    public static String calculate(ExaminedFile examinedFile) throws NoSuchAlgorithmException, IOException {

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        InputStream inputStream = Files.newInputStream(examinedFile.getFilePath());
        DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
        int bytesCount = 0;
        byte[] byteArray = new byte[1024];
        while ((bytesCount = inputStream.read(byteArray)) != -1) {
            messageDigest.update(byteArray, 0, bytesCount);
        };
        inputStream.close();
        byte[] digest = messageDigest.digest();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< digest.length ;i++)
        {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}

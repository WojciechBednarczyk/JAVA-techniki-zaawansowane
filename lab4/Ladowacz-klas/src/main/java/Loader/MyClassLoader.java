package Loader;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader{

    public Path searchPath;

    public MyClassLoader(Path path) {
        searchPath = path;
    }
    public Class<?> findClass(String binName) throws ClassNotFoundException{

        Path classFile = Paths.get(searchPath +
                FileSystems.getDefault().getSeparator() +
                FileSystems.getDefault().getSeparator() +
                binName.replace(".",FileSystems.getDefault().getSeparator()) +
                ".class");

        byte[] buf = null;

        try {
            buf = Files.readAllBytes(classFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(binName,buf,0, buf.length);
    }
}

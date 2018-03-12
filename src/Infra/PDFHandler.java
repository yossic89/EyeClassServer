package Infra;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PDFHandler {

    //TODO by config
    private static String path = "D:\\EyeClass\\PDF";

    public static PDFSaveAck SaveAsPdf(byte[] arr)
    {
        //check if path dir exists
        if (!Files.exists(Paths.get(path)))
        {
            try {
                Files.createDirectories(Paths.get(path));
            } catch (IOException e) {
                System.out.println("Cannot create directories - " + e);
            }
        }

        //build path
        String fullPath = String.format("%s\\%s.pdf", path, generateName());
        boolean isSuccesSave = true;
        OutputStream out = null;
        try {
            out = new FileOutputStream(fullPath);
            out.write(arr);
            out.close();
        } catch (Exception e) {
            isSuccesSave = false;
        }
        return new PDFSaveAck(isSuccesSave, fullPath);
    }

    private static synchronized String generateName()
    {
        return Long.toString(System.currentTimeMillis());
    }

    public static class PDFSaveAck{

        public PDFSaveAck(boolean pass, String path)
        {
            this.isSuccess = pass;
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        String path;
        boolean isSuccess;
    }
}



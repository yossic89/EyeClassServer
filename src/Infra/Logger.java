package Infra;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Logger {

    public static void log(String data) throws IOException {
        if (m_log == null)
            m_log = new Logger();
        m_log.write(data);
    }

    private Logger()
    {
        initLogger();
    }

    public void write(String data) throws IOException {
        writer.write(data);
        writer.newLine();
        writer.flush();
    }

    private void initLogger()
    {
        String path = Config.getInstance().getDebug().getLogDir();
        String dir = path +"\\"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMYY_HHmmssSSS"));
        String javaPath = Paths.get(dir).toString();
        if (!Files.exists(Paths.get(dir)))
        {
            try {
                Files.createDirectories(Paths.get(dir));
            } catch (IOException e) {
                System.err.println("Cannot create directories - " + e);
            }
        }
        String fullPath = String.format("%s\\Logger.txt", dir);
        File logFile = new File(fullPath);
        try
        {
            writer =  new BufferedWriter(new FileWriter(logFile, true));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static Logger m_log = null;
    private BufferedWriter writer = null;

}

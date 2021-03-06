package Infra;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EyeBase {

    public EyeBase(){}

    protected void log(String data)  {
        StackTraceElement stackTraceElements =  new Exception().getStackTrace()[1];
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String output = String.format("%s| %s | %s:%d",dt, data, stackTraceElements.getFileName(), stackTraceElements.getLineNumber());
        //sout if debug mode enable
        if (Config.getInstance().getDebug().getDebugMode())
            System.out.println(output);
        try
        {
            Logger.log(output);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

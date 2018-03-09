package Infra;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EyeBase {

    public EyeBase(){}

    protected void Log(String data)  {
        StackTraceElement stackTraceElements =  new Exception().getStackTrace()[1];
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String output = String.format("%s| %s | %s:%d",dt, data, stackTraceElements.getFileName(), stackTraceElements.getLineNumber());
        System.out.println(output);
        /*try
        {
            Logger.Log(output);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }
}

package Infra;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class Config {

    private static final String PATH = "C:\\config.json";

    private static Configuration config = null;

    public static Configuration getInstance()
    {
      System.out.println("0000000");
        if (config == null)
        {
            try {
                Gson gson = new Gson();
                config = gson.fromJson(new FileReader(PATH), Configuration.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

}

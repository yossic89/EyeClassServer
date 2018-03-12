package Infra;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class Config {

    private static final String PATH = "Config.xml";

    private static Configuration config = null;

    public static Configuration getInstance()
    {
        System.out.println("0000000");
        if (config == null)
        {
            StringReader reader = new StringReader(PATH);
            JAXBContext jaxbContext = null;
            try {
                System.out.println("111111111");
                jaxbContext = JAXBContext.newInstance(Configuration.class);
                System.out.println("222222222");
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                System.out.println("3333333");
                config = (Configuration) jaxbUnmarshaller.unmarshal(reader);

                if (config == null)
                    System.out.println("NO GODD");
                else
                    System.out.println("GOODDD");
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        System.out.println("9999999");
        return config;
    }

}

import Infra.Config;

public class Main {
    public static void main(String [ ] args){
        //set config file for local mode
        if (args.length > 0)
            Config.readFromOutsidePath(args[0]);
        new ConsoleUT().mainMenu();

    }
}

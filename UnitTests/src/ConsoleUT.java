import Infra.EyeBase;
import Infra.Logger;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Student;

public class ConsoleUT {
    public static void main(String [ ] args){
        System.out.println("Hello World");
        School sc = new School("ORT", "Holon");
        sc.Test("TEST");
    }
}

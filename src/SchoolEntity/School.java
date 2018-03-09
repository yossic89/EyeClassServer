package SchoolEntity;

import Infra.EyeBase;
import SchoolEntity.UsersEntity.User;

import java.util.ArrayList;

public class School extends EyeBase {

    public School(String name, String address)
    {
        this.schoolName = name;
        this.address = address;
        classes = new ArrayList<>();
    }

    public void Test(String d)
    {
        Log(d);
    }

    public String GetName(){return schoolName;}

    private String schoolName;
    private String address;
    ArrayList<Class> classes;
}

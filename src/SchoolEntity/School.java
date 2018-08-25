package SchoolEntity;

import Infra.EyeBase;
import SchoolEntity.UsersEntity.User;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class School extends EyeBase {

    public School(String name, String address)
    {
        this.schoolName = name;
        this.address = address;
        classes = new ArrayList<>();
    }

    public boolean addClass(Class c)
    {
       if (classes.contains(c.getID()))
            return false;
        classes.add(c.getID());
        return true;
    }

    public String GetName(){return schoolName;}

    @Id
    private String schoolName;
    private String address;
    //@ManyToOne(cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Class.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    ArrayList<String> classes;
}

package SchoolEntity.UsersEntity;

import SchoolEntity.Class;
import SchoolEntity.School;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Student extends User {
    public Student(long id, String password, School school, String fullName, SchoolEntity.Class p_class) {
        super(id, password, school, fullName);
        m_class = p_class;
        //class_id=p_class.getID();
    }

    public SchoolEntity.Class getStudentClass(){return  m_class;}

    //@OneToMany(cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Class.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SchoolEntity.Class m_class;
    //@OneToMany(cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Class.class)
   // private String class_id;
}

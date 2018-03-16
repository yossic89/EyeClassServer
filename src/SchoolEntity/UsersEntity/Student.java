package SchoolEntity.UsersEntity;

import SchoolEntity.School;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Student extends User {
    public Student(long id, String password, School school, String fullName,String classId) {
        super(id, password, school, fullName);
        m_classId = classId;
        //class_id=p_class.getID();
    }

    public String getStudentClassId(){return  m_classId;}

    //@OneToMany(cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Class.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private String m_classId;
    //@OneToMany(cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = Class.class)
   // private String class_id;
}

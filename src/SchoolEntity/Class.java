package SchoolEntity;

import Infra.CommonEnums;
import Infra.EyeBase;
import SchoolEntity.UsersEntity.Student;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Class extends EyeBase implements Serializable {

    public Class(CommonEnums.SchoolClasses grade, int grade_id, School school){
        this.grade = grade;
        this.grade_id = grade_id;
        students = new ArrayList<Long>();

        //generate unique id
        this.id = String.format("%s_%s", school.GetName(), this.GetClassName());
    }

    public boolean AddStudent(long studentId)
    {
        if (students.contains(studentId))
            return false;
        students.add(studentId);
        return true;
    }

    public ArrayList<Long> getStudents() {
        return students;
    }

    public String getID(){return this.id;}

    public String GetClassName(){return grade.name() + "_" + grade_id;}

    @Id
    private String id;
    private CommonEnums.SchoolClasses grade;
    private int grade_id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ArrayList<Long> students;


}

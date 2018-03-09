package SchoolEntity;

import Infra.CommonEnums;
import Infra.EyeBase;
import SchoolEntity.UsersEntity.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Class extends EyeBase implements Serializable {

    public Class(CommonEnums.SchoolClasses grade, int grade_id, School school){
        this.grade = grade;
        this.grade_id = grade_id;
        students = new ArrayList<Student>();

        //generate unique id
        this.id = String.format("%s_%s", school.GetName(), this.GetClassName());
    }

    public boolean AddStudent(Student s)
    {
        if (students.contains(s))
            return false;
        students.add(s);
        return true;
    }

    public String GetClassName(){return grade.name() + "_" + grade;}

    private String id;
    private CommonEnums.SchoolClasses grade;
    private int grade_id;
    private ArrayList<Student> students;
}

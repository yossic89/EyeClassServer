package Engine;

import Infra.CommonEnums;
import Infra.EyeBase;
import LessonManager.MultipleQuestion;
import SchoolEntity.Class;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import SchoolEntity.UsersEntity.User;

import java.util.ArrayList;
import java.util.List;

public class SchoolServer extends EyeBase {

    public SchoolServer(School school)
    {
        m_school = school;
    }

    public School getSchool(){return m_school;}

    public boolean addStuent(Student s)
    {
        boolean addStudent = s.getStudentClass().AddStudent(s);
        if (addStudent)
            return DBConnection.GetInstance().Save(s);
        return false;
    }

    public boolean addLesson(byte[] pdfBytes, long teacher_id, CommonEnums.Curriculum cur, List<MultipleQuestion> ques)
    {
        //get teacher

        return true;
    }

    public boolean addClass(Class c)
    {
        boolean addClass = m_school.addClass(c);
        if (addClass)
            return DBConnection.GetInstance().Save(c);
        return false;
    }

    public Class getClassByGradeId(CommonEnums.SchoolClasses grade, int grade_id)
    {
        String id = String.format("%s_%s_%d", m_school.GetName(), grade.name(), grade_id);
        return DBConnection.GetInstance().getClassByName(id);
    }

    public boolean addTeacher(Teacher t)
    {
        return DBConnection.GetInstance().Save(t);
    }

    public List<User> getAllUsers()
    {
        return DBConnection.GetInstance().getAllUsersForSchool(m_school.GetName());
    }

    private School m_school;
}

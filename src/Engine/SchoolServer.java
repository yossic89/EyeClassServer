package Engine;

import Infra.CommonEnums;
import Infra.EyeBase;
import Infra.Logger;
import LessonManager.MultipleQuestion;
import SchoolEntity.Class;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import SchoolEntity.UsersEntity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SchoolServer extends EyeBase {

    public SchoolServer(School school)
    {
        m_school = school;
        classMap = new HashMap<>();
    }

    public void initMaps(){

    }

    public School getSchool(){return m_school;}

    public boolean addLesson(byte[] pdfBytes, long teacher_id, CommonEnums.Curriculum cur, List<MultipleQuestion> ques)
    {
        //get teacher

        return true;
    }

    public boolean addClass(Class c)
    {
        addClassToMap(c);
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

    //Class
    private boolean checkIfClassExist(String classId){
        if (!classMap.containsKey(classId)){
            Log("checkIfClassExist: The class with the id: "+classId +" is not exist.");
            return false;
        }
        return true;
    }

    public String getClassName(String classId){
        if (!checkIfClassExist(classId)) return null;
        Class c = classMap.get(classId);
        return c.GetClassName();
    }

    public boolean addStudentToClass(String classId,Student student){
        if (!checkIfClassExist(classId)) return false;
        Class c = classMap.get(classId);
        if (!c.AddStudent(student)) return false;
        if (!DBConnection.GetInstance().Save(student)) return false;
        Log("addStudentToClass: The student: "+ student.getM_id() + " added to class: "+classId);
        return true;
    }

    public void addClassToMap(Class c){
        classMap.put(c.getID(),c);
    }

    public Class getClassFromMap(String classId){
        return classMap.get(classId);
    }


    private School m_school;
    private HashMap<String, Class> classMap;
}

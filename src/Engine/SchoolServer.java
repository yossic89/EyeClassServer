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
        usersMap = new HashMap<>();
    }

    public void initMaps(){
        //init classes
        for (SchoolEntity.Class c : DBConnection.GetInstance().getAllClassesBySchool(m_school.GetName()))
            classMap.put(c.getID(), c);
    }

    public School getSchool(){return m_school;}

    public boolean addLesson(byte[] pdfBytes, long teacher_id, CommonEnums.Curriculum cur, List<MultipleQuestion> ques)
    {
        //get teacher

        return true;
    }

    public boolean addClass(Class c)
    {
        if (!checkIfClassExist(c.getID())) addClassToMap(c);
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
        Class c = getClassFromMap(classId);
        return c.GetClassName();
    }

    public boolean addStudentToClass(String classId,Student student){
        //Add student to student maps
        if (!checkIfStudentExist(student.getM_id())) usersMap.put(student.getM_id(),student);
        else return false;

        //check if class exist
        if (!checkIfClassExist(classId)) return false;
        Class c = getClassFromMap(classId);

        //add student to instance class and to db
        if (!c.AddStudent(student.getM_id())) return false;
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

    //Users
    private boolean checkIfStudentExist(long studentId){
        if (!usersMap.containsKey(studentId)){
            return false;
        }
        return true;
    }

    public void addStudentToMap(Student student){
        usersMap.put(student.getM_id(),student);
    }

    public Student getStudentFromMap(long studentId){
        return (Student)usersMap.get(studentId);
    }

    public String getPassword(long studentId){
        if (!checkIfStudentExist(studentId)){
            Log("getPassword: The student with the id: "+studentId +" is not exist.");
            return null;
        }
        return getStudentFromMap(studentId).getM_password();
    }

    public String getFullNameOfUser(long studentId){
        if (!checkIfStudentExist(studentId)){
            Log("getFullNameOfUser: The student with the id: "+studentId +" is not exist.");
            return null;
        }
        return getStudentFromMap(studentId).getM_fullName();
    }

    public School getSchoolOfUser(long studentId){
        if (!checkIfStudentExist(studentId)){
            Log("getSchoolOfUser: The student with the id: "+studentId +" is not exist.");
        }
        return getStudentFromMap(studentId).getM_school();
    }

    private School m_school;
    private HashMap<String, Class> classMap;
    private HashMap<Long, User> usersMap;
}

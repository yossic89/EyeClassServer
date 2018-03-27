package Engine;

import Infra.CommonEnums;
import Infra.EyeBase;
import Infra.Logger;
import Infra.PDFHandler;
import LessonManager.Lesson;
import LessonManager.MultipleQuestion;
import SchoolEntity.Class;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import SchoolEntity.UsersEntity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        //init users
        for (SchoolEntity.UsersEntity.User user : DBConnection.GetInstance().getAllUsersForSchool(m_school.GetName()))
            usersMap.put(user.getM_id(), user);

    }

    public School getSchool(){return m_school;}

    public boolean addLesson(byte[] pdfBytes, String headline, long teacher_id, CommonEnums.Curriculum cur, ArrayList<MultipleQuestion> ques)
    {
        //check validity teacher_id
        if (!checkIfUserExist(teacher_id))
            return false;
        //save pdf
        PDFHandler.PDFSaveAck ack = PDFHandler.SaveAsPdf(pdfBytes);
        if (!ack.isSuccess())
            return false;
        //add new lesson
        Lesson lesson=new Lesson(ack.getPath(), ques, teacher_id, headline, cur);
        //save to DB
        DBConnection.GetInstance().Save(lesson);
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
        if (!checkIfUserExist(t.getM_id())) usersMap.put(t.getM_id(),t);
        else{
            Log("addTeacher: The user with Id: "+ t.getM_id() + " is already exist");
            return false;
        }
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
        if (!checkIfUserExist(student.getM_id())) usersMap.put(student.getM_id(),student);
        else{
            Log("addStudentToClass: The user with Id: "+ student.getM_id() + " is already exist");
            return false;
        }

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
    private boolean checkIfUserExist(long studentId){
        if (!usersMap.containsKey(studentId)){
            return false;
        }
        return true;
    }

    public ArrayList<Lesson> getAllLessonsForTeacher(long id)
    {
        return DBConnection.GetInstance().getLessonsForTeacher(id);
    }

    public ArrayList<Long> getAllTeacherId()
    {
        ArrayList<Long> teachers = new ArrayList<>();
        for (Map.Entry<Long, User> entry : usersMap.entrySet())
        {
            if (entry.getValue() instanceof Teacher)
                teachers.add(entry.getKey());
        }
        return teachers;
    }

    public void addStudentToMap(Student student){
        usersMap.put(student.getM_id(),student);
    }

    public Student getStudentFromMap(long studentId){
        return (Student)usersMap.get(studentId);
    }

    public Teacher getTeacherFromMap(long teacherId){
        User u=usersMap.get(teacherId);
        if(u instanceof Teacher)
            return (Teacher)u;
        //TODO: not so nice to return null..
        return null;
    }

    public String getPassword(long studentId){
        if (!checkIfUserExist(studentId)){
            Log("getPassword: The student with the id: "+studentId +" is not exist.");
            return null;
        }
        return getStudentFromMap(studentId).getM_password();
    }

    public String getFullNameOfUser(long studentId){
        if (!checkIfUserExist(studentId)){
            Log("getFullNameOfUser: The student with the id: "+studentId +" is not exist.");
            return null;
        }
        return getStudentFromMap(studentId).getM_fullName();
    }

    public String getSchoolOfUser(long studentId){
        if (!checkIfUserExist(studentId)){
            Log("getSchoolOfUser: The student with the id: "+studentId +" is not exist.");
        }
        return getStudentFromMap(studentId).get_schoolId();
    }

    private School m_school;
    private HashMap<String, Class> classMap;
    private HashMap<Long, User> usersMap;
}

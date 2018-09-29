package Engine;

import Distractions.MeasureParams;
import Infra.CommonEnums;
import Infra.EyeBase;
import Infra.PDFHandler;
import LessonManager.ActiveLesson;
import LessonManager.Lesson;
import LessonManager.MultipleQuestion;
import SchoolEntity.Class;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Admin;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import SchoolEntity.UsersEntity.User;
import ViewModel.AdminDistractionParamViewModel;
import ViewModel.QuestionAnsViewModel;
import ViewModel.TeacherDistractionParamViewModel;
import ViewModel.UsersViewModel;

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
        m_classesActiveLesson = new HashMap<>();
    }

    public void initMaps(){
        //init classes
        for (SchoolEntity.Class c : DBConnection.GetInstance().getAllClassesBySchool(m_school.GetName()))
            classMap.put(c.getID(), c);

        //init users
        for (SchoolEntity.UsersEntity.User user : DBConnection.GetInstance().getAllUsersForSchool(m_school.GetName()))
            usersMap.put(user.getM_id(), user);
    }

    public List<SchoolEntity.Class> getAllClasses() {

        return DBConnection.GetInstance().getAllClassesBySchool(m_school.GetName());
    }

    public List<TeacherDistractionParamViewModel> getDistractionForTeacher(long id){return DBConnection.GetInstance().getDistractionForTeacher(id);}

    public List<QuestionAnsViewModel> getQuestionsAnsForTeacher(long teacher_id){return DBConnection.GetInstance().getQuestionsAnsForTeacher(teacher_id);}

    public List<AdminDistractionParamViewModel> getDistractionForAdmin(){return DBConnection.GetInstance().getDistractionForAdmin(m_school.GetName());}

    public School getSchool(){return m_school;}

    public boolean addLesson(byte[] pdfBytes, String headline, long teacher_id, CommonEnums.Curriculum cur, List<MultipleQuestion> ques)
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

    public void setTrackerForClass(String class_id, boolean toTrack)
    {
        m_classesActiveLesson.get(class_id).setCollectData(toTrack);
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

    public String addTeacher(Teacher t)
    {
        String err = "";
        if (!checkIfUserExist(t.getM_id())) usersMap.put(t.getM_id(),t);
        else{
            err = ("The user with Id: "+ t.getM_id() + " is already exist");
            return err;
        }
        if (!DBConnection.GetInstance().Save(t))
            err = "Internal DB error";
        return err;
    }

    public String addAdmin(Admin a)
    {
        String err = "";
        if (!checkIfUserExist(a.getM_id())) usersMap.put(a.getM_id(),a);
        else{
            err =("addAdmin: The user with Id: "+ a.getM_id() + " is already exist");
            return err;
        }
        if (!DBConnection.GetInstance().Save(a))
            err = "Internal DB error";
        return err;
    }

    public List<UsersViewModel> getAllUsersViewModel()
    {
        List<UsersViewModel> retVal = new ArrayList<>();
        for(User u :getAllUsers())
        {
            if (u instanceof Student){
                Student s = (Student)u;
                String class_name = classMap.get(s.getStudentClassId()).GetClassName();
                retVal.add(new UsersViewModel(s.getM_id(), s.getM_fullName(), CommonEnums.UserTypes.Student, class_name));
            }
            else if (u instanceof Teacher){
                Teacher t = (Teacher)u;
                retVal.add(new UsersViewModel(t.getM_id(), t.getM_fullName(), CommonEnums.UserTypes.Teacher, t.getCurriculum()));
            }
            else if (u instanceof Admin)
            {
                retVal.add(new UsersViewModel(u.getM_id(), u.getM_fullName(), CommonEnums.UserTypes.Admin));
            }
        }
        return retVal;
    }

    public List<User> getAllUsers()
    {
        return DBConnection.GetInstance().getAllUsersForSchool(m_school.GetName());
    }

    //Class
    private boolean checkIfClassExist(String classId){
        if (!classMap.containsKey(classId)){
            log("checkIfClassExist: The class with the id: "+classId +" is not exist.");
            return false;
        }
        return true;
    }

    public String getClassName(String classId){
        if (!checkIfClassExist(classId)) return null;
        Class c = getClassFromMap(classId);
        return c.GetClassName();
    }

    public String addStudentToClass(String classId,Student student){
        String err = "";
        //Add student to student maps
        if (!checkIfUserExist(student.getM_id())) usersMap.put(student.getM_id(),student);
        else{
            err = ("The user with Id: "+ student.getM_id() + " is already exist");
            return err;
        }

        //check if class exist
        if (!checkIfClassExist(classId))
        {
            err = "Class: " + classId + "not exsits in the system";
            return err;
        }
        Class c = getClassFromMap(classId);

        //add student to instance class and to db
        if (!c.AddStudent(student.getM_id()))
        {
            err = "User " + student.getM_fullName() + " is already in the class";
            return err;
        }
        if (!DBConnection.GetInstance().Save(student)){
            err = "Internal DB error";
            return err;
        }
        log("addStudentToClass: The student: "+ student.getM_id() + " added to class: "+classId);
        return err;
    }

    //Users
    private boolean checkIfUserExist(long studentId){
        if (!usersMap.containsKey(studentId)){
            return false;
        }
        return true;
    }

    public List<Lesson> getAllLessonsForTeacher(long id)
    {
        return DBConnection.GetInstance().getLessonsForTeacher(id);
    }

    public List<Long> getAllTeacherId()
    {
        ArrayList<Long> teachers = new ArrayList<>();
        for (Map.Entry<Long, User> entry : usersMap.entrySet())
        {
            if (entry.getValue() instanceof Teacher)
                teachers.add(entry.getKey());
        }
        return teachers;
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
            log("getPassword: The student with the id: "+studentId +" is not exist.");
            return null;
        }
        return getStudentFromMap(studentId).getM_password();
    }

    public String getFullNameOfUser(long studentId){
        if (!checkIfUserExist(studentId)){
            log("getFullNameOfUser: The student with the id: "+studentId +" is not exist.");
            return null;
        }
        return getStudentFromMap(studentId).getM_fullName();
    }

    public String getSchoolOfUser(long studentId){
        if (!checkIfUserExist(studentId)){
            log("getSchoolOfUser: The student with the id: "+studentId +" is not exist.");
        }
        return getStudentFromMap(studentId).get_schoolId();
    }

    public void startLesson(long id, String class_id)
    {
        //get lesson
        Lesson l = DBConnection.GetInstance().getLessonById(id);
        if (l == null)
        {
            log("Lesson with id:"+ id +" is not exists.");
            return;
        }

        //Start active lesson
        if (m_classesActiveLesson.containsKey(class_id))
        {
            log(String.format("Class %s has lesson that deleted", classMap.get(class_id)));
            m_classesActiveLesson.remove(class_id);
        }

        log(String.format("Lesson(%s), id:%d is up", l.get_lessonHeadline(), id));
        m_classesActiveLesson.put(class_id, new ActiveLesson(l, classMap.get(class_id).getStudents()));
    }

    public Map<String, CommonEnums.StudentConcentratedStatus> getStudentsStatus(String class_id)
    {
        Map<String, CommonEnums.StudentConcentratedStatus> retVal = new HashMap<>();
        if (!(m_classesActiveLesson.containsKey(class_id)))
        {
            log("Failed to find active lesson for class: " + class_id);
        }
        else
        {
            Map<Long, CommonEnums.StudentConcentratedStatus> data = m_classesActiveLesson.get(class_id).getStudentsStatus();
            for (Map.Entry<Long, CommonEnums.StudentConcentratedStatus> entry : data.entrySet())
            {
                retVal.put(usersMap.get(entry.getKey()).getM_fullName(), entry.getValue());
            }
        }
        return retVal;
    }

    public void handleMeasureParamForStudent(String studentClassId, MeasureParams param)
    {
        if (m_classesActiveLesson.containsKey(studentClassId))
            m_classesActiveLesson.get(studentClassId).handleMeasureParam(param);
        else
            log("Unable ro find class_id: " + studentClassId);
    }

    public long getTeacherIdByClass(String class_id) {return m_classesActiveLesson.get(class_id).getTeacherId();}

    public long getLessonIdByClass(String class_id) {return m_classesActiveLesson.get(class_id).getLessonId();}

    public void setTeacherPageForLesson(String class_id, int page){m_classesActiveLesson.get(class_id).setTeacherPage(page);}

    public void setQuestionDataForDelivery(String class_id, String questionData){m_classesActiveLesson.get(class_id).setQuestionDataForDeliver(questionData);}

    public boolean CheckIfLessonActive(String class_id) {return m_classesActiveLesson.containsKey(class_id);}

    public String getQuestionDataOfActiveLesson(String class_id) {return m_classesActiveLesson.get(class_id).getQuestionData();}

    public byte[] GetLessonPlan(String class_id){return m_classesActiveLesson.get(class_id).getPdfAsBytes();}

    public void addClassToMap(Class c){
        classMap.put(c.getID(),c);
    }

    public Class getClassFromMap(String classId){
        return classMap.get(classId);
    }

    public void endAllLessons()
    {
        for (String class_id : m_classesActiveLesson.keySet())
        {
            m_classesActiveLesson.get(class_id).endLesson();
        }
        m_classesActiveLesson.clear();
    }

    public void endLesson(String class_id)
    {
        if (m_classesActiveLesson.containsKey(class_id))
        {
            m_classesActiveLesson.get(class_id).endLesson();
            m_classesActiveLesson.remove(class_id);
            log(String.format("Lesson for class [%s] is down", class_id));
        }
        else
            log(String.format("Lesson for class [%s] not found", class_id));
    }

    public List<MultipleQuestion> getLessonQuestions(String class_id){
        if (!(m_classesActiveLesson.containsKey(class_id)))
        {
            log("Failed to find active lesson for class: " + class_id);
        }
        return m_classesActiveLesson.get(class_id).get_questions();
    }

    public int getTeacherPageForLesson(String class_id){return m_classesActiveLesson.get(class_id).getTeacherPage();}


    private School m_school;
    private HashMap<String, Class> classMap;
    private HashMap<Long, User> usersMap;
    private HashMap<String, ActiveLesson> m_classesActiveLesson;
}

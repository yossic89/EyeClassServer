package Engine;

import Distractions.MeasureParams;
import Infra.CommonEnums;
import Infra.Config;
import Infra.EyeBase;
import LessonManager.Lesson;
import LessonManager.MultipleQuestion;
import LessonManager.QuestionStatisticForStudent;
import SchoolEntity.Class;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Admin;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import SchoolEntity.UsersEntity.User;
import ViewModel.AdminDistractionParamViewModel;
import ViewModel.TeacherDistractionParamViewModel;
import ViewModel.UsersViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EyeClassEngine extends EyeBase {

    public static EyeClassEngine GetInstance()
    {

        if (eng == null)
        {
            eng = new EyeClassEngine();
            eng.initMap();
        }

        return eng;
    }

    public EyeClassEngine()
    {
        schoolsMap = new HashMap<>();
    }

    public void initMap()
    {
        for (School s : DBConnection.GetInstance().getAllSchool())
        {
            SchoolServer ss = new SchoolServer(s);
            ss.initMaps();
            schoolsMap.put(s.GetName(), ss);
        }
    }

    public SchoolServer getSchoolServer(String name)
    {
        SchoolServer retVal = null;
        if (schoolsMap.containsKey(name))
            retVal = schoolsMap.get(name);
        return retVal;
    }

    public void DeleteDB()
    {
        DBConnection.GetInstance().RestartDB();
    }

    public boolean AddSchool(School s)
    {
       boolean ret = DBConnection.GetInstance().Save(s);
       if (ret)
       {
           SchoolServer ss = new SchoolServer(s);
           schoolsMap.put(s.GetName(), ss);
       }

       return ret;
    }

    public User getUser(long id)
    {
        return DBConnection.GetInstance().getUser(id);
    }

    public CommonEnums.UserTypes GetUserType(long id, String pass)
    {
        CommonEnums.UserTypes ret = CommonEnums.UserTypes.NONE;
        User u = DBConnection.GetInstance().getUser(id, pass);
        if (u != null)
        {
            if (u instanceof Student)
                ret = CommonEnums.UserTypes.Student;
            else if (u instanceof Teacher)
                ret = CommonEnums.UserTypes.Teacher;
            else if (u instanceof Admin)
                ret = CommonEnums.UserTypes.Admin;
        }
        return ret;
    }

    public void StartLesson(Teacher t, long lesson_id, String class_id)
    {
        schoolsMap.get(t.get_schoolId()).startLesson(lesson_id, class_id);
    }

    public ArrayList<Lesson> getAllLessonsForTeacher(Teacher t){return schoolsMap.get(t.get_schoolId()).getAllLessonsForTeacher(t.getM_id());}

    public Map<String, CommonEnums.StudentConcentratedStatus> getStudentsLessonStatus(User u, String class_id) {return schoolsMap.get(u.get_schoolId()).getStudentsStatus(class_id);}

    public byte[] getLessonPlanDataForClass(User u, String class_id){return schoolsMap.get((u.get_schoolId())).GetLessonPlan(class_id);}

    public boolean getActiveLessonForStudent(Student s){return schoolsMap.get(s.get_schoolId()).CheckIfLessonActive(s.getStudentClassId());}

    public long getTeacherIdOfActiveLessonByStudent(Student s){return schoolsMap.get(s.get_schoolId()).getTeacherIdByClass(s.getStudentClassId());}

    public long getLessonIdOfActiveLessonByStudent(Student s){return schoolsMap.get(s.get_schoolId()).getLessonIdByClass(s.getStudentClassId());}

    public void handleStudentMeasuring(Student s, MeasureParams params){schoolsMap.get(s.get_schoolId()).handleMeasureParamForStudent(s.getStudentClassId(), params);}

    public void setTeacherPageForLesson(Teacher t, String class_id, int page){schoolsMap.get(t.get_schoolId()).setTeacherPageForLesson(class_id, page);}

    public void setQuestionDataForDelivery(Teacher t, String class_id, String questionData){schoolsMap.get(t.get_schoolId()).setQuestionDataForDelivery(class_id,questionData);}

    public String getQuestionDataOfActiveLesson(Student s){return schoolsMap.get(s.get_schoolId()).getQuestionDataOfActiveLesson(s.getStudentClassId());}

    public int getPhotoSampling(){return Config.getInstance().getOpenCV().getSamplingIntervalMS();}

    public void endLesson(Teacher t, String class_id){schoolsMap.get(t.get_schoolId()).endLesson(class_id);}

    public boolean addLesson(Teacher t, byte[] pdfBytes, String headline, CommonEnums.Curriculum cur, ArrayList<MultipleQuestion> ques)
    {
        return schoolsMap.get(t.get_schoolId()).addLesson(pdfBytes, headline, t.getM_id(), cur, ques);
    }

    public void setTracker(Teacher t, String class_id, boolean track){schoolsMap.get(t.get_schoolId()).setTrackerForClass(class_id, track);}

    public List<Class> getAllClasses(User u){return schoolsMap.get(u.get_schoolId()).getAllClasses();}

    public List<TeacherDistractionParamViewModel> getDistractionForTeacher(Teacher t){return schoolsMap.get(t.get_schoolId()).getDistractionForTeacher(t.getM_id());}

    public void saveAnswerOfStudentInDB(Student s,String question, boolean isGoodAns, String studAns, long questionId ){
        long studId = s.getM_id();
        long lessonId = getLessonIdOfActiveLessonByStudent(s);
        long teacherId = getTeacherIdOfActiveLessonByStudent(s);
        QuestionStatisticForStudent questionStatisticForStudent = new QuestionStatisticForStudent(studId, lessonId, teacherId, questionId, question, isGoodAns, studAns);
        Log(String.format("Student with id: %d answer: %s " ,studId ,isGoodAns));
        if (!DBConnection.GetInstance().Save(questionStatisticForStudent))
            System.out.println("Can't save question statistic of student to db");
    }

    public List<AdminDistractionParamViewModel> getDistractionForAdmin(Admin a){return schoolsMap.get(a.get_schoolId()).getDistractionForAdmin();}

    public List<UsersViewModel> getAllUsersViewModel(Admin a){return schoolsMap.get(a.get_schoolId()).getAllUsersViewModel();}

    public ArrayList<MultipleQuestion> getQuestionsForClass(User u, String class_id){ return schoolsMap.get((u.get_schoolId())).getLessonQuestions(class_id);}


    HashMap<String, SchoolServer> schoolsMap;

    private static EyeClassEngine eng;

}

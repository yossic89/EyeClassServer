package Engine;

import Infra.CommonEnums;
import Infra.Config;
import Infra.EyeBase;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Admin;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import SchoolEntity.UsersEntity.User;

import java.util.HashMap;
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

    public void StartLesson(Teacher t, int lesson_id, String class_id)
    {
        schoolsMap.get(t.get_schoolId()).startLesson(1, class_id);
    }

    public Map<String, CommonEnums.StudentConcentratedStatus> getStudentsLessonStatus(User u, String class_id) {return schoolsMap.get(u.get_schoolId()).getStudentsStatus(class_id);}

    public byte[] getLessonPlanDataForClass(User u, String class_id){return schoolsMap.get((u.get_schoolId())).GetLessonPlan(class_id);}

    public boolean getActiveLessonForStudent(Student s){return schoolsMap.get(s.get_schoolId()).CheckIfLessonActive(s.getStudentClassId());}

    public int getPhotoSampling(){return Config.getInstance().getOpenCV().getSamplingIntervalMS();}

    HashMap<String, SchoolServer> schoolsMap;

    private static EyeClassEngine eng;

}

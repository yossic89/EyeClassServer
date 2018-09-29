package Engine;

import Distractions.DistractionParam;
import Infra.Config;
import Infra.EyeBase;
import LessonManager.Lesson;
import LessonManager.QuestionStatisticForStudent;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Admin;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import SchoolEntity.UsersEntity.User;
import ViewModel.AdminDistractionParamViewModel;
import ViewModel.QuestionAnsViewModel;
import ViewModel.TeacherDistractionParamViewModel;

import javax.jdo.annotations.Transactional;
import javax.persistence.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBConnection extends EyeBase  {

    public static synchronized DBConnection GetInstance()
    {
        if (m_db == null)
        {
            m_db = new DBConnection();
            m_db.initDB();
        }
        return m_db;
    }

    private DBConnection()
    {
        dbOpen = false;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Close();
            }
        });
    }

    public void RestartDB()
    {
        String location = Config.getInstance().getDatabase().getDBPath();
        Close();
        try{
            if (Files.deleteIfExists(Paths.get(location)))
                Log("DB is deleted from " + location);
        }
        catch (Exception e) {}

        m_db.initDB();
    }

    public List<Object> query(String queryStr,Class obj)
    {
        Log("Query: "+ queryStr);
        TypedQuery<Object> queryL = em.createQuery(queryStr, obj);
        return queryL.getResultList();
    }

    public List<School> getAllSchool()
    {
        List<Object> list = query("SELECT s FROM School s", School.class);
        List<School> retVal = new ArrayList<>();
        for (Object obj : list)
            retVal.add((School) obj);
        return retVal;
    }

    public Lesson getLessonById(long id)
    {
        String queryStr = String.format("SELECT l FROM Lesson l WHERE l.id=%d", id);
        List<Object> obj = query(queryStr, Lesson.class);
        if (obj.size() == 0)
            return null;
        ArrayList<Lesson> retVal = new ArrayList<>();
        for (Object o : obj)
            retVal.add((Lesson)o);
        return retVal.get(0);
    }

    public ArrayList<Lesson> getLessonsForTeacher(long id)
    {
        String query = String.format("SELECT l FROM Lesson l WHERE l.m_teacher_id=%d", id);
        List<Object> list = query(query, Lesson.class);
        ArrayList<Lesson> retVal = new ArrayList<>();
        for (Object obj : list)
            retVal.add((Lesson)obj);
        return retVal;
    }

    public List<User> getAllUsersForSchool(String schoolName)
    {
        String query = String.format("SELECT p FROM User p WHERE p.m_schoolId=\"%s\"", schoolName);
        List<Object> list = query(query, User.class);
        List<User> retVal = new ArrayList<>();
        for (Object obj : list)
            retVal.add((User)obj);
        return retVal;
    }

    public List<SchoolEntity.Class> getAllClassesBySchool(String schoolName)
    {
        String query = String.format("SELECT p FROM Class p WHERE p.id LIKE \"%s%%\"", schoolName);
        List<Object> list = query(query, SchoolEntity.Class.class);
        List<SchoolEntity.Class> retVal = new ArrayList<>();
        for (Object obj : list)
            retVal.add((SchoolEntity.Class)obj);
        return retVal;

    }

    public List<AdminDistractionParamViewModel> getDistractionForAdmin(String schoolName)
    {
        String query = String.format(" SELECT d, l, s,c, t FROM Teacher t, Class c, Student s, DistractionParam d, Lesson l WHERE l.m_teacher_id=t.m_id AND l.id=d.lesson_id AND c.id=s.m_classId AND d.student_id=s.m_id AND s.m_schoolId=\"%s\"", schoolName);
        List<Object> list = query(query, Object[].class);
        List<Object[]> ll = new ArrayList<>();
        for (Object obj : list)
            ll.add((Object[])obj);
        List<AdminDistractionParamViewModel> retVal = new ArrayList<>();
        for(Object[] obj : ll)
        {
            DistractionParam d = (DistractionParam)obj[0];
            Lesson l = (Lesson)obj[1];
            Student s = (Student)obj[2];
            SchoolEntity.Class c = (SchoolEntity.Class)obj[3];
            Teacher t = (Teacher)obj[4];
            AdminDistractionParamViewModel viewModel = new AdminDistractionParamViewModel(Long.toString(s.getM_id()), s.getM_fullName(), c.GetClassName(), d.getDateAsStr(), l.get_curriculum().toString(),t.getM_fullName(), d.getDistrationType().toString(), d.getDurationAsStr());
            retVal.add(viewModel);
        }
        return retVal;
    }

    public List<QuestionAnsViewModel> getQuestionsAnsForTeacher(long teacher_id)
    {
        String query = String.format("SELECT s, l, q, c FROM Class c, Student s, Lesson l, QuestionStatisticForStudent q WHERE l.m_teacher_id=%d AND q.lessonId=l.id AND s.m_id=q.studId AND c.id=s.m_classId", teacher_id);
        List<Object> list = query(query, Object[].class);
        List<Object[]> ll = new ArrayList<>();
        for (Object obj : list)
            ll.add((Object[])obj);
        List<QuestionAnsViewModel> retVal = new ArrayList<>();
        for(Object[] obj : ll)
        {
            Student s = (Student)obj[0];
            Lesson l = (Lesson)obj[1];
            QuestionStatisticForStudent q = (QuestionStatisticForStudent)obj[2];
            SchoolEntity.Class c = (SchoolEntity.Class)obj[3];
            QuestionAnsViewModel viewModel = new QuestionAnsViewModel(s.getM_id(), s.getM_fullName(), c.GetClassName(), l.get_curriculum(), l.get_lessonHeadline(),q.getQuestion(), q.isGoodAnswer(), q.getStudAnswer());
            retVal.add(viewModel);
        }
        return retVal;
    }

    public List<TeacherDistractionParamViewModel> getDistractionForTeacher(long teacher_id)
    {
        String query = String.format(" SELECT d, l, s,c FROM Class c, Student s, DistractionParam d, Lesson l WHERE l.id=d.lesson_id AND c.id=s.m_classId AND d.student_id=s.m_id AND l.m_teacher_id=%d", teacher_id);
        List<Object> list = query(query, Object[].class);
        List<Object[]> ll = new ArrayList<>();
        for (Object obj : list)
            ll.add((Object[])obj);
        List<TeacherDistractionParamViewModel> retVal = new ArrayList<>();
        for(Object[] obj : ll)
        {
            DistractionParam d = (DistractionParam)obj[0];
            Lesson l = (Lesson)obj[1];
            Student s = (Student)obj[2];
            SchoolEntity.Class c = (SchoolEntity.Class)obj[3];
            TeacherDistractionParamViewModel viewModel = new TeacherDistractionParamViewModel(Long.toString(s.getM_id()), s.getM_fullName(), c.GetClassName(), d.getDateAsStr(), l.get_curriculum().toString(), d.getDistrationType().toString(), d.getDurationAsStr());
            retVal.add(viewModel);
        }
        return retVal;
    }

    public SchoolEntity.Class getClassByName(String name)
    {
        SchoolEntity.Class retVal = null;
        String query = String.format("SELECT c FROM Class c WHERE c.id=\"%s\"", name);
        List<Object> list = query(query, SchoolEntity.Class.class);
        if (list.size() > 0)
            retVal = (SchoolEntity.Class)list.get(0);
        return retVal;
    }

    public User getUser(long id, String pass)
    {
        User retVal = null;
        String query = String.format("SELECT p FROM User p WHERE p.m_id=%d AND p.m_password=\"%s\"", id, pass);
        List<Object> list = query(query, SchoolEntity.UsersEntity.User.class);
        if (list.size()==1)
            retVal = (User)list.get(0);
        return retVal;
    }

    public User getUser(long id)
    {
        User retVal = null;
        String query = String.format("SELECT p FROM User p WHERE p.m_id=%d", id);
        List<Object> list = query(query, SchoolEntity.UsersEntity.User.class);
        if (list.size()==1)
            retVal = (User)list.get(0);
        return retVal;
    }

    private void initDB()
    {
        emf = Persistence.createEntityManagerFactory(Config.getInstance().getDatabase().getDBPath());
        em = emf.createEntityManager();
        Object location = emf.getProperties().get("objectdb.connection.path");
        Log("DB location: " + location.toString());
        dbOpen = true;
    }

    public void Close()
    {
        if (dbOpen) {
            try
            {
                em.close();
                emf.close();
                dbOpen = false;
            }
            catch (Exception e)
            {
                System.out.println("FAILED TO SAVE");
                e.printStackTrace();
            }

        }
    }

    @Transactional
    public boolean Save(Object obj)
    {
        if (em.contains(obj))
            return false;
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(obj);
        tx.commit();
        //em.getTransaction().commit();
        return true;
    }

    private static DBConnection m_db = null;
    EntityManager em;
    EntityManagerFactory emf;
    private boolean dbOpen;

}

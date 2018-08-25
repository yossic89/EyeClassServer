package Engine;

import Infra.EyeBase;
import LessonManager.Lesson;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.User;

import javax.jdo.annotations.Transactional;
import javax.persistence.*;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Close();
            }
        });
    }
    public void RestartDB()
    {
        Object location = emf.getProperties().get("objectdb.connection.path");

        Close();
        try{
            if (Files.deleteIfExists(Paths.get(location.toString())))
                Log("DB is deleted from " + location.toString());
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

    public Lesson getLessonById(int id)
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
        emf = Persistence.createEntityManagerFactory("$objectdb/EyeClassDB/ECDB.odb");
        em = emf.createEntityManager();
        Object location = emf.getProperties().get("objectdb.connection.path");
        Log("DB location: " + location.toString());
    }

    private void Close()
    {
        //em.getTransaction().commit();
        em.close();
        emf.close();
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

}

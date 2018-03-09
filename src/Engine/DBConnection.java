package Engine;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBConnection {

    public static synchronized DBConnection GetInstance()
    {
        if (m_db == null)
        {
            m_db = new DBConnection();

        }
        return m_db;
    }

    private void initDB()
    {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("$objectdb/EyeClassDB/ECDB.odb");
    }

    private static DBConnection m_db = null;
}

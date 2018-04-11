package Controller;

import Common.Constans;
import Infra.EyeBase;
import SchoolEntity.UsersEntity.User;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

public class SessionUtils {

    public void AddUserToSession(ServletContext servletContext, User u)
    {
        long unique = System.currentTimeMillis();
        servletContext.setAttribute(Constans.UNIQUE_ID, unique);
        m_manager.addUser(unique, u);
    }

    public User GetUserFromSession(ServletContext servletContext)
    {
        User retVal = null;
        if (m_manager.checkIfSessionExists((long)servletContext.getAttribute(Constans.UNIQUE_ID)))
            retVal = m_manager.getUser((long)servletContext.getAttribute(Constans.UNIQUE_ID));
        return retVal;
    }



    public static SessionUtils GetInstance()
    {
        if (m_obj == null)
        {
            m_obj = new SessionUtils();
            m_obj.init();
        }

        return m_obj;
    }

    private void init()
    {
        if (m_manager == null)
            m_manager = new UsersManager();
    }

    private static SessionUtils m_obj = null;
    private UsersManager m_manager = null;

    private class UsersManager extends EyeBase
    {
        public UsersManager()
        {
            users_map = new HashMap<>();
        }

        public boolean checkIfSessionExists(long unique) { return users_map.containsKey(unique);}

        public void addUser(long unique, User u) {
            removePreviousUser(u.getM_id());
            Log(String.format("%d:%s logged in", u.getM_id(), u.getM_fullName()));
            users_map.put(unique, u);
        }

        private void removePreviousUser(long user_id)
        {
            long unique = -1;
            for(Map.Entry entry: users_map.entrySet())
            {
                if(((User)entry.getValue()).getM_id() == user_id)
                {
                    unique = (long)entry.getKey();
                    break;
                }
            }
            if (unique > 0)
                users_map.remove(unique);

        }

        public void deleteUser(long l) {users_map.remove(l);}


        public User getUser(long l){return users_map.get(l);}

        HashMap<Long, User> users_map;
    }
}

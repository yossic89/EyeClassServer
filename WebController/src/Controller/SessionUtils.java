package Controller;

import Common.Constans;
import Infra.EyeBase;
import SchoolEntity.UsersEntity.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionUtils {

    public void AddUserToSession(HttpServletRequest request, User u)
    {
        String unique = UUID.randomUUID().toString().replace("-", "");
        HttpSession session = request.getSession(true);
        if (session != null)
            session.setAttribute(Constans.UNIQUE_ID, unique);
        m_manager.addUser(unique, u);
    }

    public User GetUserFromSession(HttpServletRequest request)
    {
        User retVal = null;
        HttpSession session = request.getSession(false);
        if (m_manager.checkIfSessionExists((String)session.getAttribute(Constans.UNIQUE_ID)))
            retVal = m_manager.getUser((String)session.getAttribute(Constans.UNIQUE_ID));
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

        public boolean checkIfSessionExists(String unique) { return users_map.containsKey(unique);}

        public void addUser(String unique, User u) {
            removePreviousUser(u.getM_id());
            Log(String.format("%d:%s logged in", u.getM_id(), u.getM_fullName()));
            users_map.put(unique, u);
        }

        private void removePreviousUser(long user_id)
        {
            String unique = "";
            for(Map.Entry entry: users_map.entrySet())
            {
                if(((User)entry.getValue()).getM_id() == user_id)
                {
                    unique = (String)entry.getKey();
                    break;
                }
            }
            if (unique != "")
                users_map.remove(unique);

        }

        public void deleteUser(String id) {users_map.remove(id);}


        public User getUser(String id){return users_map.get(id);}

        HashMap<String, User> users_map;
    }
}

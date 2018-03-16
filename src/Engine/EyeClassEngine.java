package Engine;

import Infra.EyeBase;
import SchoolEntity.School;

import java.util.HashMap;

public class EyeClassEngine extends EyeBase {

    public EyeClassEngine()
    {
        schoolsMap = new HashMap<>();
    }

    public void initMap()
    {
        for (School s : DBConnection.GetInstance().getAllSchool())
        {
            Log("add " + s.GetName());
            schoolsMap.put(s.GetName(), new SchoolServer(s));
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
        System.out.println(ret + "!!!!!!!!!!");
       if (ret)
           schoolsMap.put(s.GetName(), new SchoolServer(s));
       return ret;
    }


    HashMap<String, SchoolServer> schoolsMap;

}

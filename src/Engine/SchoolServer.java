package Engine;

import Infra.EyeBase;
import SchoolEntity.School;

public class SchoolServer extends EyeBase {

    public SchoolServer(School school)
    {
        m_school = school;
    }

    private School m_school;
}

package SchoolEntity.UsersEntity;
import Infra.EyeBase;
import SchoolEntity.School;

import java.io.Serializable;

public abstract class User extends EyeBase implements Serializable {

    public User(long id, String password, School school, String fullName) {
        this.m_id = id;
        this.m_password = password;
        this.m_school = school;
        this.m_fullName = fullName;
    }

    public long getM_id() {
        return m_id;
    }

    public String getM_password() {
        return m_password;
    }

    public School getM_school() {
        return m_school;
    }

    public String getM_fullName() {
        return m_fullName;
    }

    protected long m_id;
    protected String m_password;
    protected School m_school;
    protected String m_fullName;
}

package SchoolEntity.UsersEntity;
import Infra.EyeBase;
import SchoolEntity.Class;
import SchoolEntity.School;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class User extends EyeBase implements Serializable {

    public User(long id, String password, String school, String fullName) {
        this.m_id = id;
        this.m_password = password;
        this.m_schoolId = school;
        this.m_fullName = fullName;
    }

    public long getM_id() {
        return m_id;
    }

    public String getM_password() {
        return m_password;
    }

    public String get_schoolId() { return m_schoolId; }

    public String getM_fullName() {
        return m_fullName;
    }

    @Id
    protected long m_id;
    protected String m_password;
    protected String m_fullName;
    protected String m_schoolId;
}

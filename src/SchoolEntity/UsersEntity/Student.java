package SchoolEntity.UsersEntity;

import SchoolEntity.School;

public class Student extends User {
    public Student(long id, String password, School school, String fullName, SchoolEntity.Class p_class) {
        super(id, password, school, fullName);
        m_class = p_class;
    }

    private SchoolEntity.Class m_class;
}

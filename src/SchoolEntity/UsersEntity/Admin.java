package SchoolEntity.UsersEntity;

import SchoolEntity.School;

public class Admin extends User {
    public Admin(long id, String password, School school, String fullName) {
        super(id, password, school, fullName);
    }
}

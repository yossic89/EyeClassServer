package SchoolEntity.UsersEntity;

import SchoolEntity.School;

import javax.persistence.Entity;

@Entity
public class Admin extends User {
    public Admin(long id, String password, String school, String fullName) {
        super(id, password, school, fullName);
    }
}

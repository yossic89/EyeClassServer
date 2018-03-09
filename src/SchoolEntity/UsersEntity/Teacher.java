package SchoolEntity.UsersEntity;

import Infra.CommonEnums;
import SchoolEntity.School;

import java.util.ArrayList;

public class Teacher extends User {
    public Teacher(long id, String password, School school, String fullName, ArrayList<CommonEnums.Curriculum> curriculum) {
        super(id, password, school, fullName);
        this.curriculum = curriculum;
    }

    private ArrayList<CommonEnums.Curriculum> curriculum;
}

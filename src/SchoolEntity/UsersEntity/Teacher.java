package SchoolEntity.UsersEntity;

import Infra.CommonEnums;
import SchoolEntity.School;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher extends User {
    public Teacher(long id, String password, String school, String fullName, List<CommonEnums.Curriculum> curriculum) {
        super(id, password, school, fullName);
        this.curriculum = curriculum;
    }

    public List<CommonEnums.Curriculum> getCurriculum() {
        return curriculum;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommonEnums.Curriculum> curriculum;
}

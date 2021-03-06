package LessonManager;

import Infra.CommonEnums;
import Infra.EyeBase;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Teacher;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lesson extends EyeBase implements Serializable {


    public Lesson(String filePath, List<MultipleQuestion> questions, long teacher_id, String lessonHeadline, CommonEnums.Curriculum curriculum) {
        this.m_filePath = filePath;
        this.m_questions = questions;
        this.m_teacher_id = teacher_id;
        this.m_lessonHeadline = lessonHeadline;
        this.m_curriculum = curriculum;

    }

    public String get_filePath() {
        return m_filePath;
    }


    public String get_lessonHeadline() {
        return m_lessonHeadline;
    }

    public CommonEnums.Curriculum get_curriculum() {
        return m_curriculum;
    }

    public List<MultipleQuestion> getM_questions(){
        return m_questions;
    }

    public long get_id(){return id;}

    @Id @GeneratedValue
    protected long id;
    String m_filePath;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<MultipleQuestion> m_questions;
    long m_teacher_id;
    String m_lessonHeadline;
    CommonEnums.Curriculum m_curriculum;
}

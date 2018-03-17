package LessonManager;

import Infra.CommonEnums;
import Infra.EyeBase;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Teacher;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class Lesson extends EyeBase implements Serializable {

    public Lesson(String filePath, ArrayList<MultipleQuestion> questions, long teacher_id, String lessonHeadline, CommonEnums.Curriculum curriculum) {
        this.m_filePath = filePath;
        this.m_questions = questions;
        this.m_teacher_id = teacher_id;
        this.m_lessonHeadline = lessonHeadline;
        this.m_curriculum = curriculum;

    }
    @Id @GeneratedValue
    private long id;
    String m_filePath;
    @ManyToOne(cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = MultipleQuestion.class)
    ArrayList<MultipleQuestion> m_questions;
    long m_teacher_id;
    String m_lessonHeadline;
    CommonEnums.Curriculum m_curriculum;
}

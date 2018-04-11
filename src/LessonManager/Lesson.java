package LessonManager;

import Infra.CommonEnums;
import Infra.EyeBase;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Teacher;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    public String get_filePath() {
        return m_filePath;
    }

    public ArrayList<MultipleQuestion> get_questions() {
        return m_questions;
    }

    public String get_lessonHeadline() {
        return m_lessonHeadline;
    }

    public CommonEnums.Curriculum get_curriculum() {
        return m_curriculum;
    }

    @Id @GeneratedValue
    private long id;
    String m_filePath;
    ArrayList<MultipleQuestion> m_questions;
    long m_teacher_id;
    String m_lessonHeadline;
    CommonEnums.Curriculum m_curriculum;
}

package LessonManager;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class QuestionStatisticForStudent {
    @Id
    @GeneratedValue
    protected long id;
    private long studId;
    private long questionId;
    private long lessonId;
    private long teacherId;
    private String question;
    private boolean isGoodAnswer;
    private String studAnswer;

    public QuestionStatisticForStudent(long studId, long lessonId,long teacherId, long questionId, String question, boolean isGoodAnswer, String studAnswer){
        this.studId = studId;
        this.lessonId = lessonId;
        this.teacherId = teacherId;
        this.questionId = questionId;
        this.question = question;
        this.isGoodAnswer = isGoodAnswer;
        this.studAnswer = studAnswer;
    }


}

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
    private String rightAnswer;

    public QuestionStatisticForStudent(long studId, long lessonId,long teacherId, long questionId, String question, boolean isGoodAnswer, String studAnswer, String rightAnswer){
        this.studId = studId;
        this.lessonId = lessonId;
        this.teacherId = teacherId;
        this.questionId = questionId;
        this.question = question;
        this.isGoodAnswer = isGoodAnswer;
        this.studAnswer = studAnswer;
        this.rightAnswer = rightAnswer;
    }


    public boolean isGoodAnswer() {
        return isGoodAnswer;
    }

    public String getStudAnswer() {
        return studAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

}

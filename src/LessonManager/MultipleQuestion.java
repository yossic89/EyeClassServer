package LessonManager;

import Infra.EyeBase;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class MultipleQuestion extends EyeBase implements Serializable {

    public MultipleQuestion(String q, String rightAns, String[] options,String topic, int time)
    {
        this.question = q;
        this.rightAns = rightAns;
        this.wrongOptions = options;
        this.topic = topic;
        this.time = time;
    }

    public String getRightAns(){return rightAns;}

    public void setTopic(String topic){this.topic =topic;}

    @Id @GeneratedValue
    private long id;
    String question;
    String rightAns;
    String[] wrongOptions;
    String topic;
    int time;

}

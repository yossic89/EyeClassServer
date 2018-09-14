package LessonManager;

import Infra.EyeBase;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class MultipleQuestion extends EyeBase implements Serializable {

    @Id @GeneratedValue
    private long id;
    String question;
    String rightAns;
    String[] allOptions;
    String topic;
    //TODO add
    String time;

    public MultipleQuestion(String q, String rightAns, String[] options,String topic)
    {
        this.question = q;
        this.rightAns = rightAns;
        this.allOptions = options;
        this.topic = topic;
    }

    public String getRightAns(){return rightAns;}

    public MultiQueData getQuestionWithAns()
    {
        return new MultiQueData(question, allOptions);
    }

    public void setTopic(String topic){this.topic =topic;}





    public class MultiQueData
    {
        public MultiQueData(String question, String[] options) {
            this.question = question;
            this.options = options;
        }

        public String getQuestion() {
            return question;
        }

        public String[] getOptions() {
            return options;
        }

        String question;
        String[] options;
    }

}

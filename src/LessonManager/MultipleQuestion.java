package LessonManager;

import Infra.EyeBase;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class MultipleQuestion extends EyeBase implements Serializable {

    public MultipleQuestion(String q, String rightAns, String[] options)
    {
        this.question = q;
        this.rightAns = rightAns;
        this.allOptions = options;
        this.topic = "THIS IS TOPIC!NEED TO CHANGE";
    }

    public String getRightAns(){return rightAns;}

    public MultiQueData getQuestionWithAns()
    {
        return new MultiQueData(question, allOptions);
    }

    public void setTopic(int i){this.topic = "THIS IS TOPIC!NEED TO CHANGE: "+i;}


    @Id @GeneratedValue
    private long id;
    String question;
    String rightAns;
    String[] allOptions;
    String topic;


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

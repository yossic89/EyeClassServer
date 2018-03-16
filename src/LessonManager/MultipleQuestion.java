package LessonManager;

import Infra.EyeBase;
import javafx.util.Pair;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Tuple;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class MultipleQuestion extends EyeBase implements Serializable {

    public MultipleQuestion(String q, String rightAns, String[] options)
    {
        this.question = q;
        this.rightAns = rightAns;
        this.allOptions = options;
    }

    public String getRightAns(){return rightAns;}

    public Pair<String, String[]> getQuestionWithAns()
    {
        return new Pair<>(question, allOptions);
    }


    @Id @GeneratedValue
    private long id;
    String question;
    String[] allOptions;
    String rightAns;
}

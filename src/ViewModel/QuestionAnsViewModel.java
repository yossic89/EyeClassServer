package ViewModel;

import Infra.CommonEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionAnsViewModel implements IViewModel {

    private long student_id;
    private String student_name;
    private String _class;
    private CommonEnums.Curriculum curriculum;
    private String lesson;
    private String question;
    private String rightAns;
    private boolean isRight;
    private String answer;

    public QuestionAnsViewModel(long student_id, String student_name, String _class, CommonEnums.Curriculum curriculum, String lesson,String question, boolean isRight, String answer, String rightAns) {
        this.student_id = student_id;
        this.student_name = student_name;
        this._class = _class;
        this.curriculum = curriculum;
        this.lesson = lesson;
        this.isRight = isRight;
        this.answer = answer;
        this.question = question;
        this.rightAns = rightAns;
    }

    @Override
    public List<String> getAsList() {
        return new ArrayList<String>(Arrays.asList(Long.toString(student_id), student_name, _class, curriculum.toString(),lesson, question,rightAns, answer, Boolean.toString(isRight)));
    }
}

package ViewModel;

import Infra.CommonEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionAnsViewModel implements IViewModel {

    private long id;
    private String name;
    private String _class;
    private CommonEnums.Curriculum curriculum;
    private String lesson;
    private boolean isRight;
    private String answer;

    @Override
    public List<String> getAsList() {
        return new ArrayList<String>(Arrays.asList(Long.toString(id), name, _class, curriculum.toString(), lesson, Boolean.toString(isRight), answer));
    }
}

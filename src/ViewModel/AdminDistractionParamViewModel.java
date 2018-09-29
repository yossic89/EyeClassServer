package ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminDistractionParamViewModel extends TeacherDistractionParamViewModel {
    private String teacher;

    public AdminDistractionParamViewModel(String id, String name, String _class, String date, String curriculum, String teacher, String type, String duration) {
        super(id, name, _class, date, curriculum, type, duration);
        this.teacher = teacher;
    }

    @Override
    public List<String> getAsList(){return new ArrayList<String>(Arrays.asList(id, name, _class, date, curriculum,teacher, type, duration));}
}

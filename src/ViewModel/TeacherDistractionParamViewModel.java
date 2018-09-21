package ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherDistractionParamViewModel implements IViewModel {
    protected String id;
    protected String name;
    protected String _class;
    protected String date;
    protected String curriculum;
    protected String type;
    protected String duration;

    public TeacherDistractionParamViewModel(String id, String name, String _class, String date, String curriculum, String type, String duration) {
        this.id = id;
        this.name = name;
        this._class = _class;
        this.date = date;
        this.curriculum = curriculum;
        this.type = type;
        this.duration = duration;
    }

    @Override
    public List<String> getAsList(){return new ArrayList<String>(Arrays.asList(id, name, _class, date, curriculum, type, duration));}
}

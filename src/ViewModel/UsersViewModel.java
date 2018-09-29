package ViewModel;

import Infra.CommonEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UsersViewModel implements IViewModel {

    private long id;
    private String name;
    private CommonEnums.UserTypes type;
    private List<CommonEnums.Curriculum> curriculumList;
    private String _class;

    //for student
    public UsersViewModel(long id, String name, CommonEnums.UserTypes type, String _class) {
        this.id = id;
        this.name = name;
        this.type = type;
        this._class = _class;
        curriculumList = new ArrayList<>();
    }

    //for teacher
    public UsersViewModel(long id, String name, CommonEnums.UserTypes type, List<CommonEnums.Curriculum> curriculumList) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.curriculumList = curriculumList;
        this._class = " ";
    }

    //for admin
    public UsersViewModel(long id, String name, CommonEnums.UserTypes type) {
        this.id = id;
        this.name = name;
        this.type = type;
        curriculumList = new ArrayList<>();
        this._class = " ";
    }

    private String curriculumToStr()
    {
        if (curriculumList.size() == 0)
            return " ";
        else
            return curriculumList.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    @Override
    public List<String> getAsList() {return new ArrayList<String>(Arrays.asList(Long.toString(id), name, type.toString(), curriculumToStr(), _class));}
}

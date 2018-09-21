package Distractions;

import Infra.CommonEnums;
import Infra.EyeBase;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
public class DistractionParam extends EyeBase implements Serializable {


    public DistractionParam(CommonEnums.DistractionType type, long student, long lesson){
        this.type = type;
        isActive = true;
        end = new Date(System.currentTimeMillis());
        start = end;
        student_id = student;
        lesson_id = lesson;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void updateEndTime(){
        end = new Date(System.currentTimeMillis());
    }

    public CommonEnums.DistractionType getType() {
        return type;
    }

    public int getDistractionDuration(){
        long d1 = end.getTime();
        long d2 = start.getTime();
        int duration = (int)(d1-d2)/1000;
        return duration;
    }

    public String getDateAsStr()
    {
        DateFormat df = new SimpleDateFormat("MM/dd/YY");
        return df.format(start);
    }

    public String getDurationAsStr()
    {
        long d1 = end.getTime();
        long d2 = start.getTime();
        int duration = (int)(d1-d2);
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(duration);
    }

    public CommonEnums.DistractionType getDistrationType(){return this.type;}

    @Id @GeneratedValue
    protected long id;
    private CommonEnums.DistractionType type;
    private Date start;
    private Date end;
    private boolean isActive;
    private long student_id;
    private long lesson_id;

    public static class DistractionParamViewModel{

        private String id;
        private String name;
        private String _class;
        private String date;
        private String curriculum;
        private String type;
        private String duration;

        public DistractionParamViewModel(String id, String name, String _class, String date, String curriculum, String type, String duration) {
            this.id = id;
            this.name = name;
            this._class = _class;
            this.date = date;
            this.curriculum = curriculum;
            this.type = type;
            this.duration = duration;
        }

        public List<String> getAsList(){return new ArrayList<String>(Arrays.asList(id, name, _class, date, curriculum, type, duration));}
    }
}



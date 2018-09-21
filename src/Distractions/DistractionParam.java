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
        int duration = getDistractionDuration();
        int minutes = duration / 60;
        int seconds = duration % 60;
        String retVal = "";
        if (minutes > 0)
            retVal = String.format("%d min %d sec", minutes, seconds);
        else
            retVal = String.format("%d sec", seconds);
        return  retVal;
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
}



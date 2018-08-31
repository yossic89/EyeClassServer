package Distractions;

import Infra.CommonEnums;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class DistractionParam implements Serializable {


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

    @Id @GeneratedValue
    private String id;
    private CommonEnums.DistractionType type;
    private Date start;
    private Date end;
    private boolean isActive;
    private long student_id;
    private long lesson_id;
}



package Distractions;

import Infra.CommonEnums;

import java.util.Date;

public class DistractionParam {
    private CommonEnums.DistractionType type;
    private Date start;
    private Date end;
    private boolean isActive;

    public DistractionParam(CommonEnums.DistractionType type){
        this.type = type;
        isActive = true;
        end = new Date(System.currentTimeMillis());
        start = end;
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
}



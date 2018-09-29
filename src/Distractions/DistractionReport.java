package Distractions;

import Engine.DBConnection;
import Infra.CommonEnums;
import Infra.Config;
import Infra.EyeBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DistractionReport extends EyeBase {

    public DistractionReport(long lesson_id)
    {
        this.lesson_id = lesson_id;
        studentsDistractions = new HashMap<>();
    }

    public void handleMeasure(MeasureParams measure, int teacherPage)
    {
        //check if meassure if eyes=2 && student page == teacher page
        CommonEnums.DistractionType type = checkMeasure(measure, teacherPage);
        if (type != CommonEnums.DistractionType.None)
            addDistraction(type, measure.getStudent_id());
        else
            handleLastDistraction(measure.getStudent_id());
        Log(String.format("handleMeasure for student id [%d], eyes count [%d], student page [%d]. teacher page [%d], STATUS: [%s]"
        , measure.getStudent_id(), measure.getEyes_count(), measure.getPage(), teacherPage, type.toString()));
        //show image if has one
        if (measure.getPhoto_arr() != null)
            ImageViewer.showImage(measure.getPhoto_arr());
    }

    private void handleLastDistraction(long student_id)
    {
        if (studentsDistractions.containsKey(student_id))
        {
            DistractionParam students_district = studentsDistractions.get(student_id).get(studentsDistractions.get(student_id).size() - 1);
            //this distraction is not active any more
            students_district.setActive(false);
            //remove if this districtaion is less than the minimum from config
            if (students_district.getDistractionDuration() < Config.getInstance().getOpenCV().getMinimumTimeForDistractionReportSec())
                studentsDistractions.get(student_id).remove(students_district);
        }
    }

    private void addDistraction(CommonEnums.DistractionType type, long student_id)
    {
        //first time for student distration
        if (!studentsDistractions.containsKey(student_id))
        {
            studentsDistractions.put(student_id, new ArrayList<>());
            studentsDistractions.get(student_id).add(new DistractionParam(type, student_id, lesson_id));
        }
        else
        {
            DistractionParam students_district = studentsDistractions.get(student_id).get(studentsDistractions.get(student_id).size() - 1);
            //if there is active distraction
            if (students_district.isActive())
            {
                //increse time
                if (type == students_district.getType())
                    students_district.updateEndTime();
                //close the last districation and open new one
                else
                {
                    handleLastDistraction(student_id);
                    studentsDistractions.get(student_id).add(new DistractionParam(type, student_id, lesson_id));
                }
            }
            //add new one
            else
                studentsDistractions.get(student_id).add(new DistractionParam(type, student_id, lesson_id));
        }
    }

    private CommonEnums.DistractionType checkMeasure(MeasureParams measure, int teacherPage)
    {
        if (measure.getEyes_count() != 2 && measure.getPage() != teacherPage)
            return CommonEnums.DistractionType.All;
        else if (measure.getPage() != teacherPage)
            return CommonEnums.DistractionType.Page;
        else if (measure.getEyes_count() != 2)
            return CommonEnums.DistractionType.Looking;
        else
            return CommonEnums.DistractionType.None;
    }

    public CommonEnums.DistractionType getStudentStatus(long id)
    {
        CommonEnums.DistractionType retVal = CommonEnums.DistractionType.None;
        if (studentsDistractions.containsKey(id) )
        {
            //validate
            DistractionParam param = studentsDistractions.get(id).get(studentsDistractions.get(id).size() - 1);
            if (param.isActive() && param.getDistractionDuration() >= Config.getInstance().getOpenCV().getMinimumTimeForDistractionReportSec())
                retVal = param.getType();
        }
        return retVal;
    }

    public void closeAllDistrations()
    {
        for(long id : studentsDistractions.keySet())
            handleLastDistraction(id);
    }

    public void closeLesson()
    {
        //handle last districation
        closeAllDistrations();
        //save in DB
        saveDistractions();
    }

    private void saveDistractions()
    {
        for (Map.Entry<Long, List<DistractionParam>> iter : studentsDistractions.entrySet())
        {
            for(DistractionParam param : iter.getValue())
            {
                if (!DBConnection.GetInstance().Save(param))
                    Log(String.format("Failed to update DistractionParam for id [%d]", iter.getKey()));
            }
        }

    }

    private long lesson_id;
    private String school_id;
    private long teacher_id;
    Map<Long, List<DistractionParam>> studentsDistractions;
}

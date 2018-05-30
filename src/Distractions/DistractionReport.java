package Distractions;

import Infra.CommonEnums;
import Infra.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistractionReport {

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

        //show image if has one
        if (measure.getPhoto_arr() != null)
            ImageViewer.showImage(measure.getPhoto_arr());
    }

    private void handleLastDistraction(long student_id)
    {
        if (studentsDistractions.containsKey(student_id))
        {
            DistractionParam students_district = studentsDistractions.get(student_id).get(studentsDistractions.get(student_id).size() - 1);
            if (students_district.getDistractionDuration() < Config.getInstance().getOpenCV().getMinimumTimeForDistractionReportSec())
                studentsDistractions.get(student_id).remove(students_district);
        }
    }

    private void addDistraction(CommonEnums.DistractionType type, long student_id)
    {
        if (!studentsDistractions.containsKey(student_id))
        {
            studentsDistractions.put(student_id, new ArrayList<>());
            studentsDistractions.get(student_id).add(new DistractionParam(type));
        }
        else
        {
            DistractionParam students_district = studentsDistractions.get(student_id).get(studentsDistractions.get(student_id).size() - 1);
            //if there is active distraction

            if (students_district.isActive())
            {
                if (type == students_district.getType())
                    students_district.updateEndTime();
                else
                {
                    students_district.setActive(false);
                    //handleLastDistraction(student_id);//keep it - for intergration
                    studentsDistractions.get(student_id).add(new DistractionParam(type));
                }
            }
            //add new one
            else
                studentsDistractions.get(student_id).add(new DistractionParam(type));
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

    private long lesson_id;
    Map<Long, List<DistractionParam>> studentsDistractions;
}

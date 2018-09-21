package LessonManager;

import Distractions.DistractionReport;
import Distractions.MeasureParams;
import Infra.CommonEnums;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveLesson extends Lesson {

    public ActiveLesson(Lesson l, List<Long> students)
    {
        super(l.m_filePath, l.m_questions, l.m_teacher_id, l.m_lessonHeadline, l.m_curriculum);
        collectData = true;
        id = l.id;
        m_teacherPage = 0;
        m_distraction = new DistractionReport(id);
        m_questions = l.m_questions;
        initLesson(students);
    }

    private void initLesson(List<Long> students)
    {
        m_studentsStatus = new HashMap<>();
        for (long id : students)
            m_studentsStatus.put(id, CommonEnums.StudentConcentratedStatus.Unknown);
    }

    public byte[] getPdfAsBytes()
    {
        try {
           return Files.readAllBytes(Paths.get(m_filePath));
        } catch (IOException e) {
            Log("Failed to open lesson pdf" + e);
        }
        return null;
    }

    public void handleMeasureParam(MeasureParams measure)
    {
        if (!collectData)
            return;
        m_distraction.handleMeasure(measure, m_teacherPage);

        //update map
        if (!m_studentsStatus.containsKey(measure.getStudent_id()))
            Log("Unable to find studnet id " + measure.getStudent_id() +" for lesson id: " + id);
        else
        {
            CommonEnums.DistractionType type = m_distraction.getStudentStatus(measure.getStudent_id());
            if (type == CommonEnums.DistractionType.None)
                m_studentsStatus.put(measure.getStudent_id(), CommonEnums.StudentConcentratedStatus.Concentrated);
            else
                m_studentsStatus.put(measure.getStudent_id(), CommonEnums.StudentConcentratedStatus.NotConcentrated);
        }

    }

    private void studentStatusDebug()
    {
        for (Map.Entry<Long, CommonEnums.StudentConcentratedStatus> iter : m_studentsStatus.entrySet())
            Log(String.format("id [%d] status [%s]", iter.getKey(), iter.getValue().toString()));
    }

    public Map<Long, CommonEnums.StudentConcentratedStatus> getStudentsStatus() {
        studentStatusDebug();
        return m_studentsStatus;
    }

    public ArrayList<MultipleQuestion> get_questions() {
        return m_questions;
    }

    public void setTeacherPage(int teacherPage) {
        this.m_teacherPage = teacherPage;
        Log(String.format("Active Lesson [%s] teacher page set to [%d]", m_lessonHeadline, this.m_teacherPage));
    }

    public void setCollectData(boolean toCollect)
    {
        collectData = toCollect;
        Log(String.format("Collect data to lesson [%s] is set to [%s]", m_lessonHeadline, collectData));
        //restart status to unkown and close open
        if (!collectData)
        {
            //close opens
            m_distraction.closeAllDistrations();

            //update all to unkown
            for(long id : m_studentsStatus.keySet())
            {
                m_studentsStatus.put(id, CommonEnums.StudentConcentratedStatus.Unknown);
            }

        }
    }

    public String getQuestionData() {
        return questionData;
    }

    public long getTeacherId(){return  m_teacher_id;}

    public long getLessonId(){return id;}

    public void setQuestionDataForDeliver(String questionData) {
        this.questionData = questionData;
    }
    private String questionData;

    public void endLesson()
    {
        m_distraction.closeLesson();
    }

    public int getTeacherPage(){return m_teacherPage;}
  
    int m_teacherPage;
    Map<Long, CommonEnums.StudentConcentratedStatus> m_studentsStatus;
    DistractionReport m_distraction;
    ArrayList<MultipleQuestion> m_questions;
    boolean collectData;
}

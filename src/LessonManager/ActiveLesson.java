package LessonManager;

import Distractions.DistractionReport;
import Distractions.MeasureParams;
import Infra.CommonEnums;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveLesson extends Lesson {

    public ActiveLesson(Lesson l, List<Long> students)
    {
        super(l.m_filePath, l.m_questions, l.m_teacher_id, l.m_lessonHeadline, l.m_curriculum);
        m_teacherPage = 0;
        m_distraction = new DistractionReport(id);
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

    public Map<Long, CommonEnums.StudentConcentratedStatus> getStudentsStatus() { return m_studentsStatus;}

    public int getTeacherPage() {
        return m_teacherPage;
    }

    public void setTeacherPage(int teacherPage) {
        this.m_teacherPage = teacherPage;
        Log(String.format("Active Lesson [%s] teacher page set to [%d]", m_lessonHeadline, this.m_teacherPage));
    }

    int m_teacherPage;
    Map<Long, CommonEnums.StudentConcentratedStatus> m_studentsStatus;
    DistractionReport m_distraction;
}

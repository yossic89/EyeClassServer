package LessonManager;

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

    public Map<Long, CommonEnums.StudentConcentratedStatus> getStudentsStatus() { return m_studentsStatus;}

    public int getTeacherPage() {
        return m_teacherPage;
    }

    public void setTeacherPage(int teacherPage) {
        this.m_teacherPage = teacherPage;
    }

    int m_teacherPage;
    Map<Long, CommonEnums.StudentConcentratedStatus> m_studentsStatus;
}

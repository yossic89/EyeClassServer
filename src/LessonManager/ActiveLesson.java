package LessonManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ActiveLesson extends Lesson {

    public ActiveLesson(Lesson l)
    {
        super(l.m_filePath, l.m_questions, l.m_teacher_id, l.m_lessonHeadline, l.m_curriculum);
        m_teacherPage = 0;
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

    public int getTeacherPage() {
        return m_teacherPage;
    }

    public void setTeacherPage(int teacherPage) {
        this.m_teacherPage = teacherPage;
    }

    int m_teacherPage;
}

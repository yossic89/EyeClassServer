package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import SchoolEntity.UsersEntity.Student;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class StudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch(req.getParameter(Constans.REQUEST)){
            case Constans.ACTIVE_LESSON:
                isActiveLesson(resp);
                break;
            case Constans.DISPLAY_PDF:
                displayPDF(resp);
                break;
            case Constans.FINISH_LESSON:
                break;
        }
    }

    private void isActiveLesson(HttpServletResponse resp) throws IOException, ServletException
    {
        Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(getServletContext());
        boolean isActive = EyeClassEngine.GetInstance().getActiveLessonForStudent(s);
        PrintWriter out = resp.getWriter();
        out.print(isActive);
    }

    private void displayPDF(HttpServletResponse resp) throws IOException, ServletException
    {
        Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(getServletContext());
        byte[] pdf_arr = EyeClassEngine.GetInstance().getLessonPlanDataForClass(s, s.getStudentClassId());
        resp.setContentType("application/pdf");
        resp.setHeader("Content-disposition","inline; filename='lesson.pdf'");
        resp.setContentLength(pdf_arr.length);
        OutputStream out = resp.getOutputStream();
        out.write(pdf_arr);
        out.close();
    }

    private void isLessonFinished(HttpServletResponse resp) throws IOException, ServletException
    {
        Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(getServletContext());
        boolean isFinish = !EyeClassEngine.GetInstance().getActiveLessonForStudent(s);
        PrintWriter out = resp.getWriter();
        out.print(isFinish);
    }
}

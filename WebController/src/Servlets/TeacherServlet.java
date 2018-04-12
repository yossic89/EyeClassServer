package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import Infra.CommonEnums;
import SchoolEntity.UsersEntity.Teacher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TeacherServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch(req.getParameter(Constans.TEACHER_REQ)){
            case Constans.DEMO_LESSON: doDemoLesson(); break;
            case Constans.DISPLAY_PDF: displayPDF(resp); break;
        }
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ArrayList<CommonEnums.Curriculum> a = new ArrayList<>();
        Teacher t = new Teacher(123456789,"12345", "ORT Eilat", "Test me please", a);
        EyeClassEngine.GetInstance().StartLesson(t, 1, "ORT Eilat_Grade 11_1");
        byte[] b = EyeClassEngine.GetInstance().getLessonPlanDataForClass(t, "ORT Eilat_Grade 11_1");
        resp.setContentType("application/pdf");
        resp.setHeader("Content-disposition","inline; filename='test.pdf'");
        resp.setContentLength(b.length);
        OutputStream out = resp.getOutputStream();

        out.write(b);
        out.close();
    }
    private void doDemoLesson() throws IOException, ServletException {
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(getServletContext());
        EyeClassEngine.GetInstance().StartLesson(t, 1, "ORT Eilat_Grade11_1");


    }

    private void displayPDF(HttpServletResponse resp) throws IOException, ServletException {
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(getServletContext());
        byte[] pdf_arr = EyeClassEngine.GetInstance().getLessonPlanDataForClass(t, "ORT Eilat_Grade11_1");
        resp.setContentType("application/pdf");
        resp.setHeader("Content-disposition","inline; filename='lesson.pdf'");
        resp.setContentLength(pdf_arr.length);
        OutputStream out = resp.getOutputStream();
        out.write(pdf_arr);
        out.close();

    }
}

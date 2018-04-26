package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import Infra.CommonEnums;
import Infra.Config;
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

        switch(req.getParameter(Constans.REQUEST)){
            case Constans.DEMO_LESSON: doDemoLesson(req); break;
            case Constans.DISPLAY_PDF: displayPDF(req, resp); break;
        }
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ArrayList<CommonEnums.Curriculum> a = new ArrayList<>();
        Teacher t = new Teacher(111111111,"12345", "ORT Eilat", "Test me please", a);
        EyeClassEngine.GetInstance().StartLesson(t, 1, "ORT Eilat_Grade11_1");
    }
    private void doDemoLesson(HttpServletRequest req) throws IOException, ServletException {
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        EyeClassEngine.GetInstance().StartLesson(t, 1, "ORT Eilat_Grade11_1");
    }

    private void displayPDF(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        String class_id = req.getParameter(Constans.CLASS_ID);
        byte[] pdf_arr = EyeClassEngine.GetInstance().getLessonPlanDataForClass(t, class_id);
        resp.setContentType("application/pdf");
        resp.setHeader("Content-disposition","inline; filename='lesson.pdf'");
        resp.setContentLength(pdf_arr.length);
        OutputStream out = resp.getOutputStream();
        out.write(pdf_arr);
        out.close();
    }
}

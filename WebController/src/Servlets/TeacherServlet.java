package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import SchoolEntity.UsersEntity.Teacher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TeacherServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch(req.getParameter(Constans.TEACHER_REQ)){
            case Constans.DEMO_LESSON: doDemoLesson();
            case Constans.DISPLAY_PDF: displayPDF(resp);
        }
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        out.print("Hi, I'm a Teacher!");
    }
    private void doDemoLesson() throws IOException, ServletException {
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(getServletContext());
        EyeClassEngine.GetInstance().StartLesson(t, 1, "ORT Eilat_Grade 11_1");


    }

    private void displayPDF(HttpServletResponse resp) throws IOException, ServletException {
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(getServletContext());
        PrintWriter out = resp.getWriter();
        out.print(EyeClassEngine.GetInstance().getLessonPlanDataForClass(t, "ORT Eilat_Grade 11_1"));
    }
}

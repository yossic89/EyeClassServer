package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import Infra.Config;
import SchoolEntity.UsersEntity.Student;
import com.google.gson.Gson;

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
                isActiveLesson(req, resp);
                break;
            case Constans.DISPLAY_PDF:
                displayPDF(req, resp);
                break;
            case Constans.FINISH_LESSON:
                break;
            case Constans.STUDENT_PARAMETERS:
                sendParameters(req, resp);
                break;
        }
    }

    private void isActiveLesson(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(req);
        boolean isActive = EyeClassEngine.GetInstance().getActiveLessonForStudent(s);
        PrintWriter out = resp.getWriter();
        out.print(isActive);
    }

    private void displayPDF(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(req);
        byte[] pdf_arr = EyeClassEngine.GetInstance().getLessonPlanDataForClass(s, s.getStudentClassId());
        resp.setContentType("application/pdf");
        resp.setHeader("Content-disposition","inline; filename='lesson.pdf'");
        resp.setContentLength(pdf_arr.length);
        OutputStream out = resp.getOutputStream();
        out.write(pdf_arr);
        out.close();
    }

    private void isLessonFinished(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(req);
        boolean isFinish = !EyeClassEngine.GetInstance().getActiveLessonForStudent(s);
        PrintWriter out = resp.getWriter();
        out.print(isFinish);
    }

    private void sendParameters(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(req);
        boolean ifSend = Config.getInstance().getOpenCV().getDebugByID() == s.getM_id();
        ParamObj ret = new ParamObj(Config.getInstance().getOpenCV().getSamplingIntervalMS(), ifSend);
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(ret));
    }

    public class ParamObj
    {
        public ParamObj(int sample, boolean ifSend)
        {
            this.ifSendPhoto = ifSend;
            this.photoSampling = sample;
        }

        private int photoSampling;
        private boolean ifSendPhoto;
    }
}

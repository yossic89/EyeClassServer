package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Distractions.ImageViewer;
import Distractions.MeasureParams;
import Engine.EyeClassEngine;
import Infra.Config;
import SchoolEntity.UsersEntity.Student;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
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
                isLessonFinished(req, resp);
                break;
            case Constans.STUDENT_PARAMETERS:
                sendParameters(req, resp);
                break;
            case Constans.MEASURE:
                getMeasureData(req, resp);
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

    private void getMeasureData(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(req);
        Gson g = new Gson();
        deviationData d = g.fromJson(req.getParameter("data"), deviationData.class);
        MeasureParams param = new MeasureParams(s.getM_id(), d.getPage_num(), d.getEyes_count(), d.getPhoto());
        EyeClassEngine.GetInstance().handleStudentMeasuring(s, param);
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

    public class deviationData
    {
        public deviationData(int _eyes, byte[] _photo)
        {
            eyes_count = _eyes;
            photo = _photo;
        }

        public void setPage_num(int page_num) {
            this.page_num = page_num;
        }

        public byte[] getPhoto() {
            return photo;
        }

        public int getPage_num() {
            return page_num;
        }

        public int getEyes_count() {
            return eyes_count;
        }

        byte[] photo;
        int page_num;
        int eyes_count;

    }
}

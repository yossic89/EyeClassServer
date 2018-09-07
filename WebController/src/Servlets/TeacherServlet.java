package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import Infra.CommonEnums;
import Infra.Config;
import LessonManager.MultipleQuestion;
import SchoolEntity.UsersEntity.Teacher;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch (req.getParameter(Constans.REQUEST)) {
            case Constans.DEMO_LESSON:
                doDemoLesson(req);
                break;
            case Constans.DISPLAY_PDF:
                displayPDF(req, resp);
                break;
            case Constans.STUDENTS_STATUS:
                studentsStatus(req, resp);
                break;
            case Constans.PAGE_UPDATE:
                setTeacherPage(req);
                break;
            case Constans.QUESTIONS_LESSON:
                questionLesson(req, resp);
                break;
            case Constans.END_LESSON:
                endLesson(req);
                break;
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String param = req.getParameter("status");
        if (param.contains("start"))
        {
            System.out.println("DEBUG MODE - Start");
            ArrayList<CommonEnums.Curriculum> a = new ArrayList<>();
            Teacher t = new Teacher(111111111,"12345", "ORT Eilat", "Test me please", a);
            EyeClassEngine.GetInstance().StartLesson(t, 5, "ORT Eilat_Grade11_1");
        }

        if (param.contains("end"))
        {
            System.out.println("DEBUG MODE - End");
            ArrayList<CommonEnums.Curriculum> a = new ArrayList<>();
            Teacher t = new Teacher(111111111,"12345", "ORT Eilat", "Test me please", a);
            EyeClassEngine.GetInstance().endLesson(t, "ORT Eilat_Grade11_1");
        }
    }

    private void endLesson(HttpServletRequest req) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        String class_id = req.getParameter(Constans.CLASS_ID);
        EyeClassEngine.GetInstance().endLesson(t, class_id);
    }

    private void setTeacherPage(HttpServletRequest req) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        String class_id = req.getParameter(Constans.CLASS_ID);
        int page = Integer.parseInt(req.getParameter("page"));
        EyeClassEngine.GetInstance().setTeacherPageForLesson(t, class_id, page);
    }

    private void doDemoLesson(HttpServletRequest req) throws IOException, ServletException {
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        EyeClassEngine.GetInstance().StartLesson(t, 5, "ORT Eilat_Grade11_1");
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

    private void studentsStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        String class_id = req.getParameter(Constans.CLASS_ID);
        Map<String, CommonEnums.StudentConcentratedStatus> data = EyeClassEngine.GetInstance().getStudentsLessonStatus(t, class_id);
        Map<String, Integer> retVal = new HashMap<>();
        for (Map.Entry<String, CommonEnums.StudentConcentratedStatus> entry : data.entrySet())
        {
            retVal.put(entry.getKey(), entry.getValue().getValue());
        }
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(data));
        out.close();
    }

    private void questionLesson(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        String class_id = req.getParameter(Constans.CLASS_ID);
        ArrayList<MultipleQuestion> data = EyeClassEngine.GetInstance().getQuestionsForClass(t, class_id);
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(data));
        out.close();
    }
}

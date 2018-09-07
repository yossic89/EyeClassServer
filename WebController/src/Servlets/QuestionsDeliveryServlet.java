package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import Infra.CommonEnums;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//@WebServlet(name = "QuestionsDeliveryServlet", urlPatterns = {"/questionsDeliveryServlet"})
public class QuestionsDeliveryServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        deliverQuestionsToStudents(req, resp);
    }

    private void deliverQuestionsToStudents(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("$$$$ " + req.getParameter("action"));

        if (req.getParameter("action").equals("send")) {
            String class_id = req.getParameter(Constans.CLASS_ID);
            String questionData = req.getParameter("questionData");
            Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
            EyeClassEngine.GetInstance().setQuestionDataForDelivery(t, class_id, questionData);
        }
        else if (req.getParameter("action").equals("get")){
            Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(req);
            String questionData = EyeClassEngine.GetInstance().getQuestionDataOfActiveLesson(s);
            PrintWriter out = resp.getWriter();
            out.print(questionData);
        }
        if (req.getParameter("action").equals("clear")) {
            String class_id = req.getParameter(Constans.CLASS_ID);
            String questionData = null;
            Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
            EyeClassEngine.GetInstance().setQuestionDataForDelivery(t, class_id, questionData);
        }
    }
}


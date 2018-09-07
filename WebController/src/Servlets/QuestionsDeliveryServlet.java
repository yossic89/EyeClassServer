package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.DBConnection;
import Engine.EyeClassEngine;
import Infra.CommonEnums;
import LessonManager.QuestionStatisticForStudent;
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
        switch (req.getParameter("action")) {
            case "send":
                doSendAction(req);
                break;
            case "get":
               doGetAction(req,resp);
                break;
            case "clear":
                doClearAction(req);
                break;
            case "save":
                doSaveAction(req);
                break;
        }
    }

    private void doSendAction(HttpServletRequest req){
        String class_id = req.getParameter(Constans.CLASS_ID);
        String questionData = req.getParameter("questionData");
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        EyeClassEngine.GetInstance().setQuestionDataForDelivery(t, class_id, questionData);
    }

    private void doGetAction(HttpServletRequest req, HttpServletResponse resp){
        Student s = (Student)SessionUtils.GetInstance().GetUserFromSession(req);
        String questionData = null;
        try{questionData = EyeClassEngine.GetInstance().getQuestionDataOfActiveLesson(s);}
        catch (Exception e){}
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.print(questionData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doClearAction(HttpServletRequest req){
        String class_id = req.getParameter(Constans.CLASS_ID);
        String questionData = null;
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        EyeClassEngine.GetInstance().setQuestionDataForDelivery(t, class_id, questionData);
    }

    private void doSaveAction(HttpServletRequest req){
        Student student = (Student)SessionUtils.GetInstance().GetUserFromSession(req);
        String question = req.getParameter(Constans.QUESTION);
        boolean isGoodAns =  Boolean.valueOf(req.getParameter(Constans.IS_GOOD_ANSWER));
        String studAns = req.getParameter(Constans.STUDENT_ANSWER);
        long questionId = Long.valueOf(req.getParameter(Constans.QUESTION_ID));
        EyeClassEngine.GetInstance().saveAnswerOfStudentInDB(student, question,isGoodAns,studAns,questionId);
    }


}


package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import Infra.CommonEnums;
import LessonManager.Lesson;
import LessonManager.MultipleQuestion;
import SchoolEntity.UsersEntity.Teacher;
import ViewModel.QuestionAnsViewModel;
import ViewModel.TeacherDistractionParamViewModel;
import com.google.gson.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class TeacherServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch (req.getParameter(Constans.REQUEST)) {
            case Constans.DEMO_LESSON:
                doDemoLesson(req);
                break;
            case Constans.START_LESSON:
                startLesson(req);
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
            case Constans.TEACHER_LESSON_SELECT:
                lessonsForTeacher(req, resp);
                break;
            case Constans.TEACHER_DISTRACTIONS:
                teacherLessonsDistractions(req, resp);
                break;
            case Constans.TEACHER_QUESTION_REPORT:
                teacherQuestionReport(req, resp);
                break;
            case Constans.CLASSES:
                getClassesMap(req, resp);
                break;
            case Constans.TEACHER_CURRICULUM:
                getCurriculumList(req, resp);
                break;
            case Constans.UPLOAD_LESSON:
                uploadLesson(req, resp);
                break;
            case Constans.TRACKER:
                setTracker(req);
                break;
        }
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    ///this is work around to check student without teacher application
    /// this get method doenst work from appication, it works only from browesr links
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String param = req.getParameter("status");
        if (param.contains("start"))
        {
            System.out.println("DEBUG MODE - Start");
            ArrayList<CommonEnums.Curriculum> a = new ArrayList<>();
            Teacher t = new Teacher(111111111,"12345", "Lakewood Elwood", "Test me please", a);
            EyeClassEngine.GetInstance().StartLesson(t, 1, "Lakewood Elwood_Grade11_1");
        }

        if (param.contains("end"))
        {
            System.out.println("DEBUG MODE - End");
            ArrayList<CommonEnums.Curriculum> a = new ArrayList<>();
            Teacher t = new Teacher(111111111,"12345", "Lakewood Elwood", "Test me please", a);
            EyeClassEngine.GetInstance().endLesson(t,  "Lakewood Elwood_Grade11_1");
        }

        if (param.contains("que"))
        {
            System.out.println("DEBUG MODE - Question");
            ArrayList<CommonEnums.Curriculum> a = new ArrayList<>();

            Teacher t = new Teacher(111111111,"12345", "Lakewood Elwood", "Test me please", a);
            String queJson = "{\"id\":2,\"question\":\"how much is 1 + 1\",\"rightAns\":\"2\",\"wrongOptions\":[\"1\",\"11\",\"3\"],\"topic\":\"basic que\",\"time\":100}";
            EyeClassEngine.GetInstance().setQuestionDataForDelivery(t, "Lakewood Elwood_Grade11_1", queJson);
        }
    }

    private void teacherQuestionReport(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        List<QuestionAnsViewModel> queAns = EyeClassEngine.GetInstance().getQuestionsAnsForTeacher(t);
        List<List<String>> data = new ArrayList<>();
        for(QuestionAnsViewModel model : queAns )
            data.add(model.getAsList());
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(data));
        out.close();
    }

    private void teacherLessonsDistractions(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        List<TeacherDistractionParamViewModel> distractions = EyeClassEngine.GetInstance().getDistractionForTeacher(t);
        List<List<String>> data = new ArrayList<>();
        for(TeacherDistractionParamViewModel model : distractions )
            data.add(model.getAsList());
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(data));
        out.close();
    }

    private void uploadLesson(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        String data = req.getParameter("data");
        boolean result = true;
        try
        {
            JsonObject jObj = new JsonParser().parse(data).getAsJsonObject();
            String headline = jObj.get("mTitle").getAsString();
            CommonEnums.Curriculum curriculum = CommonEnums.Curriculum.valueOf(jObj.get("mCurr").getAsString());
            JsonArray pdfArr = jObj.get("lessonFile").getAsJsonArray();
            byte[] pdfFile = new byte[pdfArr.size()];
            for (int i = 0; i < pdfArr.size(); i++)
                pdfFile[i] = pdfArr.get(i).getAsByte();
            //check if there is questions
            ArrayList<MultipleQuestion> questions = new ArrayList<>();
            if (jObj.get("questions") != null)
            {
                JsonArray questionsJsonArr = jObj.get("questions").getAsJsonArray();
                Gson gson = new Gson();
                for (JsonElement jEle : questionsJsonArr)
                    questions.add(gson.fromJson(jEle.toString(), MultipleQuestion.class));
            }
            result = EyeClassEngine.GetInstance().addLesson(t, pdfFile, headline, curriculum, questions);

        }
        catch (Exception e){
            result = false;
            System.out.println(e);
        }

        PrintWriter out = resp.getWriter();
        out.print(result);
        out.close();
    }

    private void setTracker(HttpServletRequest req) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        String class_id = req.getParameter(Constans.CLASS_ID);
        boolean toTrack = Boolean.valueOf(req.getParameter("track"));
        EyeClassEngine.GetInstance().setTracker(t, class_id, toTrack);
    }

    private void getCurriculumList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(t.getCurriculum()));
        out.close();
    }

    private void getClassesMap(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        List<SchoolEntity.Class> classes = EyeClassEngine.GetInstance().getAllClasses(t);
        HashMap<String, String> class_to_id = new HashMap<>();
        for (SchoolEntity.Class c : classes)
            class_to_id.put(c.GetClassName(), c.getID());

        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(class_to_id));
        out.close();
    }

    private void lessonsForTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        //get all lessons for this teacher
        List<Lesson> lessons = EyeClassEngine.GetInstance().getAllLessonsForTeacher(t);
        HashMap<String, HashMap<String, Long>> curculum_headline_id = new HashMap<>();
        for (Lesson l : lessons)
        {
            if (!curculum_headline_id.containsKey(l.get_curriculum().toString()))
                curculum_headline_id.put(l.get_curriculum().toString(), new HashMap<>());
            HashMap<String, Long> cur_data = curculum_headline_id.get(l.get_curriculum().toString());
            String headline = l.get_lessonHeadline();

            //verify there is no duplicate headlines
            if (cur_data.containsKey(headline))
                headline = headline + "_" + l.get_id();

            cur_data.put(headline, l.get_id());
            curculum_headline_id.put(l.get_curriculum().toString(), cur_data);
        }
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(curculum_headline_id));
        out.close();
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
        EyeClassEngine.GetInstance().StartLesson(t, 1, "ORT Eilat_Grade11_1");
    }

    private void startLesson(HttpServletRequest req) throws IOException, ServletException {
        Teacher t = (Teacher)SessionUtils.GetInstance().GetUserFromSession(req);
        long lesson_id =Long.valueOf(req.getParameter(Constans.LESSON_ID));
        String class_id = req.getParameter(Constans.CLASS_ID);
        EyeClassEngine.GetInstance().StartLesson(t, lesson_id, class_id);
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
        List<MultipleQuestion> data = EyeClassEngine.GetInstance().getQuestionsForClass(t, class_id);
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(data));
        out.close();
    }
}

package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import Infra.CommonEnums;
import SchoolEntity.UsersEntity.Admin;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import ViewModel.AdminDistractionParamViewModel;
import ViewModel.UsersViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("aaaaaa "+ req.getParameter(Constans.REQUEST));
        switch (req.getParameter(Constans.REQUEST)) {
            case Constans.USERS_LIST:
                usersList(req, resp);
                break;
            case Constans.ADMIN_DISTRACTIONS:
                adminDistractions(req, resp);
                break;
            case Constans.CLASSES:
                getClassesMap(req, resp);
                break;
            case Constans.CURRICULUM_LIST:
                getCurriculums(req, resp);
                break;
            case Constans.ADD_TEACHER:
                addTeacher(req, resp);
                break;
            case Constans.ADD_ADMIN:
                addAdmin(req, resp);
                break;
            case Constans.ADD_STUDENT:
                addStudent(req, resp);
                break;
        }
    }

    private void addTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Admin admin = (Admin)SessionUtils.GetInstance().GetUserFromSession(req);
        long id = Long.valueOf(req.getParameter("id"));
        String name = req.getParameter("name");
        String pass = req.getParameter("password");
        String cur_str = req.getParameter("extra");
        List<CommonEnums.Curriculum> cur_list = new Gson().fromJson(cur_str,  new TypeToken<ArrayList<CommonEnums.Curriculum>>(){}.getType());
        Teacher t = new Teacher(id, pass, admin.get_schoolId(), name, cur_list);
        String status = EyeClassEngine.GetInstance().addTeacher(admin, t);
        PrintWriter out = resp.getWriter();
        out.print(status);
        out.close();
    }

    private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Admin admin = (Admin)SessionUtils.GetInstance().GetUserFromSession(req);
        long id = Long.valueOf(req.getParameter("id"));
        String name = req.getParameter("name");
        String pass = req.getParameter("password");
        String class_str = req.getParameter("extra");
        Student s = new Student(id, pass, admin.get_schoolId(),name, class_str);
        String status = EyeClassEngine.GetInstance().addStudent(admin, s);
        PrintWriter out = resp.getWriter();
        out.print(status);
        out.close();
    }

    private void addAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Admin admin = (Admin)SessionUtils.GetInstance().GetUserFromSession(req);
        long id = Long.valueOf(req.getParameter("id"));
        String name = req.getParameter("name");
        String pass = req.getParameter("password");
        Admin new_admin = new Admin(id, pass, admin.get_schoolId(), name);
        String status = EyeClassEngine.GetInstance().addAdmin(admin, new_admin);
        PrintWriter out = resp.getWriter();
        out.print(status);
        out.close();
    }

    private void getCurriculums(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        List<String> enumNames = Stream.of(CommonEnums.Curriculum.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(enumNames));
        out.close();
    }

    private void getClassesMap(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Admin t = (Admin)SessionUtils.GetInstance().GetUserFromSession(req);
        List<SchoolEntity.Class> classes = EyeClassEngine.GetInstance().getAllClasses(t);
        HashMap<String, String> class_to_id = new HashMap<>();
        for (SchoolEntity.Class c : classes)
            class_to_id.put(c.GetClassName(), c.getID());

        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(class_to_id));
        out.close();
    }

    private void usersList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Admin a = (Admin)SessionUtils.GetInstance().GetUserFromSession(req);
        List<UsersViewModel> userslist = EyeClassEngine.GetInstance().getAllUsersViewModel(a);
        List<List<String>> data = new ArrayList<>();
        for (UsersViewModel model : userslist)
            data.add(model.getAsList());
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(data));
        out.close();
    }

    private void adminDistractions(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Admin a = (Admin)SessionUtils.GetInstance().GetUserFromSession(req);
        List<AdminDistractionParamViewModel> userslist = EyeClassEngine.GetInstance().getDistractionForAdmin(a);
        List<List<String>> data = new ArrayList<>();
        for (AdminDistractionParamViewModel model : userslist)
            data.add(model.getAsList());
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(data));
        out.close();
    }
}

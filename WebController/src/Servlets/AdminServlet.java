package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import SchoolEntity.UsersEntity.Admin;
import ViewModel.AdminDistractionParamViewModel;
import ViewModel.UsersViewModel;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch (req.getParameter(Constans.REQUEST)) {
            case Constans.USERS_LIST:
                usersList(req, resp);
                break;
            case Constans.ADMIN_DISTRACTIONS:
                adminDistractions(req, resp);
                break;
        }
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

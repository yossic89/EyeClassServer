package Servlets;

import Common.Constans;
import Controller.SessionUtils;
import Engine.EyeClassEngine;
import Infra.CommonEnums;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

//@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        loginAction(req, resp);
    }

    private void loginAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //check if user exsits
        String id_str = req.getParameter(Constans.ID);
        long id = Long.parseLong(id_str);
        String pass = req.getParameter(Constans.PASSWORD);
        CommonEnums.UserTypes type = CommonEnums.UserTypes.NONE;
        try {
            type = EyeClassEngine.GetInstance().GetUserType(id, pass);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //if user exists - add him to Session manager
        if (type != CommonEnums.UserTypes.NONE)
            SessionUtils.GetInstance().AddUserToSession(getServletContext(), EyeClassEngine.GetInstance().getUser(id));

        PrintWriter out = resp.getWriter();
        out.print(type.getValue());
    }
}


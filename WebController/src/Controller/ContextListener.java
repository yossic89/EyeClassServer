package Controller;

import Engine.DBConnection;
import Engine.EyeClassEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener  implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        EyeClassEngine.GetInstance().endAllLessons();
        DBConnection.GetInstance().Close();
    }
}

package Builder;

import Engine.EyeClassEngine;
import Engine.SchoolServer;
import Infra.CommonEnums;
import Infra.Config;
import SchoolEntity.Class;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Admin;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DBBuilder {
    SchoolServer server;
    String m_class;



    public void run()
    {
        System.out.println("*** START DB BUILDER ***");

        //copyDB
        String location = Config.getInstance().getDatabase().getDBPath();
        File file = new File(location);
        if (file.exists())
        {
            int suffix = 1;
            String dest = location + "_" + suffix;;
            while (file.exists())
            {
                dest = location + "_" + suffix;
                suffix++;
                file = new File(dest);
            }
            System.out.println("DB already exists: Copy DB from: "+ location + " to: " + dest);
            try{Files.copy(new File(location).toPath(), new File(dest).toPath());}
            catch (Exception e){
                System.out.println("Failed to copy DB: " + e.getMessage());
            }

        }


        //Delete prev DB
        EyeClassEngine.GetInstance().DeleteDB();

        //build new db
        //School
        EyeClassEngine.GetInstance().AddSchool(new School("Lakewood Elwood", "501 Orchard Ave, Ellwood"));
        server = EyeClassEngine.GetInstance().getSchoolServer("Lakewood Elwood");
        server.initMaps();

        //add classes
        List<CommonEnums.SchoolClasses> classes = new ArrayList<>();
        classes.add(CommonEnums.SchoolClasses.Grade10);
        classes.add(CommonEnums.SchoolClasses.Grade11);
        classes.add(CommonEnums.SchoolClasses.Grade12);
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        for(CommonEnums.SchoolClasses cl : classes)
        {
            for (int id : ids)
                server.addClass(new Class(cl, id, server.getSchool()));
        }
        //class Ya 1
        m_class = server.getClassByGradeId(CommonEnums.SchoolClasses.Grade11, 1).getID();

        //Add Admin
        server.addAdmin(new Admin(999999999, "54321", server.getSchool().GetName(), "Cecilia Tingley"));

        //Add teachers
        ArrayList<CommonEnums.Curriculum> cur = new ArrayList<>();
        cur.add(CommonEnums.Curriculum.History);
        cur.add(CommonEnums.Curriculum.Math);
        server.addTeacher(new Teacher(111111111, "12345", server.getSchool().GetName(), "Nigel Ratburn", cur));
        //Add students
        server.addStudentToClass(m_class,new Student(222222222, "12345", server.getSchool().GetName(), "Arthur Read", m_class));
        server.addStudentToClass(m_class,new Student(333333333, "12345", server.getSchool().GetName(), "Buster Baxter", m_class));
        server.addStudentToClass(m_class,new Student(444444444, "12345", server.getSchool().GetName(), "Muffy Crosswire", m_class));

        System.out.println("*** DB BUILDER DONE ***");
    }
}

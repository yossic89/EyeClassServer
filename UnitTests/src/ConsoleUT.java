import Engine.DBConnection;
import Engine.EyeClassEngine;
import Engine.SchoolServer;
import Infra.*;
import LessonManager.MultipleQuestion;
import SchoolEntity.Class;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import SchoolEntity.UsersEntity.User;

import java.io.File;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleUT {


    public ConsoleUT()
    {
        engine = new EyeClassEngine();
        engine.initMap();
        init();
    }

    private void init()
    {
        SchoolServer tmp = engine.getSchoolServer("ORT Eilat");
        if (tmp == null)
            engine.AddSchool(new School("ORT Eilat", "HaTmarim 12 Eilat"));
        server = engine.getSchoolServer("ORT Eilat");
        server.initMaps();

        Class t = server.getClassByGradeId(CommonEnums.SchoolClasses.Grade11, 1);
        if (t == null)
            server.addClass(new Class(CommonEnums.SchoolClasses.Grade11, 1, server.getSchool()));
        m_class = server.getClassByGradeId(CommonEnums.SchoolClasses.Grade11, 1).getID();
    }

    public void mainMenu(){
        System.out.println("Default school is Ort Eilat with class YA 1(יא 1)");
        List<String> menu = Arrays.asList("Reset all DB", "Show all users", "Add Student"
        ,"Add Teacher", "Add lesson", "Select lesson for teacher", "Exit" );

        while (true) {
            int indx = handleList(menu);

            switch (indx) {
                case 0:
                    engine.DeleteDB();
                    init();
                    break;
                case 1:
                    showAllUsers();
                    break;
                case 2:
                    addStudent();
                    break;
                case 3:
                    addTeacher();
                    break;
                case 4:
                    addLesson();
                    break;
                case 6:
                    System.out.println("BYE!");
                    return;

            }
        }
    }

    private void addLesson()
    {
        /*System.out.println(Config.getInstance().getDebug().getLogDir());
        //print all teacher id
        for (User u : server.getAllUsers()) {
            if (u instanceof Teacher) {
                System.out.println("Teacher: " + u.getM_id());
            }
        }
        Scanner scr = new Scanner(System.in);
        System.out.println("Write your teacher id");
        long id = scr.nextLong();*/
        byte[] arr;
        try{ arr =  Files.readAllBytes(Paths.get("Lesson.pdf"));




        String[] mop={"yes","no"};
        MultipleQuestion q1= new MultipleQuestion("are you a student?", "yes", mop);
        ArrayList<MultipleQuestion> alq=new ArrayList<>();
        alq.add(q1);
        boolean a =server.addLesson(arr, "fileush",123,CommonEnums.Curriculum.Bible, alq);
        System.out.println(a? "Add lesson done" : "Add lesson fail");
        }catch (Exception e){
        System.out.println(e.toString());
    }
    }

    private void addStudent()
    {
        Scanner scr = new Scanner(System.in);
        System.out.print("Enter ID: ");
        long id = scr.nextLong();
        scr.nextLine();
        System.out.print("Enter password(Top secret): ");
        String pass = scr.nextLine();
        System.out.print("Enter full name: ");
        String name = scr.nextLine();
        server.addStudentToClass(m_class,new Student(id, pass, server.getSchool().GetName(), name, m_class));
    }

    private void addTeacher()
    {
        Scanner scr = new Scanner(System.in);
        System.out.print("Enter ID: ");
        long id = scr.nextLong();
        scr.nextLine();
        System.out.print("Enter password(Top secret): ");
        String pass = scr.nextLine();
        System.out.print("Enter full name: ");
        String name = scr.nextLine();
        ArrayList<CommonEnums.Curriculum> l = getCurriculum();
        server.addTeacher(new Teacher(id, pass, server.getSchool().GetName(), name, l));
    }

    private ArrayList<CommonEnums.Curriculum> getCurriculum()
    {
        //print
        System.out.println("Select id, separated by comma");
        int i = 1;
        for (CommonEnums.Curriculum c : CommonEnums.Curriculum.values())
        {
            System.out.println(String.format("%d: %s", i, c.name()));
            i++;
        }

        String input = new Scanner(System.in).nextLine();
        String[] arr = input.split(",");
        ArrayList<CommonEnums.Curriculum> retVal = new ArrayList<>();
        for (String num : arr)
        {
            int indx = Integer.parseInt(num.trim());
            indx --;
            if (indx >=0 && indx < CommonEnums.Curriculum.values().length)
            {
                retVal.add(CommonEnums.Curriculum.values()[indx]);
            }
            else
                System.out.println(String.format("Amigo, index %d is out of range, i decided to forgive u and pass over", indx + 1));
        }
        return retVal;
    }

    private void showAllUsers()
    {
        for(User u :server.getAllUsers())
        {
            if (u instanceof Teacher)
            {
                System.out.println("Teacher: "+ u.getM_fullName());

                System.out.print("Teach: ");
                for (CommonEnums.Curriculum c : ((Teacher)u).getCurriculum())
                    System.out.print(c.name() +", ");
                System.out.println();
            }
            if (u instanceof Student)
            {
                System.out.println("Student: " + u.getM_fullName());
                System.out.println("Class: " + (server.getClassFromMap(((Student) u).getStudentClassId()).GetClassName()));
            }
        }
    }

    private int handleList(List<String> list)
    {
        int retVal;
        while(true) {
            for (int i = 0; i < list.size(); i++)
                System.out.println(String.format("%d: %s", i + 1, list.get(i)));

            Scanner reader = new Scanner(System.in);
            retVal = reader.nextInt();
            retVal--;
            if (retVal >=0 && retVal < list.size() )
                break;
        }
        return retVal;
    }

    EyeClassEngine engine;
    SchoolServer server;
    String m_class;

}

import Engine.EyeClassEngine;
import Engine.SchoolServer;
import Infra.*;
import LessonManager.Lesson;
import LessonManager.MultipleQuestion;
import SchoolEntity.Class;
import SchoolEntity.School;
import SchoolEntity.UsersEntity.Student;
import SchoolEntity.UsersEntity.Teacher;
import SchoolEntity.UsersEntity.User;

import java.nio.file.Files;
import java.nio.file.Paths;
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
                case 5:
                    showLesson();
                    break;
                case 6:
                    System.out.println("BYE!");
                    return;

            }
        }
    }
    private MultipleQuestion createMultipleQuestion(){
        //get question from teacher
        Scanner scr = new Scanner(System.in);
        System.out.print("Enter question: ");
        String q1 = scr.nextLine();
        System.out.print("Enter correct answer: ");
        String ans = scr.nextLine();
        System.out.print("How many false answers? ");
        int flsans=scr.nextInt();
        //ArrayList<String> allopt=new ArrayList<>();
        String[] allopt = new String[flsans+1];
        allopt[0]=ans;
        //allopt.add(ans);
        for(int i=1; i<flsans+1; i++){
            //get other answers
            System.out.print("Enter false answer: ");
            //ans = scr.nextLine();
            String fls = scr.next();
            allopt[i] = fls;
            //allopt.add(ans);
        }
        MultipleQuestion mulq1= new MultipleQuestion(q1, ans, allopt);
        return mulq1;
    }

    private void showLesson()
    {
        //get all teachers
        ArrayList<Long> teachersIdLong = server.getAllTeacherId();
        ArrayList<String> teacherIdStr = new ArrayList<>();
        for (long l : teachersIdLong)
            teacherIdStr.add(Long.toString(l));
        System.out.println("Select your teacher id");
        int indx = handleList(teacherIdStr);

        //get all lessons for this teacher
        ArrayList<Lesson> lessons = server.getAllLessonsForTeacher(teachersIdLong.get(indx));
        for (Lesson l : lessons)
        {
            System.out.println("LESSON:");
            System.out.println("Headline: " + l.get_lessonHeadline());
            System.out.println("Lesson pdf store in: " + l.get_filePath());
            System.out.println("Curriculum: " + l.get_curriculum());

            //print multi ques
            for (MultipleQuestion q : l.get_questions())
            {
                System.out.println("Q: " + q.getQuestionWithAns().getQuestion());
                System.out.println("A: " + q.getRightAns());
                System.out.println("Options: " + Arrays.toString(q.getQuestionWithAns().getOptions()));
            }
            System.out.println();
        }
    }

    private void addLesson()
    {
        byte[] arr;
        try{
            arr =  Files.readAllBytes(Paths.get("C:\\Lesson.pdf"));
            Scanner scr = new Scanner(System.in);
            System.out.print("How many questions? ");
            ArrayList<MultipleQuestion> allquests=new ArrayList<>();
            int quest = scr.nextInt();
            for(int i=0; i<quest; i++){
                MultipleQuestion q1 = createMultipleQuestion();
                allquests.add(q1);
            }
            System.out.print("File headline: ");
            String headln = scr.next();
            //get teacher's information
            System.out.print("Enter your ID: ");
            long id = scr.nextLong();
            //get teacher's curriculum
            Teacher t = server.getTeacherFromMap(id);
            System.out.println("Select only one curriculum id");
            for (CommonEnums.Curriculum c : (t.getCurriculum()))
                System.out.print(c.ordinal()+1 +". " + c.name() + ", ");
            System.out.println();
            int cur = scr.nextInt();
            boolean a =server.addLesson(arr, headln, id, CommonEnums.Curriculum.values()[cur-1], allquests);

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
        System.out.println("Select curriculum id, separated by comma");
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

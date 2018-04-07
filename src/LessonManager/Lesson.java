package LessonManager;

import Infra.CommonEnums;
import Infra.EyeBase;
import SchoolEntity.UsersEntity.Teacher;

import java.io.Serializable;
import java.util.ArrayList;

public class Lesson extends EyeBase implements Serializable {


    String filePath;
    ArrayList<MultipleQuestion> questions;
    Teacher author;
    String lessonHeadline;
    CommonEnums.Curriculum curriculum;
}

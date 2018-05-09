package Infra;

public class CommonEnums {

    public enum SchoolClasses
    {
        Grade1,
        Grade2,
        Grade3,
        Grade4,
        Grade5,
        Grade6,
        Grade7,
        Grade8,
        Grade9,
        Grade10,
        Grade11,
        Grade12,
    }

    public enum Curriculum{
        Math,
        History,
        English,
        Assembly,
        Bible,
    }

    public enum UserTypes {
        NONE(0),
        Admin(1),
        Teacher(2),
        Student(3);

        private final int value;
        private UserTypes(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum StudentConcentratedStatus {
        Unknown(0),
        Concentrated(1),
        NotConcentrated(2);

        private final int value;
        private StudentConcentratedStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}

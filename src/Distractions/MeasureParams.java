package Distractions;

public class MeasureParams {

    public MeasureParams(long student_id, int page, int eyes_count, byte[] photo_arr) {
        this.student_id = student_id;
        this.page = page;
        this.eyes_count = eyes_count;
        this.photo_arr = photo_arr;
    }

    public long getStudent_id() {
        return student_id;
    }

    public int getPage() {
        return page;
    }

    public int getEyes_count() {
        return eyes_count;
    }

    public byte[] getPhoto_arr() {
        return photo_arr;
    }

    private long student_id;
    private int page;
    private int eyes_count;
    private byte[] photo_arr;

}

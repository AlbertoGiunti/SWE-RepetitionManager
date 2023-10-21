package domainModel;

public class Booking {
    private final int idLesson;
    private String studentCF;

    public Booking(int idLesson, String studentCF){
        this.idLesson = idLesson;
        this.studentCF = studentCF;
    }

    // Getters
    public int getAdID() {
        return idLesson;
    }

    public String getStudentCF() {
        return studentCF;
    }
}

package businessLogic;

import domainModel.State.*;
import domainModel.Student;
import domainModel.Lesson;
import dao.LessonDAO;

import java.util.List;
import java.util.Objects;

public class BookingsController {
    private final LessonsController lessonsController;
    private final StudentsController studentsController;
    private final LessonDAO lessonDAO;

    public BookingsController(LessonsController lessonsController, StudentsController studentsController , LessonDAO lessonDAO){
        this.lessonsController = lessonsController;
        this.studentsController = studentsController;
        this.lessonDAO = lessonDAO;
    }

    /**
     *
     *
     * @param studentCF This is the Fiscal Code of the student
     * @param idLesson This is the lesson id
     * @throws Exception when idLesson or studentCF don't exist and bubbles up exceptions of LessonsController::changeLessonState, LessonDAO::changeState
     */

    public void bookLesson(String studentCF, int idLesson) throws Exception {
        Lesson lesson = lessonsController.getLesson(idLesson);
        Student student = studentsController.getPerson(studentCF);
        if (student == null) throw new IllegalArgumentException("The given student does not exist.");
        if ( lesson == null) throw new IllegalArgumentException("The given lesson id does not exist.");

        if(Objects.equals(lesson.getStateExtraInfo(), studentCF)){
            throw new RuntimeException("The given student is already booked for this lesson ");
        }

        // Questo controllo già lo faccio in lessonsController.changeLessonState
        if (!Objects.equals(lesson.getState(), "Available")){
            throw new RuntimeException("You cannot book this lesson");
        }

        for (Lesson l: this.getStudentBookedLessons(studentCF)){
            if ((l.getStartTime().isBefore(lesson.getEndTime()) || l.getStartTime().equals(lesson.getEndTime()))
                    && (l.getEndTime().isAfter(lesson.getStartTime()) || l.getEndTime().equals(lesson.getStartTime())))
                throw new RuntimeException("The given student is already occupied in the given time range (in course #" + l.getIdLesson() + ")");
        }

        Booked book = new Booked(student.getCF());

        this.lessonsController.changeLessonState(lesson.getIdLesson(), book);
    }

    /**
     * The student cancels the reservation, and the lesson becomes available again
     *
     * @param studentCF This is the Fiscal Code of the student
     * @param idLesson This is the lesson id
     * @throws Exception bubbles up exceptions of LessonsController::changeLessonState and LessonDAO::changeState
     */
    public void deleteLessonBooking(String studentCF, int idLesson) throws Exception{
        Available av = new Available();
        this.lessonsController.changeLessonState(idLesson, av);
    }

    /**
     * Get all the lessons booked by a student.
     *
     * @param studentCF This is the Fiscal Code of the student
     * @return All the lessons booked by the student
     * @throws Exception bubbles up exceptions of LessonDAO::getStudentBookedLesson
     */
    public List<Lesson> getStudentBookedLessons(String studentCF) throws Exception{
        return lessonDAO.getStudentBookedLessons(studentCF);
    }
}

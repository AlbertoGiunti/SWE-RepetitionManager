package businessLogic;

import domainModel.State.*;
import domainModel.Student;
import domainModel.Lesson;
import dao.LessonDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class StateController {
    private final LessonsController lessonsController;
    private final StudentsController studentsController;
    private final LessonDAO lessonDAO;

    public StateController(LessonsController lessonsController, StudentsController studentsController , LessonDAO lessonDAO){
        this.lessonsController = lessonsController;
        this.studentsController = studentsController;
        this.lessonDAO = lessonDAO;
    }

    /**
     * Book a lesson
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


        if (!Objects.equals(lesson.getState(), "Available")){
            throw new RuntimeException("You cannot book this lesson");
        }

        for (Lesson l: this.getStudentBookedLessons(studentCF)){
            if ((l.getStartTime().isBefore(lesson.getEndTime()) || l.getStartTime().equals(lesson.getEndTime()))
                    && (l.getEndTime().isAfter(lesson.getStartTime()) || l.getEndTime().equals(lesson.getStartTime())))
                throw new RuntimeException("The given student is already occupied in the given time range (in course #" + l.getIdLesson() + ")");
        }

        Booked book = new Booked(student.getCF());

        this.lessonDAO.changeState(idLesson, book);
    }

    /**
     * The student cancels the reservation, and the lesson becomes available again
     *
     * @param studentCF This is the Fiscal Code of the student
     * @param idLesson This is the lesson id
     * @throws Exception bubbles up exceptions of LessonDAO::changeState
     */
    public void deleteBooking(String studentCF, int idLesson) throws Exception{
        Lesson lesson = lessonsController.getLesson(idLesson);
        if ( lesson == null) throw new IllegalArgumentException("The given lesson id does not exist.");

        Student student = studentsController.getPerson(studentCF);
        if (student == null) throw new IllegalArgumentException("The given student does not exist.");


        if (!Objects.equals(lesson.getState(), "Booked"))
            throw new RuntimeException("The booking cannot be canceled because it is not in a 'Booked' state.");

        if(Objects.equals(lesson.getStateExtraInfo(), studentCF)){
            Available av = new Available();
            this.lessonDAO.changeState(idLesson, av);
        }else {
            throw new RuntimeException("Student" + studentCF + "is not booked for lesson" + idLesson + ".");
        }

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

    /**
     * Cancel a Lesson, you cannot cancel a completed lesson.
     *
     * @param idLesson
     * @throws Exception
     */
    public void cancelLesson(int idLesson) throws Exception {
        Lesson lesson = lessonsController.getLesson(idLesson);
        if ( lesson == null) throw new IllegalArgumentException("The given lesson id does not exist.");

        if (Objects.equals(lesson.getState(), "Completed")){
            throw new RuntimeException("You cannot cancel a lesson that has already been completed.");
        }

        Cancelled cancelled = new Cancelled(LocalDateTime.now());
        this.lessonDAO.changeState(idLesson, cancelled);
    }

    /**
     * Set as completed a booked lesson.
     *
     * @param idLesson
     * @throws Exception
     */
    public void completeLesson(int idLesson) throws Exception{
        Lesson lesson = lessonsController.getLesson(idLesson);
        if ( lesson == null) throw new IllegalArgumentException("The given lesson id does not exist.");

        if (!Objects.equals(lesson.getState(), "Booked")){
            throw new RuntimeException("You cannot set as completed a lesson that is not booked.");
        }

        Completed completed = new Completed(LocalDateTime.now());
        this.lessonDAO.changeState(idLesson, completed);
    }
}

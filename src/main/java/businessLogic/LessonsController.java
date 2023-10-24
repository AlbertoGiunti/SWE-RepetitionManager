package businessLogic;

import dao.LessonDAO;
import domainModel.Lesson;
import domainModel.Tutor;

import java.time.LocalDateTime;

public class LessonsController {

    private final LessonDAO lessonDAO;

    private final PeopleController<Tutor> tutorsController;

    public LessonsController(LessonDAO lessonDAO, PeopleController<Tutor> tutorsController){
        this.lessonDAO = lessonDAO;
        this.tutorsController = tutorsController;
    }

    /**
     * Adds a new lesson to the list
     *
     * @param title             The title of the lesson
     * @param description       The description of the lesson
     * @param startTime         The start time of the lesson
     * @param endTime           The end time of the lesson
     * @param price             The price of the lesson
     * @param tutorCF           The fiscal code of the tutor for the lesson
     *
     * @return The id of the newly created lesson
     *
     * @throws Exception If the tutor is not found, bubbles up exceptions of LessonDAO::insert()
     * @throws IllegalArgumentException If the tutor is already occupied in the given time range
     */

    /**
    public int addLesson(String title, String description, LocalDateTime startTime, LocalDateTime endTime, double price, String tutorCF) throws Exception{
        Tutor tutor = tutorsController.getPerson(tutorCF);
        if (tutor == null)
            throw new IllegalArgumentException("Tutor not found");

        // Check if the given tutor is not already occupied for the given time range
        for (Lesson l : this.lessonDAO.getAll()) {
            if (l.getTutorCF().getFiscalCode().equals(trainerFiscalCode)) {
                if ((l.getStartDate().isBefore(endDate) || l.getStartDate().equals(endDate))
                        && (l.getEndDate().isAfter(startDate) || l.getEndDate().equals(startDate)))
                    throw new IllegalArgumentException("The given trainer is already occupied in the given time range (in course #" + l.getId() + ")");
            }
        }

    }
     */
}

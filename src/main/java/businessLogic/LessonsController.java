package businessLogic;

import dao.LessonDAO;
import dao.TagDAO;
import domainModel.Lesson;
import domainModel.State.*;
import domainModel.Tags.*;
import domainModel.Tutor;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


import static java.util.Collections.unmodifiableList;

public class LessonsController {

    private final LessonDAO lessonDAO;

    private final TagDAO tagDAO;

    private final PeopleController<Tutor> tutorsController;


    public LessonsController(LessonDAO lessonDAO, TagDAO tagDAO, PeopleController<Tutor> tutorsController){
        this.lessonDAO = lessonDAO;
        this.tagDAO =  tagDAO;
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
    public int addLesson(String title, String description, LocalDateTime startTime, LocalDateTime endTime, double price, String tutorCF, List<Tag> tags) throws Exception{
        Tutor tutor = tutorsController.getPerson(tutorCF);
        if (tutor == null)
            throw new IllegalArgumentException("Tutor not found");

        // Check if the given tutor is not already occupied for the given time range
        for (Lesson l : this.lessonDAO.getAll()) {
            if (l.getTutorCF().equals(tutorCF)) {
                if ((l.getStartTime().isBefore(endTime) || l.getStartTime().equals(endTime))
                        && (l.getEndTime().isAfter(startTime) || l.getEndTime().equals(startTime)))
                    throw new RuntimeException("The given tutor is already occupied in the given time range (in course #" + l.getIdLesson() + ")");
            }
        }

        Lesson l = new Lesson(lessonDAO.getNextLessonID(), title, description, startTime, endTime, price, tutorCF);
        // Aggiungo i tag alla lezione che poi creerà il collegamento tag-lezione
        for (Tag t : tags){
            l.addTag(t);
        }
        lessonDAO.insert(l);
        return l.getIdLesson();
    }

    /**
     * Delete a lesson
     *
     * @param idLesson
     * @return
     * @throws Exception
     */
    public boolean removeLesson(int idLesson) throws Exception{
        return lessonDAO.delete(idLesson);
    }

    /**
     * Return the given lesson
     *
     * @param idLesson
     * @return
     * @throws Exception
     */
    public Lesson getLesson(int idLesson) throws Exception {
        return lessonDAO.get(idLesson);
    }

    /**
     * Returns a read-only list of lessons
     *
     * @return
     * @throws Exception
     */
    public List<Lesson> getAll() throws Exception {
        return unmodifiableList(this.lessonDAO.getAll());
    }

    /**
     * Get all the lessons of a tutor that are in that state
     *
     * @param tutorCF
     * @param state
     * @return
     * @throws Exception
     */
    public List<Lesson> getTutorLessonByState(String tutorCF, State state) throws Exception {
        return lessonDAO.getTutorLessonsByState(tutorCF, state);
    }

    /**
     * This method change the lesson state.
     *
     * @param idLesson
     * @param newState
     * @throws Exception
     */
    public void changeLessonState(int idLesson, State newState) throws Exception {
        Lesson lesson = null;
        lesson = lessonDAO.get(idLesson);
        if(lesson == null){
            throw new IllegalArgumentException("Lesson not found");
        }

        String oldState = lesson.getState();

        switch (oldState) {
            case "Completed":
                throw new IllegalArgumentException("You cannot change the status of a lesson that has already been conducted.");
            case "Available":
                switch (newState.getState()) {
                    case "Booked":
                    case "Cancelled":
                        lessonDAO.changeState(idLesson, newState);
                    default:
                        throw new IllegalArgumentException("This lesson is " + oldState + "and cannot be changed to" + newState.getState());
                }

            case "Booked":
                switch (newState.getState()) {
                    case "Available":
                    case "Cancelled":
                    case "Completed":
                        lessonDAO.changeState(idLesson, newState);
                    default:
                        throw new IllegalArgumentException("This lesson is " + oldState + "and cannot be changed to" + newState.getState());
                }

            case "Cancelled":
                if (newState.getState().equals("Available")) {
                    lessonDAO.changeState(idLesson, newState);
                } else {
                    throw new IllegalArgumentException("This lesson is " + oldState + "and cannot be changed to" + newState.getState());
                }

            default:
                throw new IllegalArgumentException("Invalid state");
        }
    }

    /**
     * Attach an existing tag to a lesson
     *
     * @param idLesson
     * @param tagToAttach
     * @throws Exception bubbles up exception of TagDAO::getTag
     */
    public void attachTag(int idLesson, Tag tagToAttach) throws Exception {
        if (this.tagDAO.getTag(tagToAttach.getTag(), tagToAttach.getTypeOfTag()) != null){
            this.tagDAO.attachTag(idLesson, tagToAttach);
        }else
            throw new IllegalArgumentException("This tag does not exist");
    }

    /**
     *
     *
     * @param idLesson
     * @param tagToDetach
     * @return
     * @throws Exception
     */
    public boolean detachTag(int idLesson, Tag tagToDetach) throws Exception{
        List<Tag> tags = this.tagDAO.getTagsByLesson(idLesson);
        for (Tag t : tags){
            if (Objects.equals(t.getTypeOfTag(), tagToDetach.getTypeOfTag()) && Objects.equals(t.getTag(), tagToDetach.getTypeOfTag())){
                return this.tagDAO.detachTag(idLesson, tagToDetach);
            }
        }
        return false;
    }
}

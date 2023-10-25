package dao;

import domainModel.Lesson;
import domainModel.State.State;
import domainModel.Tags.*;

import java.util.List;

public interface LessonDAO extends DAO <Lesson, Integer> {
    /**
     * Get last inserted lesson
     */
    public int getLastLessonID() throws Exception;


    /**
     * Get all the lessons of a tutor in a specific state
     *
     * @param tCF
     * @param state
     * @return
     * @throws Exception
     */
    public List<Lesson> getTutorLessonsByState(String tCF, State state) throws Exception;

    /**
     * Get all the booked lessons of a student
     *
     * @param sCF
     * @return
     * @throws Exception
     */
    public List<Lesson> getStudentBookedLessons(String sCF) throws Exception;

    /**
     * Change the state of a lesson
     *
     * @param idLesson
     * @param newState
     * @throws Exception
     */
    public void changeState(Integer idLesson, State newState) throws Exception;
}

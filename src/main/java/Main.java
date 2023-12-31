import businessLogic.*;
import dao.*;
import domainModel.*;
import domainModel.Tags.*;
import domainModel.Search.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception{

        Database.setDatabase("main.db");
        Database.initDatabase();

        // DAOS
        TutorDAO tutorDAO = new SQLiteTutorDAO();
        StudentDAO studentDAO = new SQLiteStudentDAO();
        TagDAO tagDAO = new SQLiteTagDAO();
        LessonDAO lessonDAO = new SQLiteLessonDAO(tagDAO);

        //Controllers
        TutorsController tutorsController = new TutorsController(tutorDAO);
        StudentsController studentsController = new StudentsController(studentDAO);
        TagsController tagsController = new TagsController(tagDAO);
        LessonsController lessonsController = new LessonsController(lessonDAO, tagDAO, tutorsController);
        StateController stateController = new StateController(lessonsController, studentsController, lessonDAO);


        // Add sample tutors
        tutorsController.addPerson("tutor1", "Tutor", "Uno", "1234A");
        tutorsController.addPerson("tutor2", "Tutor", "Due", "5678B");
        tutorsController.addPerson("tutor3", "Tutor", "Tre", "1011C");

        // Add sample students
        studentsController.addPerson("student1", "Student", "Uno", "Elementary");
        studentsController.addPerson("student2", "Student", "Due", "Elementary");
        studentsController.addPerson("student3", "Student", "Tre", "Elementary");

        // Add sample tags
        Tag tagZoneFirenze = tagsController.createTag("Firenze", "Zone");
        Tag tagZoneMilano = tagsController.createTag("Milano", "Zone");
        Tag tagZoneParma = tagsController.createTag("Parma", "Zone");

        Tag tagSubjectMatematica = tagsController.createTag("Matematica", "Subject");
        Tag tagSubjectFisica = tagsController.createTag("Fisica", "Subject");
        Tag tagSubjectInglese = tagsController.createTag("Inglese", "Subject");
        Tag tagSubjectProbabilita = tagsController.createTag("Probabilita", "Subject");
        Tag tagSubjectStatistica = tagsController.createTag("Statistica", "Subject");


        // Creates a list of tags
        List<Tag> tags1 = new ArrayList<>();
        tags1.add(tagZoneFirenze);
        tags1.add(tagSubjectMatematica);
        tags1.add(tagSubjectStatistica);
        tags1.add(tagSubjectProbabilita);
        tags1.add(tagSubjectInglese);

        List<Tag> tags2 = new ArrayList<>();
        tags2.add(tagZoneMilano);
        tags2.add(tagSubjectFisica);
        tags2.add(tagSubjectMatematica);
        tags2.add(tagSubjectInglese);

        List<Tag> tags3 = new ArrayList<>();
        tags3.add(tagZoneParma);
        tags3.add(tagSubjectFisica);

        List<Tag> tags4 = new ArrayList<>();
        tags4.add(tagZoneMilano);
        tags4.add(tagSubjectInglese);

        List<Tag> tags5 = new ArrayList<>();
        tags5.add(tagZoneFirenze);
        tags5.add(tagSubjectInglese);


        // Add sample lessons
        int lMatFi = lessonsController.addLesson("Lezione di Matematica", "Prima lezione di matematica a Firenze", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 25, "tutor1", tags1);
        int lFisMi = lessonsController.addLesson("Lezione di Fisica", "Prima lezione di fisica a Milano", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 25.00, "tutor2", tags2);
        int lFisPa = lessonsController.addLesson("Lezione di Fisica", "Prima lezione di fisica a Parma", LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5), 30.00, "tutor2", tags3);
        int lIngMi = lessonsController.addLesson("Lezione di Inglese", "Prima lezione di inglese a Milano", LocalDateTime.now().plusHours(23), LocalDateTime.now().plusHours(24), 35.00, "tutor2", tags4);
        int lIngFi = lessonsController.addLesson("Lezione di Inglese", "Prima lezione di inglese a Firenze", LocalDateTime.now().plusHours(27), LocalDateTime.now().plusHours(29), 18.00, "tutor2", tags5);

        // DECORATOR
        System.out.println("Searching for lessons with Matematica and Inglese tags and price less than 29.00. Query generated:");
        List<Lesson> lessons = lessonsController.search(
                new DecoratorSearchPrice
                    (new DecoratorSearchSubject(
                            new DecoratorSearchSubject(
                                    new SearchConcrete(),
                            "Matematica"),
                    "Inglese"),
                29.00));

        System.out.println("\nResults:");
        for (Lesson l : lessons){
            System.out.println(l.toString());
        }

        System.out.println("\nSearching for lessons starting from one hour ago. Query generated:");

        List<Lesson> lessons2= lessonsController.search(new DecoratorSearchStartTime(
                new SearchConcrete(), LocalDateTime.now().plusHours(5)));

        System.out.println("\nResults:");
        for (Lesson l : lessons2){
            System.out.println(l.toString());
        }

    }
}
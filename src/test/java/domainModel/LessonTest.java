package domainModel;

import domainModel.Tags.*;
import domainModel.State.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import java.time.LocalDateTime;

public class LessonTest {
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        // Inizializza una nuova lezione prima di ogni test
        lesson = new Lesson(1, "Math Lesson", "Learn math", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 10.0, "12345");
    }

    @Test
    void testLessonCreationWithValidDates() {
        // Assicura che la creazione di una lezione con date valide non lanci un'eccezione
        Assertions.assertDoesNotThrow(() -> new Lesson(2, "Science Lesson", "Learn science", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 20.0, "67890"));
    }

    @Test
    void testLessonCreationWithInvalidDates() {
        // Assicura che la creazione di una lezione con data di fine precedente a quella di inizio generi un'eccezione
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Lesson(3, "Invalid Lesson", "Invalid dates", LocalDateTime.now(), LocalDateTime.now().minusHours(1), 5.0, "54321"));
    }

    @Test
    void testGetters() {
        // Verifica i metodi di getter
        Assertions.assertEquals(1, lesson.getIdLesson());
        Assertions.assertEquals("Math Lesson", lesson.getTitle());
        Assertions.assertEquals("Learn math", lesson.getDescription());
        Assertions.assertNotNull(lesson.getStartTime());
        Assertions.assertNotNull(lesson.getEndTime());
        Assertions.assertEquals(10.0, lesson.getPrice());
        Assertions.assertEquals("12345", lesson.getTutorCF());
    }

    @Test
    void testSetState() {
        // Verifica il metodo di impostazione dello stato
        Available availableState = new Available();
        lesson.setState(availableState);
        Assertions.assertEquals("Available", lesson.getState());
        Assertions.assertEquals("", lesson.getStateExtraInfo());
    }

    @Test
    void testTagOperations() {
        // Verifica i metodi di gestione dei tag
        TagZone tagZone = new TagZone(1, "Firenze");
        TagSubject tagSubject = new TagSubject( 2, "Matematica");

        // Aggiungi un tag
        lesson.addTag(tagZone);
        Assertions.assertTrue(lesson.getTags().contains(tagZone));

        // Rimuovi un tag
        lesson.removeTag("Zone", "Tag1");
        Assertions.assertFalse(lesson.getTags().contains(tagZone));

        // Tentativo di rimuovere un tag che non esiste
        boolean removed = lesson.removeTag("Type2", "Tag2");
        Assertions.assertFalse(removed);
    }
}

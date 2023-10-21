package businessLogic;

import dao.DAO;
import domainModel.Person;

import java.util.List;
import static java.util.Collections.unmodifiableList;

public abstract class PeopleController <T extends Person> {
    // L'ID in questo caso è il CF, quindi una String
    private final DAO<T, String> dao;

    // Constructor
    PeopleController(DAO<T, String> dao){
        this.dao = dao;
    }

    /**
     * Add a new Person in the DB
     *
     * @param newPerson The new person (Tutor or Student)
     *
     * @return The fiscal code of the newly created person
     */
    protected String addPerson(T newPerson) throws Exception{
        this.dao.insert(newPerson);
        return newPerson.getCF();
    }

    /**
     * Remove from the DB the person with the corresponding CF
     */
    public boolean removePerson(String cf) throws Exception{
        // Le delete sono booleane
        return this.dao.delete(cf);
    }

    /** Returns the person with the corresponding CF */
    public T getPerson(String cf) throws Exception {
        return this.dao.get(cf);
    }

    /**
     * Returns a read-only list of people
     *
     * @return The list of people
     */
    public List<T> getAll() throws Exception {
        return unmodifiableList(this.dao.getAll());
    }

}

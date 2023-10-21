package businessLogic;

import domainModel.Tutor;
import dao.TutorDAO;

public class TutorsController extends PeopleController<Tutor>{
    public TutorsController(TutorDAO tutorDAO){
        super(tutorDAO);
    }

    /**
     * Add a new tutor
     *
     * @return The CF of the newly created tutor
     * @throws Exception bubbles up exceptions of PeopleController::addPerson()
     */
    public String addPerson(String cf, String name, String surname, String iban) throws Exception {
        Tutor t = new Tutor(cf, name, surname, iban);
        return super.addPerson(t);
    }

}

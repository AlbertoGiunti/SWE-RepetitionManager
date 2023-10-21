package businessLogic;

import dao.StudentDAO;
import domainModel.Student;

public class StudentsController extends PeopleController<Student>{
    public StudentsController(StudentDAO studentDAO){
        super(studentDAO);
    }

    /**
     * Add a new student
     *
     * @return The CF of the newly created student
     * @throws Exception bubbles up exceptions of PeopleController::addPerson()
     */
    public String addPerson(String cf, String name, String surname, String level) throws Exception {
        Student s = new Student(cf, name, surname, level);
        return super.addPerson(s);
    }
}

package domainModel;

public class Student extends Person{
    private String level;

    // Constructor
    public Student(String CFS, String name, String surname, String level) {
        super(CFS, name, surname);
        this.level = level;
    }

    // Getters
    public String getLevel() {
        return level;
    }

    // Setters
    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "fiscalCode='" + getCF() + '\'' +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", level=" + level +
                '}';
    }
}

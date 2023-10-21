package domainModel;

public class Tutor extends Person{
    private String iban;

    // Constructor
    public Tutor(String CFT, String name, String surname, String iban) {
        super(CFT, name, surname);
        this.iban = iban;
    }

    // Getters
    public String getIban() {
        return iban;
    }

    // Setters
    public void setIban(String iban) {
        this.iban = iban;
    }

    // The method toString() returns a string-format representation of the object that includes all the fields.
    @Override
    public String toString() {
        return "Trainer{" +
                "fiscalCode='" + getCF() + '\'' +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", iban=" + iban +
                '}';
    }
}

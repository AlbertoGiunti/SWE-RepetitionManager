package domainModel.State;

public class Booked extends State {
    private String studentCF;

    public Booked(String studentCF){
        this.state = "Booked";
        this.studentCF = studentCF;
    }

    @Override
    public String getState(){
        return this.state;
    }

    @Override
    public String getStateInfo(){
        return "This lesson is" + state + " by " + studentCF + ".";
    }



}

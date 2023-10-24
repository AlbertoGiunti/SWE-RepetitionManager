package domainModel.State;

public class Available extends State {

    public Available(){
        this.state = "Available";
    }

    @Override
    public String getStateInfo(){
        return "This lesson is" + state + ".";
    }

}

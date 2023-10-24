package domainModel.State;

import java.time.LocalDateTime;

public class Completed extends State {
    private final LocalDateTime completedTime;

    public Completed(LocalDateTime ldt){
        this.state = "Completed";
        this.completedTime = ldt;
    }

    @Override
    public String getState(){
        return this.state;
    }

    @Override
    public String getStateInfo(){
        return "This lesson got" + state + " at " + completedTime + ".";
    }
}

package domainModel.State;

import java.time.LocalDateTime;

public class Cancelled extends State {

    private final LocalDateTime cancelledTime;

    public Cancelled(LocalDateTime ldt){
        this.state = "Cancelled";
        this.cancelledTime = ldt;
    }

    @Override
    public String getState(){
        return this.state;
    }

    @Override
    public String getExtraInfo(){
        return this.cancelledTime.toString();
    }
}

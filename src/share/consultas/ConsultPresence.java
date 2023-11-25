package share.consultas;

import share.events.events;
import share.login.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsultPresence implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<login> log;
    private List<events> event;

    public ConsultPresence() {
        log = new ArrayList<>();
        event = new ArrayList<>();
    }

    public List<login> getLog() {return log;}

    public void setLog(List<login> log) {this.log = log;}

    public List<events> getEvent() {return event;}

    public void setEvent(List<events> event) {this.event = event;}
}

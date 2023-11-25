package share.consultas;

import share.events.events;
import share.registo.registo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsultPresence implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<registo> reg;
    private List<events> event;

    public ConsultPresence() {
        reg = new ArrayList<>();
        event = new ArrayList<>();
    }

    public List<registo> getLog() {return reg;}

    public void setLog(List<registo> reg) {this.reg = reg;}

    public List<events> getEvent() {return event;}

    public void setEvent(List<events> event) {this.event = event;}
}

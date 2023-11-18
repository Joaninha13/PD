package share.consultas;

import share.events.events;
import share.login.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsultPresence implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email, designacao, filtro, msg;

    private List<login> log;
    private List<events> event;

    public ConsultPresence(String email, String designacao, String filtro, String msg) {
        log = new ArrayList<>();
        event = new ArrayList<>();
        this.designacao = designacao;
        this.msg = msg;
        this.email = email;
        this.filtro = filtro;

    }

    public String getDesignacao() {return designacao;}

    public void setDesignacao(String designacao) {this.designacao = designacao;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getFiltro() {return filtro;}

    public void setFiltro(String filtro) {this.filtro = filtro;}

    public String getMsg() {return msg;}

    public void setMsg(String msg) {this.msg = msg;}

    public List<login> getLog() {return log;}

    public void setLog(List<login> log) {this.log = log;}

    public List<events> getEvent() {return event;}

    public void setEvent(List<events> event) {this.event = event;}
}

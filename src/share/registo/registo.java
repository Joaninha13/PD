package share.registo;

import java.io.Serializable;

public class registo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String identificationNumber;
    private String email;
    private String password;
    private String msg;
    private boolean isRegistered;

    public registo(String name, String identificationNumber, String email, String password) {
        this.name = name;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.password = password;
        this.isRegistered = false;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }
}

package share.login;

import java.io.Serializable;

public class login implements Serializable {

    private static final long serialVersionUID = 1L;
    String email, pass, msg;
    boolean isValid;

    public login(String email, String pass) {
        this.email = email;
        this.pass = pass;
        this.isValid = false;
        this.msg = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}

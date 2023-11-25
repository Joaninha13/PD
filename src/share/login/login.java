package share.login;

import java.io.Serializable;

public class login implements Serializable {

    private static final long serialVersionUID = 1L;
    private String email, pass, msg;
    private boolean isValid;
    private boolean isAdmin;

    public login(String email, String pass) {
        this.email = email;
        this.pass = pass;
        this.isValid = false;
        this.isAdmin = false;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}

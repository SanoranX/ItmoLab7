package server.util;

import java.io.Serializable;

public class ServerRequest implements Serializable {

    private String syntax;
    private String arg;

    private String mail = "";
    private String password = "";


    public ServerRequest(String command, String arg){
        this.syntax = command;
        this.arg = arg;
    }

    public String getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }

    public String getArg() {
        return arg;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

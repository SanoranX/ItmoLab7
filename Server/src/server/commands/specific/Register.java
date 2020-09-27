package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class Register extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {

        if(!db.checkUsername(serverRequest.getArg().split(",")[0]) && !checkLogin(serverRequest)) {
            db.addUser(serverRequest.getArg().split(",")[0], serverRequest.getArg().split(",")[1]);
            return "Аккаунт зарегистрирован.";
        }
        else{
            return "Данный email уже зарегистрирован!";
        }
    }
}

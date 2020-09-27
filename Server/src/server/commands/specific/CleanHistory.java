package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class CleanHistory extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        if (checkLogin(serverRequest) && db.checkUser(serverRequest.getMail(), serverRequest.getPassword())) {
            history = "";
            return "История комманд была очищена";
        }
        else if (!checkLogin(serverRequest))
            return "Вы не ввели данные о своём аккаунте";
        else{
            return "Аккаунт не был найден/Введён неверный пароль.";
        }

    }
}

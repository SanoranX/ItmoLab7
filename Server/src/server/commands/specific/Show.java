package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class Show extends AbstractCommand{

    @Override
    public String execute(ServerRequest serverRequest) {
        if (checkLogin(serverRequest) && db.checkUser(serverRequest.getMail(), serverRequest.getPassword())) {
            if (routes.size() == 0)
                return "Коллекция пуста";
            else {
                final String[] result = new String[1];
                routes.stream().forEach(x -> {
                    result[0] = result[0] + x.toString();
                });
                return result[0];
            }
        }
        else if (!checkLogin(serverRequest))
            return "Вы не ввели данные о своём аккаунте";
        else{
            return "Аккаунт не был найден/Введён неверный пароль.";
        }
    }
}

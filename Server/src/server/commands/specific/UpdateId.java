package server.commands.specific;

import server.collection.Route;
import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class UpdateId extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        if (checkLogin(serverRequest) && db.checkUser(serverRequest.getMail(), serverRequest.getPassword())) {
            try {
                db.remove(1, 1);
                db.add(new Route(db.count(), serverRequest.getArg()), 1);
                db.getRoutes(routes);
                return "Всё вышло.";
            } catch (Exception e) {
                e.printStackTrace();
                return "Не вышло";
            }
        }
        else if (!checkLogin(serverRequest))
            return "Вы не ввели данные о своём аккаунте";
        else{
            return "Аккаунт не был найден/Введён неверный пароль.";
        }
    }
}

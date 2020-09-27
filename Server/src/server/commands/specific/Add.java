package server.commands.specific;

import server.collection.Route;
import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class Add extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        if (checkLogin(serverRequest) && db.checkUser(serverRequest.getMail(), serverRequest.getPassword())) {
            try {
                Route route = new Route(db.count(), serverRequest.getArg());
                route.setCreatorId(db.getUserId(serverRequest.getMail(), serverRequest.getPassword()));
                db.add(route, db.getUserId(serverRequest.getMail(), serverRequest.getPassword()));
                db.getRoutes(routes);
                return "Элемент был добавлен в коллекцию!";
            } catch (Exception o) {
                o.printStackTrace();
                return "Во время выполнения команды произошла ошибка!";
            }
        } else if (!checkLogin(serverRequest))
            return "Вы не ввели данные о своём аккаунте";
        else {
            return "Аккаунт не был найден/Введён неверный пароль.";
        }
    }
}

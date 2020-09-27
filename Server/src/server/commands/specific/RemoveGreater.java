package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class RemoveGreater extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        if (checkLogin(serverRequest) && db.checkUser(serverRequest.getMail(), serverRequest.getPassword())) {
            try {
                int id = Integer.parseInt(serverRequest.getArg().split(",")[0]);
                int userId = db.getUserId(serverRequest.getMail(), serverRequest.getPassword());
                db.removeGreater(id, userId);
                db.getRoutes(routes);
                return "Выполнено";
            } catch (Exception e) {
                e.printStackTrace();
                return "Ошибка";
            }
        }
        else if (!checkLogin(serverRequest))
            return "Вы не ввели данные о своём аккаунте";
        else{
            return "Аккаунт не был найден/Введён неверный пароль.";
        }
    }
}

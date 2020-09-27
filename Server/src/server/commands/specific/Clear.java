package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class Clear extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        if (checkLogin(serverRequest) && db.checkUser(serverRequest.getMail(), serverRequest.getPassword())) {
            try {
                int before = db.count();
                db.removeUsersElement(db.getUserId(serverRequest.getMail(), serverRequest.getPassword()));
                int after = db.count();
                db.getRoutes(routes);
                return ("Все ваши элементы были удалены\nВсего было убрано: " + (before - after) + " элементов.\nКол-во сейчас: " + db.count());
            } catch (Exception e) {
                return "Произошла ошибка";
            }
        }
        else if (!checkLogin(serverRequest))
            return "Вы не ввели данные о своём аккаунте";
        else{
            return "Аккаунт не был найден/Введён неверный пароль.";
        }
    }
}

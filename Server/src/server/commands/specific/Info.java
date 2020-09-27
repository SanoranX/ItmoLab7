package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class Info extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        if (checkLogin(serverRequest) && db.checkUser(serverRequest.getMail(), serverRequest.getPassword())) {
            return "\n[Информация в базе данных] Кол-во элементов равно: " + db.count() + "\n[Информация из памяти] " + "Кол-во элементов в памяти: " + routes.size();
        }
        else if (!checkLogin(serverRequest))
            return "Вы не ввели данные о своём аккаунте";
        else{
            return "Аккаунт не был найден/Введён неверный пароль.";
        }
    }
}

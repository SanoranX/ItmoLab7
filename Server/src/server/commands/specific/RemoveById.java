package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class RemoveById extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        if (checkLogin(serverRequest) && db.checkUser(serverRequest.getMail(), serverRequest.getPassword())) {
            try {
                int before = db.count();
                db.remove(Integer.valueOf(serverRequest.getArg()), db.getUserId(serverRequest.getMail(), serverRequest.getPassword()));
                db.getRoutes(routes);
                int now = db.count();
                System.out.println(routes.size());
                if(before == now)
                    return "Ничего не было удалено. Возможно элемент коллекции не принадлежит вам";
                else
                    return ("Количество элементов до этого " + before + "\nУдалено: " + (before - now)+ "\nСейчас: " + db.count());

            } catch (Exception e) {
                e.printStackTrace();
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

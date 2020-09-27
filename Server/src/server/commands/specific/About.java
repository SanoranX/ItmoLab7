package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class About extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        return "Автор: Рафаилов Илья\n" +
                "Вариант: \n" +
                "Для выполнения команд зарегистрируйтесь в команде register";
    }
}

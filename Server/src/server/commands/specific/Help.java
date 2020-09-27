package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class Help extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        final String[] result = {"\n"};
        commands.stream().forEach(x -> {
            result[0] = result[0] + x.toString();
        });

        result[0] += "login - Команда, работающая только на клиенте, что позволяет зайти в свой аккаунт\n";
        return result[0];
    }
}

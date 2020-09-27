package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class History extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        return history;
    }
}

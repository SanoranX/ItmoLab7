package server.commands.specific;

import server.commands.AbstractCommand;
import server.util.ServerRequest;

public class GetAccountId extends AbstractCommand {
    @Override
    public String execute(ServerRequest serverRequest) {
        return String.valueOf(db.getUserId(serverRequest.getMail(), serverRequest.getPassword()));
    }
}

package server.commands;

import server.collection.Routes;
import server.database.PostgreSQLDatabase;
import server.util.ServerRequest;

import java.util.ArrayList;

public abstract class AbstractCommand {
    public String syntax;
    public String helpText;
    public String arg;
    public PostgreSQLDatabase db;
    public Routes routes;
    public static String history = "Команды, выполненные зарегистрированными пользователями: \n";

    public static ArrayList<AbstractCommand> commands = new ArrayList<>();


    public abstract String execute(ServerRequest serverRequest);


    public static void initCommand(String syntax, String helpText, PostgreSQLDatabase db, Routes routes, AbstractCommand e){
        AbstractCommand command = e;
        command.helpText = helpText;
        command.syntax = syntax;
        command.db = db;
        command.routes = routes;
        commands.add(command); //Добавили команду в список команд
    }

    public static AbstractCommand getCommand(String name) throws NoSuchFieldException{
        for (AbstractCommand command : commands) {
            if (command.syntax.equals(name))
                return command;
        }
        throw new NoSuchFieldException("Такой команды нет");
    }

    public boolean checkLogin(ServerRequest serverRequest){
        if(serverRequest.getPassword().equals("")|| serverRequest.getMail().equals("")){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public String toString() {
        return syntax + " - " + helpText + "\n";
    }
}

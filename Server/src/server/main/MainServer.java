package server.main;

import server.collection.Routes;
import server.commands.AbstractCommand;
import server.commands.specific.*;
import server.database.PostgreSQLDatabase;
import server.interpreter.Interpreter;
import server.util.*;

public class MainServer {

    public static Routes routes = new Routes();

    public static void main(String[] args) throws Exception {

        System.out.println("Сервер стартовал на стандартных настройках.");

        PostgreSQLDatabase db = new PostgreSQLDatabase("jdbc:postgresql://localhost:5432/postgres", "postgres", "123");

        fill(db);
        Interpreter interpreter = new Interpreter(db);
        db.getRoutes(routes);
        interpreter.start();
        ServerStart serverStart = new ServerStart(1332, new RequestHandle() {
            @Override
            public byte[] handleRequest(ServerRequest serverRequest) {
                routes.writeLock.lock();
                System.out.print("\n");
                try {
                    AbstractCommand execute = AbstractCommand.getCommand(serverRequest.getSyntax());
                    if(execute.checkLogin(serverRequest)) {
                        System.out.println("[Server] пришла команда " + serverRequest.getSyntax());
                        AbstractCommand.history += "[" + serverRequest.getMail() + "]" + " - " + execute.syntax + "\n";
                    }
                    routes.writeLock.unlock();
                    return ("[Ответ от сервера] " + execute.execute(serverRequest)).getBytes();
                } catch (NoSuchFieldException e) {
                    routes.writeLock.unlock();
                    return ("[Ответ от сервера] Такой команды нет").getBytes();
                } catch (NumberFormatException e) {
                    routes.writeLock.unlock();
                    return ("[Ответ от сервера] Скорее всего вы ввели неправильно аргумент").getBytes();
                } catch (IndexOutOfBoundsException e){
                    routes.writeLock.unlock();
                    return ("[Ответ от сервера] Вы ввели недостаточное кол-во аргументов").getBytes();
                }
            }
        });
        serverStart.start();
    }

    public static void fill(PostgreSQLDatabase db) {
        AbstractCommand.initCommand("add", "Добавить элемент в коллекцию", db, routes, new Add());
        AbstractCommand.initCommand("help", "Показать все команды", db, routes, new Help());
        AbstractCommand.initCommand("update_id", "Обновить заданный ID", db, routes, new UpdateId());
        AbstractCommand.initCommand("info", "Показать общее кол-во элементов в коллекции, а так же кол-во ваших элементов", db, routes, new Info());
        AbstractCommand.initCommand("remove_by_id", "Удалить элемент по его ID", db, routes, new RemoveById());
        AbstractCommand.initCommand("clear", "Удалить все элементы из коллекции", db, routes, new Clear());
        AbstractCommand.initCommand("show", "Показать все элементы коллекции", db, routes, new Show());
        AbstractCommand.initCommand("remove_greater", "Удалить все элементы выше заданного", db, routes, new RemoveGreater());
        AbstractCommand.initCommand("register", "Зарегистрироваться как новый пользователь", db, routes, new Register());
        AbstractCommand.initCommand("history", "Показать выполненные команды", db, routes, new History());
        AbstractCommand.initCommand("about", "Показать информацию о проекте", db, routes, new About());
        AbstractCommand.initCommand("average_of_distance", "Получить среднее значение Distance", db, routes, new AverageOfDistance());
        AbstractCommand.initCommand("clean_history", "Почистить историю команд", db, routes, new CleanHistory());
        AbstractCommand.initCommand("get_id", "Позволяет узнать ID аккаунта, с которого вы зашли", db, routes, new GetAccountId());
    }
}

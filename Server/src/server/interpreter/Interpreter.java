package server.interpreter;

import server.commands.AbstractCommand;
import server.database.PostgreSQLDatabase;
import server.util.ServerRequest;

import java.util.Scanner;

public class Interpreter extends Thread{

    private PostgreSQLDatabase db;

    private static String entered;
    private static String login = "";
    private static String password = "";
    private ServerRequest serverRequest;
    public Interpreter(PostgreSQLDatabase db){
        this.db = db;
        System.out.println("Сервер готов к вводу комманд.");
    }

    @Override
    public void run() {
        enterCommands();
    }

    public void enterCommands(){
        Scanner scanner = new Scanner(System.in);
        String entered;
        while(true){
            System.out.print("# ");
            entered = scanner.nextLine();
            if(entered.equals("login")) {
                login();
                continue;
            }
            try{
                serverRequest = new ServerRequest(entered.split(" ")[0], entered.split(" ")[1]);
                serverRequest.setMail(login);
                serverRequest.setPassword(password);
            }catch (ArrayIndexOutOfBoundsException e){
                serverRequest = new ServerRequest(entered.split(" ")[0], "");
                serverRequest.setMail(login);
                serverRequest.setPassword(password);
            }
            try{
                AbstractCommand execute = AbstractCommand.getCommand(serverRequest.getSyntax());
                System.out.println(execute.execute(serverRequest));
            } catch (NoSuchFieldException e) {
                System.out.println("Такой команды нет");
            } catch (NumberFormatException e) {
                System.out.println("Скорее всего вы ввели неправильно аргумент");
            } catch (IndexOutOfBoundsException e){
                System.out.println("Вы ввели недостаточное кол-во аргументов");
            }
        }
    }

    public static void login(){
        System.out.print("Введите логин: ");
        Scanner scanner = new Scanner(System.in);
        login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        password = scanner.nextLine();
    }

    public PostgreSQLDatabase getDb() {
        return db;
    }
}

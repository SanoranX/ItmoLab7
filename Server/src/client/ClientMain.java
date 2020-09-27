package client;

import server.util.ServerRequest;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {

    private static String entered;
    private static String login = "";
    private static String password = "";

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Войдите в свой аккаунт с помощью команды: login");
        while(true){
            System.out.print("Введите команду: ");
            entered = scanner.nextLine();
            if(entered.equals("login")) {
                login();
                continue;
            }
            try{
                ServerRequest serverRequest = new ServerRequest(entered.split(" ")[0], entered.split(" ")[1]);
                serverRequest.setMail(login);
                serverRequest.setPassword(password);
                sendDataCheck(serverRequest);
            }catch (ArrayIndexOutOfBoundsException e){
                ServerRequest serverRequest = new ServerRequest(entered.split(" ")[0], "");
                serverRequest.setMail(login);
                serverRequest.setPassword(password);
                sendDataCheck(serverRequest);
            }
        }
    }


    private static void sendDataCheck(ServerRequest serverRequest){
        try{
            sendData(serverRequest);
        }catch (IOException e){
            System.out.println("[Служебная информация] Сервер либо выключен, либо недоступен!");
        }
    }
    private static void sendData(ServerRequest serverRequest) throws IOException {
        Socket outcoming = new Socket(InetAddress.getLocalHost(), 1332);
        ObjectOutputStream outputStream = new ObjectOutputStream(outcoming.getOutputStream());
        outputStream.writeObject(serverRequest);
        outputStream.flush();

        InputStreamReader inputStreamReader = new InputStreamReader(outcoming.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);
        char readChar = (char) reader.read();
        StringBuilder builder = new StringBuilder();
        builder.append(readChar);
        while (reader.ready()) {
            readChar = (char) reader.read();
            builder.append(readChar);
        }
        outcoming.close();
        while (reader.ready()) {
            readChar = (char) reader.read();
            builder.append(readChar);
        }
        outcoming.close();
        String answer = builder.toString();
        System.out.println(answer);
    }

    public static void login(){
        System.out.print("Введите логин: ");
        Scanner scanner = new Scanner(System.in);
        login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        password = scanner.nextLine();
    }
}

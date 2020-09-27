package server.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ServerThread extends Thread{


    public static final int READ_THREADS = 5; //Максимальное число потоков на чтение
    public static final int EXECUTE_THREADS = 5; //Такая же история

    private final ServerSocket serverSocket;
    private final RequestHandle requestHandle;

    private final ExecutorService readThreadPool = Executors.newFixedThreadPool(READ_THREADS);
    private final ForkJoinPool writeThreadPool = new ForkJoinPool();
    private final ExecutorService handlerExecutor = Executors.newFixedThreadPool(EXECUTE_THREADS);


    public ServerThread(ServerSocket serverSocket, RequestHandle requestHandle){
        this.serverSocket = serverSocket;
        this.requestHandle = requestHandle;
    }


    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        while(!thread.isInterrupted()){
            try{
                Socket socketClient = serverSocket.accept();
                CompletableFuture
                        .supplyAsync(readRequestMessage(socketClient), readThreadPool) // read bytes
                        .thenApplyAsync((requestHandle::handleRequest),handlerExecutor) // process request
                        .thenAcceptAsync(writeResponseMessage(socketClient), writeThreadPool).thenRun( () -> closeSocket(socketClient));
            }catch (IOException e){
                System.out.println("Приложение выбросило исклюючение.");
                e.printStackTrace();
            }
        }
        releaseResources();
    }

    private void closeSocket(Socket client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseResources() {
        try {
            readThreadPool.shutdown();
            writeThreadPool.shutdown();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Consumer<byte[]> writeResponseMessage(Socket socketClient) {
        return bytes -> {
            try {
                OutputStream outputStream = socketClient.getOutputStream();
                System.out.print("# ");
                outputStream.write(bytes);
                outputStream.flush();
                socketClient.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private Supplier<ServerRequest> readRequestMessage(Socket socketClient) {
        return () -> {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socketClient.getInputStream());
                ServerRequest serverRequest = (ServerRequest) objectInputStream.readObject();
                socketClient.shutdownInput();
                return serverRequest;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    static final class ThreadPerTaskExecutor implements Executor {
        public void execute(Runnable r) { new Thread(r).start(); }
    }
}

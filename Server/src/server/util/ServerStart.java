package server.util;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerStart {

    private final Thread serverThread;

    public ServerStart(int port, RequestHandle requestHandle) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        this.serverThread = new ServerThread(serverSocket, requestHandle);
    }

    public void start() {
        this.serverThread.start();
    }

    public void stop(){
        this.serverThread.stop();
    }
}

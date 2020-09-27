package server.util;


public interface RequestHandle {

    byte[] handleRequest(ServerRequest serverRequest);
}

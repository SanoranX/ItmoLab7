package server.database;

import server.collection.Route;
import server.collection.Routes;

public interface Database extends AutoCloseable{
    void add(Route route, int userId);
    void remove(int id, int userId);
    int count();
    void removeUsersElement(int userId);
    void getRoutes(Routes routes);
    void removeGreater(int id, int userId);
    void addUser(String email, String userPassword);
    boolean checkUser(String email, String userPassword);
    int getUserId(String email, String passwordHash);
    boolean checkUsername(String email);
    boolean checkPassword(String email, String password);
    Float getDistance();
}

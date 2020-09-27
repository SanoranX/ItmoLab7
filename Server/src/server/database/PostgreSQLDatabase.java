package server.database;

import server.collection.Route;
import server.collection.Routes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class PostgreSQLDatabase implements Database{

    private String uri;
    private String user;
    private String password;
    private final String SALT = "ITMOisBEST";

    public PostgreSQLDatabase(String uri, String user, String password) throws SQLException, ClassNotFoundException {
        this.uri = uri;
        this.user = user;
        this.password = password;
        Class.forName("org.postgresql.Driver");

        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS lab7_users (" +
                            "id SERIAL," +
                            "email VARCHAR NOT NULL," +
                            "password VARCHAR NOT NULL)"
            );
            statement.execute();
            statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS lab7 (" +
                            "id SERIAL," +
                            "road_name VARCHAR NOT NULL," +
                            "c_x DOUBLE PRECISION NOT NULL," +
                            "c_y DOUBLE PRECISION NOT NULL," +
                            "l_x FLOAT NOT NULL," +
                            "l_y INTEGER NOT NULL," +
                            "distance FLOAT NOT NULL," +
                            "user_id INTEGER NOT NULL)");
            statement.execute();
        }

    }

    @Override
    public void add(Route route, int userId) {
        System.out.println("[PostgreSQL] Запрос на добавление элемента от пользователя " + userId);
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO lab7 " +
                    "(road_name, c_x, c_y, l_x, l_y, distance, user_id)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, route.getName());
            statement.setDouble(2, route.getCoordinates().getX());
            statement.setDouble(3, route.getCoordinates().getY());
            statement.setFloat(4, route.getFrom().getX());
            statement.setInt(5, route.getFrom().getY());
            statement.setFloat(6, route.getDistance());
            statement.setInt(7, userId);
            statement.execute();
            System.out.println("[PostgreSQL] Success");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[PostgreSQL] Error");
        }
    }

    @Override
    public void remove(int id, int userId) {
        System.out.println("[PostgreSQL] Запрос на удаление элемента от пользователя #" + userId);
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM lab7 WHERE " +
                            "id = ? AND " +
                            "user_id = ?");
            statement.setInt(1, id);
            statement.setInt(2, userId);
            statement.execute();
            System.out.println("[PostgreSQL] Success");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[PostgreSQL] Error");
        }
    }

    @Override
    public int count() {
        System.out.println("[PostgreSQL] Пришел запрос на подсчет элементов коллекции");
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            Statement stmt = connection.createStatement();
            String query = "SELECT COUNT(*) FROM lab7";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[PostgreSQL] Error");
            return -1;
        }
    }

    @Override
    public void removeUsersElement(int userId) {
        System.out.println("[PostgreSQL] Пришел запрос на удаление элемента коллекции");
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM lab7 WHERE " +
                            "user_id = ?");
            statement.setInt(1, userId);
            statement.execute();
            System.out.println("[PostgreSQL] Success");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[PostgreSQL] Error");
        }
    }

    @Override
    public void getRoutes(Routes routes) {
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM lab7");
            ResultSet rs = statement.executeQuery();
            routes.clear();
            while (rs.next()) {
                routes.add(new Route(rs.getInt("id"), rs.getString("road_name"), (Double) rs.getDouble("c_x"), (Double) rs.getDouble("c_y"), (Float) rs.getFloat("l_x"), (Integer) rs.getInt("l_y"), rs.getFloat("distance"), rs.getInt("user_id")));
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }

    @Override
    public void removeGreater(int id, int userId) {
        System.out.println("[PostgreSQL] Пришел запрос на удаление элементов больше чем заданный аргумент");
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM lab7 WHERE " +
                            "id > ? AND " +
                            "user_id = ?");
            statement.setInt(1, userId);
            statement.setInt(2, id);
            statement.execute();
            System.out.println("[PostgreSQL] Success");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[PostgreSQL] Error");
        }
    }

    @Override
    public void addUser(String email, String userPassword) {
        System.out.println("[PostgreSQL] Запрос на регистрацию от " + email);
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO lab7_users " +
                    "(email, password)" +
                    "VALUES (?, ?)");

            String hashedPassword = getHash(userPassword, SALT);
            statement.setString(1, email);
            statement.setString(2, hashedPassword);
            statement.execute();
            System.out.println("[PostgreSQL] Success");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[PostgreSQL] Error");
        }
    }

    @Override
    public boolean checkUser(String email, String userPassword) {
        System.out.println("[PostgreSQL] Проверка пользователя " + email);
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM lab7_users WHERE email = ?");
            statement.setString(1, email);
            statement.execute();
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if(rs.getString("password").equals(getHash(userPassword, SALT)))
                    return true;
                else
                    return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[PostgreSQL] Error");
        }
        return false;
    }

    @Override
    public int getUserId(String email, String passwordHash) {
        System.out.println("[PostgreSQL] Возвращаем id аккаунта " + email);
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM lab7_users WHERE " +
                    "email = ? AND " +
                    "password = ?");
            statement.setString(1, email);
            statement.setString(2, getHash(passwordHash, SALT));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                System.out.println("[PostgreSQL] Success");
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("[PostgreSQL] Error");
        return -1;
    }

    @Override
    public Float getDistance() {
        System.out.println("[PostgreSQL] Возвращаем distance пользователю");
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM lab7");
            ResultSet rs = statement.executeQuery();
            float res = 0;
            while (rs.next()) {
                res = res + rs.getFloat("distance");
                return res;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Float.NaN;
        }
        return Float.NaN;
    }

    @Override
    public boolean checkUsername(String email) {
        System.out.println("[PostgreSQL] Проверяем наличие аккаунта " + email);
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(id) FROM lab7_users WHERE " +
                    "email = ?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                boolean result = rs.getInt(1) != 0;
                System.out.println(result);
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[PostgreSQL] Error");
        }
        return false;
    }

    @Override
    public boolean checkPassword(String email, String pass) {
        System.out.println("[PostgreSQL] Проверяем пароль у пользователя " + email);
        try (Connection connection = DriverManager.getConnection(uri, user, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(id) FROM lab7_users WHERE " +
                    "email = ?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            boolean result;
            if (rs.next()) {
                if(pass == rs.getString("password"))
                    result = true;
                else
                    result = false;
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[PostgreSQL] Error");
        }
        return false;
    }

    @Override
    public void close() throws Exception {

    }

    public String getHash(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
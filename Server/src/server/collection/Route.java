package server.collection;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Route {
    private Scanner scanner = new Scanner(System.in);
    private int id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически +
    private String name; //Поле не может быть null, Строка не может быть пустой +
    private Coordinates coordinates = new Coordinates(1); //Поле не может быть null +
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private LocationFrom from = new LocationFrom(1); //Поле не может быть null +
    private Float distance; //Поле не может быть null, Значение поля должно быть больше 1 +
    private int creatorId; //Поле не может быть null.

    public Route(int parID, String parName, Double parCoordinatesX, Double parCoordinatesY, Float parLocationX, Integer parLocationY, Float parDestination, int userid) {
        id = parID;
        name = parName;
        coordinates.setX(parCoordinatesX);
        coordinates.setY(parCoordinatesY);
        from.setX(parLocationX);
        from.setY(parLocationY);
        distance = parDestination;
        creatorId = userid;
    }


    public Route(int id, String arg){
        this.creationDate = LocalDateTime.now();
        this.id = Integer.parseInt(arg.split(",")[0]);
        name = arg.split(",")[0];
        coordinates.setX(Double.parseDouble(arg.split(",")[1]));
        coordinates.setY(Double.parseDouble(arg.split(",")[2]));
        from.setX(Float.parseFloat(arg.split(",")[3]));
        from.setY(Integer.parseInt(arg.split(",")[4]));
        distance = Float.parseFloat(arg.split(",")[5]);
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getId() {
        return id;
    }

    public Float getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "\n==================\nНазвание дороги: " + name + "\nID дороги: " + id + "\nDistance " + distance + "\nCoordinates X: " + coordinates.getX() + "\nCoordinates Y: " + coordinates.getY() + "\nLocation X: " + from.getX() + "\nLocation Y: " + from.getY() +  "\nСоздал пользователь: [" + creatorId + "]" + "\n" + "==================";
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocationFrom getFrom() {
        return from;
    }
}

package myArchitecture;

import java.awt.*;

public class Appartment extends Building{
    private int rooms;
    private int floor;

    public Appartment(Point coordinates, String name, int height, int rooms) {
        super(coordinates, name, height);
        this.rooms = rooms;
    }

    public int getRooms() {
        return rooms;
    }

    public int getFloor() {
        return floor;
    }
}

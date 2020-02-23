package myArchitecture;

import java.awt.*;

public class Cottage extends Building{
    private int rooms;
    private int floors;
    private Garage garage;

    public Cottage(Point coordinates, String name, int height, int rooms, int floors, Garage garage) {
        super(coordinates, name, height);
        this.rooms = rooms;
        this.floors = floors;
        this.garage = garage;
    }

    public int getRooms() {
        return rooms;
    }

    public int getFloors() {
        return floors;
    }

    public Garage getGarage() {
        return garage;
    }
}

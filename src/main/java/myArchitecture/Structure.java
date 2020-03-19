package myArchitecture;

import java.awt.*;
import java.io.Serializable;

public class Structure implements Serializable{
    private int coordinatex;
    private int coordinatey;
    public String name;


    public Point getCoordinates() {
        return new Point(coordinatex, coordinatey);
    }

    public Structure() {
    }

    public Structure(int coordinatex, int coordinatey, String name) {
        this.coordinatex = coordinatex;
        this.coordinatey = coordinatey;
        this.name = name;
    }

    public void move(Point newCoords)
    {
        this.coordinatex=newCoords.x;
        this.coordinatey=newCoords.y;
    }

    public Structure(Point coordinates, String name) {
        this.coordinatex=coordinates.x;
        this.coordinatey=coordinates.y;
        this.name = name;

    }

    @Override
    public String toString() {
        return  name;

    }
}

package myArchitecture;

import java.awt.*;

public class Structure {
    private int coordinatex;
    private int coordinatey;
    public String name;


    public Point getCoordinates() {
        return new Point(coordinatex, coordinatey);
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

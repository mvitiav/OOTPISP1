package myArchitecture;

import java.awt.*;

public class Structure {
    private Point coordinates;
    public String name;

    public Point getCoordinates() {
        return coordinates;
    }

    public void move(Point newCoords)
    {
        this.coordinates=newCoords;
    }

    public Structure(Point coordinates, String name) {
        this.coordinates = coordinates;
        this.name = name;

    }
}

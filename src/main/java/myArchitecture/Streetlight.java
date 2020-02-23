package myArchitecture;

import java.awt.*;

public class Streetlight extends NonBuilding{
    private static int id=0;
    private int power;

    public Streetlight(Point coordinates, String name, Boolean powered, int power) {
        super(coordinates, name, powered);
        this.power = power;
    }

    public static int getId() {
        return id;
    }

    public int getPower() {
        return power;
    }
    public void reSetId()
    {
        id=0;
    }
}

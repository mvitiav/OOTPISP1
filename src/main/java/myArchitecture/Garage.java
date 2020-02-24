package myArchitecture;

import java.awt.*;

public class Garage extends Building{
    private int maxCars;
    private Gate gate;

    public Garage(Point coordinates, String name, int height, int maxCars) {
        super(coordinates, name, height);
        this.maxCars = maxCars;
        this.gate=new Gate(this.getCoordinates(),this.name+"'s gate",true,1);
    }

    public int getMaxCars() {
        return maxCars;

    }

    public void setMaxCars(int maxCars) {
        this.maxCars = maxCars;
    }
}

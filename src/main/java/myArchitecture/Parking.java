package myArchitecture;

import java.awt.*;
import java.io.Serializable;

public class Parking extends Building{
    private double price;

    public Parking(Point coordinates, String name, int height, double price) {
        super(coordinates, name, height);
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

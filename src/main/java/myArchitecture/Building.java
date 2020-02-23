package myArchitecture;

import java.awt.*;

public class Building extends Structure {
    private int height;
    private int wallsCount;

    public Building(Point coordinates, String name, int height) {
        super(coordinates, name);
        this.height = height;
        this.wallsCount = 4;
    }

    public int getHeight() {
        return height;
    }

    public double calcArea(Point a, Point b)
    {
        return (  (a.x-b.x)*(a.y-b.y) < 0 ? -  (a.x-b.x)*(a.y-b.y) :   (a.x-b.x)*(a.y-b.y));
    }

    public void addWall()
    {
        this.wallsCount++;
    }

    public void breakWall() throws Exception {
        if(wallsCount>1)
        {
            this.wallsCount--;
        }
        else
            {
                throw new Exception("Not Enough walls");
            }
    }


}

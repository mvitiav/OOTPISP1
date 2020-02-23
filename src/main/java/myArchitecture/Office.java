package myArchitecture;

import java.awt.*;

public class Office extends Building{
    private int workplaces;

    public Office(Point coordinates, String name, int height, int workplaces) {
        super(coordinates, name, height);
        this.workplaces = workplaces;
    }

    public int getWorkplaces() {
        return workplaces;
    }

    public void setWorkplaces(int workplaces) {
        this.workplaces = workplaces;
    }
}

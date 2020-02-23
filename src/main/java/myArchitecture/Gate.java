package myArchitecture;

import java.awt.*;

public class Gate extends NonBuilding {
    private int thickness;

    public Gate(Point coordinates, String name, Boolean powered, int thickness) {
        super(coordinates, name, powered);
        this.thickness = thickness;
    }

    public int getThickness() {
        return thickness;
    }
}

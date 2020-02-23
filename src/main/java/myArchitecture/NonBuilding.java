package myArchitecture;

import java.awt.*;

public class NonBuilding extends Structure{
    private Boolean powered = false;

    public NonBuilding(Point coordinates, String name, Boolean powered) {
        super(coordinates, name);
        this.powered = powered;
    }

    public void setPowered(Boolean powered) {
        this.powered = powered;
    }

    public Boolean isPowered() {
        return powered;
    }

    public void switchPowered()
    {
        this.powered=!this.powered;
    }
}

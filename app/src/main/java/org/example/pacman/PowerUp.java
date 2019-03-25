package org.example.pacman;

import android.content.Context;
import android.widget.TextView;

public class PowerUp {
    private Context context;
    //a reference to the gameview
    private GameView gameView;
    private int powerx;
    private int powery;
    private boolean powerIsTaken;

    public PowerUp( int powerx, int powery){
        this.powerx = powerx;
        this.powery = powery;
    }

    public boolean PowerIsTaken() {
        return powerIsTaken;
    }
    public void SetPowerIsTaken(boolean powerIsTaken) {
        this.powerIsTaken = powerIsTaken;
    }
    public int getPowerx() {
        return powerx;
    }
    public int getPowery() {
        return powery;
    }
}

package org.example.pacman;

public class EvilPacMan {

    private int evilPacX, evilPacY;
    private boolean isSet = false;
    private boolean moving = true;

    public EvilPacMan(int evilPacx, int evilPacy){
        this.evilPacX = evilPacx;
        this.evilPacY = evilPacy;
    }

    public int getEvilPacX(){
        return evilPacX;
    }
    public int getEvilPacY(){
        return evilPacY;
    }
    public int setEvilPacX(int value) {
        return evilPacX = value;
    }
    public int setEvilPacY(int value) {
        return evilPacY = value;
    }

    public boolean IsSet(){
        return isSet;
    }
    public boolean SetEnemy(boolean isSet){
        return this.isSet = isSet;
    }

    public boolean isMoving() {
        return moving;
    }
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

}

package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
private Context context;
private int coinx, coiny;
//a reference to the gameview
private GameView gameView;
private int h,w; //height and width of screen
private boolean isTaken = false;

public GoldCoin( int coinx, int coiny){
    this.coinx = coinx;
    this.coiny = coiny;
}

public int getCoinx(){
    return coinx;
}
public int getCoiny(){
    return coiny;
}
public boolean IsTaken(){
    return isTaken;
}
public boolean SetTaken(boolean isTaken){
    return this.isTaken = isTaken;
}
}

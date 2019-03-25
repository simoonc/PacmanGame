package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.constraint.solver.widgets.Rectangle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Random;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private int points = 0; //how points do we have
    //textview reference to points
    private TextView  pointsView;
    //a reference to the gameview & coin
    private GameView gameView;
    //height and width of screen
    private int h,w;
    private Random rand = new Random();

    //** PacMan **//
    //bitmap of the pacman & coin & EvilPacMan
    private Bitmap pacBitmap;
    public int pacx, pacy;
    //Pacman speed
    private int pacSpeed =10;
    //Pacman directions
    private int up = 0;
    private int right = 1;
    private int down = 2;
    private int left = 3;
    private int nextDirection;

    //** Gold Coin **//
    private Boolean initCoins = false;
    private Bitmap coinBitMap;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    private int coinsInMap = 10;

    //** Power Up **//
    private Bitmap powerUpBitMap;
    private boolean initPowers = false;
    private TextView  powerView;
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private int powerUpsInMap = 2;

    //** Evil pac **//
    private Boolean initEnemies = false;
    private Bitmap evilPacBitMap;
    //the list of enimies - initially empty
    private ArrayList<EvilPacMan> enemies = new ArrayList<>();
    private final int enemiesInMap = 2;
    private int ghostDirection;
    int enemySpeed = 10;

    // Is the game running?
    private boolean running = false;

    public Game(Context context, TextView view, TextView power)
    {

        this.context = context;
        this.pointsView = view;
        this.powerView = power;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        evilPacBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.evilpac);
        powerUpBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.flash);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void initCoins(){
        for(int i = 0; i < coinsInMap; i++)
        {
            coins.add(new GoldCoin(
                    rand.nextInt(gameView.w - coinBitMap.getWidth()),
                    rand.nextInt(gameView.h - coinBitMap.getHeight() )
            ));
        }
    }
    public void initpowerUps(){
        for(int i = 0; i < powerUpsInMap; i++)
        {
            powerUps.add(new PowerUp(
                    rand.nextInt(gameView.w - powerUpBitMap.getWidth()),
                    rand.nextInt(gameView.h - powerUpBitMap.getHeight() )
            ));
        }
    }

    public void initEnemies(){
        for(int i = 0; i < enemiesInMap; i++)
        {
            enemies.add(new EvilPacMan(
                    rand.nextInt(gameView.w - evilPacBitMap.getWidth()),
                    rand.nextInt(gameView.h - evilPacBitMap.getHeight() )
            ));
        }
    }

    public void newGame()
    {
        //just some starting coordinates for pacman
        pacx = 50;
        pacy = 400;

        //reset the points
        points = 0;
        pointsView.setText("Points: " + points);
        setPacSpeed(10);
        powerView.setText("Power up: No power");
        initEnemies = false;

        for(EvilPacMan item : enemies){
            item.SetEnemy(true);

        }
        initCoins = false;
        for(GoldCoin item : coins){
            item.SetTaken(true);

        }
        initPowers = false;
        for(PowerUp item : powerUps){
            item.SetPowerIsTaken(true);

        }
        SetRunning(false);
        gameView.invalidate(); //redraw screen
    }

    public void win(){
        CharSequence text = "You won\n" +  "You got " + getPoints() + " points";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        resetGame();
    }
    public void loose(){
        CharSequence text = "You lost\n" +  "You got " + getPoints() + " points";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        resetGame();
    }
    public void resetGame() {
        newGame();
    }
    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void movePacmanUp()
    {
        nextDirection = up; // 0
    }
    public void movePacmanRight()
    {
        nextDirection = right; // 1
    }
    public void movePacmanDown()
    {
        nextDirection = down;// 2
    }
    public void movePacmanLeft()
    {
        nextDirection = left; // 3
    }


    public void doCollisionCheck()
    {

        /*
         * Note til mig selv.
         * Der findes mange måder at lave collision detection.
         * både med pixel perfect
         * Rectangle collision detection
         * Circular collision detection
         * Box collision detection
         * */
        // This is the collision detection for the goldcoins
        for(GoldCoin item : coins) {
            int dx = item.getCoinx() - getPacx();
            int dy = item.getCoiny() - getPacy();
            double d = Math.sqrt((dx * dx) + (dy * dy));
            int r1 = pacBitmap.getHeight() + pacBitmap.getWidth() - 240;
            int r2 = coinBitMap.getHeight() + coinBitMap.getWidth() - 240;
            if (d <= r1+r2 && !item.IsTaken() ){
                points++;
                item.SetTaken(true);
                pointsView.setText("Points " + getPoints());
            }
        }

        //This is the collision detection for powerUps
        for(PowerUp item : powerUps) {
            int dx = item.getPowerx() - getPacx();
            int dy = item.getPowery() - getPacy();
            double d = Math.sqrt((dx * dx) + (dy * dy));
            int r1 = pacBitmap.getHeight() + pacBitmap.getWidth() - 240;
            int r2 = powerUpBitMap.getHeight() + powerUpBitMap.getWidth() - 240;
            if (d <= r1+r2 && !item.PowerIsTaken() ){
                item.SetPowerIsTaken(true);
                setPacSpeed(20);
            }
        }
    }

    public void pacDeath(){
        for(EvilPacMan enemy : enemies){
            int dx = enemy.getEvilPacX() - getPacx();
            int dy = enemy.getEvilPacY() - getPacy();
            double d = Math.sqrt((dx * dx) + (dy * dy));
            int r1 = pacBitmap.getHeight() + pacBitmap.getWidth() - 265;
            int r2 = evilPacBitMap.getHeight() + evilPacBitMap.getWidth() - 265;
            if (d <= r1+r2 && !enemy.IsSet()){
                CharSequence text = "Game over\n" +  "You got " + getPoints() + " points";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                resetGame();
            }
        }
    }

    public void moveEnemies(){
        for(EvilPacMan enemy : enemies){
           // Random randDir = new Random(System.nanoTime() - 500);
            Random randDir = new Random(System.currentTimeMillis() - 500);
            ghostDirection = randDir.nextInt(4);

            switch (getGhostDirection()) {
                case 0: // UP
                    if (enemy.getEvilPacY()>=enemySpeed+getEvilPacBitMap().getHeight()-130){
                        enemy.setEvilPacY(enemy.getEvilPacY() - enemySpeed);
                        gameView.invalidate();
                    }

                    break;

                case 1: // RIGHT
                    if (enemy.getEvilPacX() + enemySpeed + getEvilPacBitMap().getWidth() <= gameView.getWidth()) {
                            enemy.setEvilPacX(enemy.getEvilPacX() + enemySpeed);
                        gameView.invalidate();
                    }
                    break;

                case 2: // DOWN
                    if(enemy.getEvilPacY()+enemySpeed+getEvilPacBitMap().getHeight()<=gameView.getHeight()){
                        enemy.setEvilPacY(enemy.getEvilPacY() + enemySpeed);
                        gameView.invalidate();

                    }
                    break;

                case 3: // LEFT

                    if(enemy.getEvilPacX()>=enemySpeed+getEvilPacBitMap().getWidth()-140){
                        enemy.setEvilPacX(enemy.getEvilPacX() - enemySpeed);
                        gameView.invalidate();
                    }
                    break;
            }
        }
    }


    // Pacman related
    public int getPacx()
    {
        return pacx;
    }
    public int getPacy()
    {
        return pacy;
    }
    public int setPacY(int value)
    {
        return pacy = value;
    }
    public int setPacX( int value)
    {
        return pacx = value;
    }
    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }
    public int getDirection(){
        return nextDirection;
    }
    public int getPacSpeed() {
        return pacSpeed;
    }
    public void setPacSpeed(int pacSpeed) {
        this.pacSpeed = pacSpeed;
    }

    // Coin related
    public int getPoints()
    {
        return points;
    }
    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }
    public Bitmap getCoinBitMap() {
        return coinBitMap;
    }
    public boolean InitCoins(){return initCoins;}
    public boolean SetCoins(boolean value) {
        return initCoins = value;
    }

    // Enemy related
    public ArrayList<EvilPacMan>getEnemies(){return enemies;}
    public Bitmap getEvilPacBitMap() {
        return evilPacBitMap;
    }
    public boolean InitEnemies(){return initEnemies;}
    public boolean SetEnemies(boolean value){
        return initEnemies = value;
    }
    public void setGhostDirection(int ghostDirection) {
        this.ghostDirection = ghostDirection;
    }
    public int getGhostDirection() {
        return ghostDirection;
    }

    // Power up related
    public ArrayList<PowerUp> getPowerUps()
    {
        return powerUps;
    }
    public Bitmap getPowerUpBitMap() {
        return powerUpBitMap;
    }
    public boolean InitPowerUps(){return initPowers;}
    public boolean SetPowerUps(boolean value) {
        return initPowers = value;
    }

    // Game related
    public boolean IsRunning() {
        return running;
    }
    public void SetRunning(boolean running) {
        this.running = running;
    }


}

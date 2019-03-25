package org.example.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    //reference to the main view
    GameView gameView;
    //reference to the game class.
    Game game;
    PowerUp powerUp;
    // TIMERS vv
    private Timer pacTimer;
    private Timer lvlTimer;
    private Timer enemyTimer;
    private Timer directionTimer;
    // Counter which counts when the level ends
    private int lvlCounter = 30;
    //Speed for the pacman
    private int speed = 10;
    private int powerUpCounter;
    //Counter which resets EvilPacMan dir
    private int refreshCounter = 7;
    TextView textViewPoints;
    TextView textViewTicker;
    TextView textViewPower;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView =  findViewById(R.id.gameView);
        textViewPoints = findViewById(R.id.points);
        textViewTicker = findViewById(R.id.ticker);
        textViewPower = findViewById(R.id.power);

        findViewById(R.id.startButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);

        Button buttonRight = findViewById(R.id.moveRight);
        Button buttonLeft = findViewById(R.id.moveLeft);
        Button buttonUp = findViewById(R.id.moveUp);
        Button buttonDown = findViewById(R.id.moveDown);

        game = new Game(this,textViewPoints, textViewPower);
        game.setGameView(gameView);
        gameView.setGame(game);
        game.newGame();
        //game.winOrLose();

        buttonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!game.IsRunning()) {
                    game.SetRunning(true);

                }
                game.movePacmanRight();
            }
        });
        buttonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!game.IsRunning()) {
                    game.SetRunning(true);

                }
                game.movePacmanLeft();
            }
        });
        buttonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!game.IsRunning()) {
                    game.SetRunning(true);

                }
                game.movePacmanUp();
            }
        });
        buttonDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!game.IsRunning()) {
                    game.SetRunning(true);

                }
                game.movePacmanDown();
            }
        });

        //make a new timer
        pacTimer = new Timer();
        lvlTimer = new Timer();
        enemyTimer = new Timer();
        directionTimer = new Timer();
        //should the game be running?
        game.SetRunning(false);

        pacTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Move();
            }

        }, 0, 25); //0 indicates we start now, 25
        //is the number of miliseconds between each call

        enemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                EnemyMove();
            }

        }, 0, 20);

        directionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DiectionTimer(); //TODO LAV DEN NIYE DIR
            }
        }, 300, 500);

        lvlTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //just to make sure if the app is killed, that we stop the timer.
        pacTimer.cancel();
        lvlTimer.cancel();
        enemyTimer.cancel();
    }
    private void Move()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(move);

    }
    private void EnemyMove()
    {
        this.runOnUiThread(enemyMove);
    }
    private void DiectionTimer()
    {
        this.runOnUiThread(DirectionTicker);
    }
    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.
        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(LevelTicker);

    }

    private Runnable move = new Runnable() {
        @Override
        public void run() {

            //This method runs in the same thread as the UI.
            // so we can draw

            if (game.IsRunning())
            {

                switch (game.getDirection()) {
                    case 0: // UP
                        if(game.getPacy()>=game.getPacSpeed()+game.getPacBitmap().getHeight()-135){
                            game.setPacY(game.getPacy() - game.getPacSpeed());
                            game.doCollisionCheck();
                            game.pacDeath();
                            gameView.invalidate();
                        }

                        break;

                    case 1: // RIGHT
                        if(game.getPacx()+game.getPacSpeed()+game.getPacBitmap().getWidth()<=gameView.getWidth()){
                            game.setPacX(game.getPacx() + game.getPacSpeed());
                            game.doCollisionCheck();
                            game.pacDeath();
                            gameView.invalidate();
                        }
                        break;

                    case 2: // DOWN
                        if(game.getPacy()+game.getPacSpeed()+game.getPacBitmap().getHeight()<=gameView.getHeight()){
                            game.setPacY(game.getPacy() + game.getPacSpeed());
                            game.doCollisionCheck();
                            game.pacDeath();
                            gameView.invalidate();

                        }
                        break;

                    case 3: // LEFT

                        if(game.getPacx()>=game.getPacSpeed()+game.getPacBitmap().getWidth()-140){
                            game.setPacX(game.getPacx() - game.getPacSpeed());
                            game.doCollisionCheck();
                            game.pacDeath();
                            gameView.invalidate();
                        }
                        break;

                }

            }

        }
    };

    private Runnable DirectionTicker = new Runnable() {
        @Override
        public void run() {
            if (game.IsRunning())
            {
                game.setGhostDirection(game.getGhostDirection());
            }
        }
    };
    private Runnable enemyMove = new Runnable() {
        @Override
        public void run() {
            if (game.IsRunning()) {
                game.moveEnemies();

            }
        }
    };

    private Runnable LevelTicker = new Runnable() {
        @Override
        public void run() {
            if (game.IsRunning())
            {
                // This code checks if the powerup is taken. and if it is
                // It sets a timer that will stop it in a few seconds
                if(game.getPacSpeed() == 20){
                    refreshCounter--;
                    textViewPower.setText("Power up: Super speed " + refreshCounter + "S");
                    if(refreshCounter <=0){
                        game.setPacSpeed(10);
                        textViewPower.setText("Power up: No power");
                        setRefreshCounter(7);
                    }
                }

                //This code checks completion of the level
                lvlCounter--;
                textViewTicker.setText("Time left: "+ lvlCounter);
                if(getLvlCounter() <= 0 && game.getPoints() < 10){
                    lvlCounter= 30;
                    textViewTicker.setText("Time left: "+lvlCounter);
                    game.loose();
                }else if(getLvlCounter()>= 0 && game.getPoints()>=10){
                    setLvlCounter(30);
                    textViewTicker.setText("Time left: "+lvlCounter);
                    game.win();
                }
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this,"New Game",Toast.LENGTH_LONG).show();
            lvlCounter = 30;
            game.SetRunning(false);
            textViewTicker.setText("Timer value: "+lvlCounter);
            game.resetGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.startButton)
        {
            game.SetRunning(true);
        }
        else if (v.getId()==R.id.stopButton)
        {
            game.SetRunning(false);
        }
    }

    public int getRefreshCounter() {
        return refreshCounter;
    }
    public void setRefreshCounter(int refreshCounter) {
        this.refreshCounter = refreshCounter;
    }

    public int getLvlCounter() {
        return lvlCounter;
    }
    public void setLvlCounter(int lvlCounter) {
        this.lvlCounter = lvlCounter;
    }
}

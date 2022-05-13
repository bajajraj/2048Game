package edu.msu.bajajraj.exambajajraj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNewGame(View view){
        getGameView().resetGame();
    }

    public GameView getGameView(){
        return (GameView) findViewById(R.id.gameView);
    }

}
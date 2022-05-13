package edu.msu.bajajraj.exambajajraj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;

import java.util.Random;

public class GameView extends View implements GestureDetector.OnGestureListener{

    /**
     * Keeping track of the score
     */
    private long totalScore = 0;

    /**
     * The size of the puzzle in pixels
     */
    private int puzzleSize;

    /**
     * How much we scale the puzzle pieces
     */
    private float scaleFactor;

    /**
     * Left margin in pixels
     */
    private int marginX;

    /**
     * Top margin in pixels
     */
    private int marginY;

    private GestureDetectorCompat gestureDetectorCompat;

    /**
     * Matrix keeping in the numbers
     */
    private int[][] arrayMatrix = new int[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

    /**
     * Percentage of the display width or height that
     * is occupied by the puzzle.
     */
    final static float SCALE_IN_VIEW = 0.9f;

    /**
     * Paint for filling the area the puzzle is in
     */
    private Paint fillPaint;

    private Paint outlinePaint;

    Context myContext;


    public GameView(Context context) {
        super(context);
        init(null, 0, context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle, context);
    }

    private void init(AttributeSet attrs, int defStyle, Context context) {
        // Create paint for filling the area the puzzle will
        // be solved in.
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xffcccccc);

        // Setting the outline paint
        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setStyle(Paint.Style.FILL);
        outlinePaint.setColor(Color.BLACK);

        myContext = context;

        gestureDetectorCompat = new GestureDetectorCompat(context, this);

        startGame();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        this.invalidate();
        super.onDraw(canvas);

        canvas.drawText(String.valueOf(totalScore), 0, 0, outlinePaint);

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = (int) wid < hit ? wid : hit;

        puzzleSize = (int)(minDim * SCALE_IN_VIEW);

        // Compute the margins so we center the puzzle
        marginX = (wid - puzzleSize) / 2;
        marginY = (hit - puzzleSize) / 2;

        // Draw the score
        String score = getResources().getString(R.string.score);
        canvas.drawText(score, marginX, (float) (marginY/1.5), outlinePaint);
        canvas.drawText(String.valueOf(totalScore), (float) (marginX + puzzleSize/(1.5)),
                (float) (marginY/1.5), outlinePaint);

        //
        // Draw the outline of the puzzle
        //

        canvas.drawRect(marginX, marginY,
                marginX + puzzleSize, marginY + puzzleSize, fillPaint);

        //
        // Draw the tiles in the grid and then add the numbers

        // Finding the length and distance of each box
        float distance = (float) puzzleSize/4;

        // Finding the length and distance of text
        float textDistanceX = distance/2;
        float textDistanceY = distance/2;
        outlinePaint.setTextSize(50f);

        // Loop to draw the boxes
        for (int i = 0; i < 4; i++) {
            int ix = 0;
            for (int j = 0; j < 4; j++) {
                int jx = i;

                // Number of the loop
                int num = arrayMatrix[i][j];

                if (num != 0){

                    Paint cellPaint = setPaintColor(num);

                    // Drawing the grid
                    canvas.drawRect(marginX + ix*distance + 10f, marginY + jx*distance + 10f,
                            marginX + (ix+1)*distance - 10f, marginY + (jx+1)*distance -10f,
                            cellPaint);

                    // Adding the text to the code
                    canvas.drawText(String.valueOf(num), marginX + ix*distance + textDistanceX - 5f,
                            marginY + jx*distance + textDistanceY + 5f, outlinePaint);
                }
                ix += 1;
            }
        }
    }

    /**
     * Sets the color of the box to be made
     * @param num the number in the box
     * @return the color to be set
     */
    private Paint setPaintColor(int num){
        // Colors taken from https://0x0800.github.io/2048-PANTONE/
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(num == 2){
            int i = Color.rgb(238,228,218);
            paint.setColor(i);
        }
        else if (num == 4){
            paint.setColor(Color.rgb(237,224,200));
        }
        else if (num == 8){
            paint.setColor(Color.rgb(242,177,121));
        }
        else if (num == 16){
            paint.setColor(Color.rgb(245,149,99));
        }
        else if (num == 32){
            paint.setColor(Color.rgb(246,124,95));
        }
        else if (num == 64){
            paint.setColor(Color.rgb(246,94,59));
        }
        else if (num == 128){
            paint.setColor(Color.rgb(237,207,114));
        }
        else if (num == 256){
            paint.setColor(Color.rgb(237,200,97));
        }
        else if (num == 512){
            paint.setColor(Color.rgb(237,200,80));
        }
        else if (num == 1024){
            paint.setColor(Color.rgb(237,197,63));
        }
        else if (num == 2048){
            paint.setColor(Color.rgb(237,194,46));
        }
        else {
            paint.setColor(Color.rgb(255,255,255));
        }
        return paint;
    }

    public void resetGame() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                arrayMatrix[i][j] = 0;
            }
        }
        totalScore = 0;
        startGame();
    }



    boolean isGameOver(){
        if(arrayMatrix[0][0]==0)
            return false;
        for(int i=0;i<4;i++) {
            for(int j=1;j<4;j++){
                if(arrayMatrix[i][j]==0 || arrayMatrix[i][j]== arrayMatrix[i][j-1]){
                    return false;
                }
            }
        }
        for(int j=0;j<4;j++) {
            for(int i=1;i<4;i++){
                if(arrayMatrix[i][j]==0 || arrayMatrix[i][j]== arrayMatrix[i-1][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void generateRandomNumber() {
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int x = rand.nextInt(4);
                int y = rand.nextInt(4);
                if (arrayMatrix[x][y] == 0){
                    int randNumber = rand.nextInt(2);
                    if (randNumber == 0){
                        arrayMatrix[x][y] = 4;
                    }
                    else {
                        arrayMatrix[x][y] = 2;
                    }
                    return;
                }
            }
        }
    }

    public void startGame(){
        int counter = 0;
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int x = rand.nextInt(4);
                int y = rand.nextInt(4);
                if (arrayMatrix[x][y] == 0){
                    int randNumber = rand.nextInt(2);
                    if (randNumber == 0){
                        arrayMatrix[x][y] = 4;
                    }
                    else {
                        arrayMatrix[x][y] = 2;
                    }
                    counter += 1;
                }
                if (counter == 2){
                    return;
                }
            }
        }
    }

    public void gameOver(){
        Toast.makeText(myContext, R.string.game_over, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return onMove(event);
    }

    public boolean onMove(MotionEvent event){

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                Log.i("onTouchEvent", "ACTION_UP");
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i("onTouchEvent", "ACTION_UP");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.i("onTouchEvent",  "ACTION_MOVE: " + event.getX() + "," + event.getY());
                break;
        }

        return false;
    }

    private void onSwipeUp() {
        if(onUp()){
            generateRandomNumber();
        }
        if(isGameOver()) {
            gameOver();
        }
        this.invalidate();
    }

    private  void onSwipeDown() {
        if (onDown()){
            generateRandomNumber();
        }
        if(isGameOver()) {
            gameOver();
        }
        this.invalidate();
    }

    private  void onSwipeLeft() {
        if (onLeft()){
            generateRandomNumber();
        }
        if(isGameOver()) {
            gameOver();
        }
        this.invalidate();
    }

    private void onSwipeRight() { 
        if(onRight()){
            generateRandomNumber();
        }
        if(isGameOver()) {
            gameOver();
        }
        this.invalidate();
    }

    private boolean onUp(){
        boolean yes = false;
        for(int j=0;j<4;j++)
        {
            for(int i=1;i<4;i++)
            {
                if(arrayMatrix[i][j]==0)
                    continue;
                int x=i-1;
                while(x>=0 && arrayMatrix[x][j]==0)
                    x--;
                if( x==-1 || (arrayMatrix[x][j]!=arrayMatrix[i][j] && (x+1)!=i) ) {
                    arrayMatrix[x + 1][j] = arrayMatrix[i][j];
                    yes=true;
                    arrayMatrix[i][j]=0;
                } else if(arrayMatrix[x][j]==arrayMatrix[i][j]){
                    arrayMatrix[x][j] += arrayMatrix[i][j];
                    yes =true;
                    totalScore = totalScore + (2L*arrayMatrix[i][j]);
                    arrayMatrix[i][j]=0;
                }
            }
        }
        return yes;
    }

    private boolean onDown(){
        boolean yes = false;
        for(int j=0;j<4;j++)
        {
            for(int i=2;i>=0;i--)
            {
                if(arrayMatrix[i][j]==0)
                    continue;
                int x=i+1;
                while(x<=3 && arrayMatrix[x][j]==0)
                    x++;
                if( x==4 || (arrayMatrix[x][j]!= arrayMatrix[i][j] && (x-1)!=i) ) {
                    yes = true;
                    arrayMatrix[x-1][j] = arrayMatrix[i][j];
                    arrayMatrix[i][j]=0;
                } else if(arrayMatrix[x][j]== arrayMatrix[i][j]){
                    yes = true;
                    arrayMatrix[x][j] += arrayMatrix[i][j];
                    totalScore += 2L * arrayMatrix[i][j];
                    arrayMatrix[i][j]=0;
                }
            }
        }
        return yes;
    }
    
    private boolean onRight(){
        boolean yes = false;
        for(int i=0;i<4;i++)
        {
            for(int j=2;j>=0;j--)
            {
                if(arrayMatrix[i][j]==0)
                    continue;
                int x=j+1;
                while(x<=3 && arrayMatrix[i][x]==0)
                    x++;
                if( x==4 || (arrayMatrix[i][x]!= arrayMatrix[i][j] && (x-1)!=j) ) {
                    yes = true;
                    arrayMatrix[i][x-1] = arrayMatrix[i][j];
                    arrayMatrix[i][j]=0;
                } else if(arrayMatrix[i][x]== arrayMatrix[i][j]){
                    yes = true;
                    arrayMatrix[i][x] += arrayMatrix[i][j];
                    totalScore = totalScore + 2L * arrayMatrix[i][j];
                    arrayMatrix[i][j]=0;
                }
            }
        }
        return yes;
    }

    private boolean onLeft(){
        boolean yes = false;
        for (int i=0; i<4;i++){
            for (int j=0;j<4;j++){
                for (int x = j+1;x<4;x++){
                    if (arrayMatrix[i][x] >0){

                        if (arrayMatrix[i][j] == 0){
                            arrayMatrix[i][j] = arrayMatrix[i][x];
                            arrayMatrix[i][x] = 0;

                            j--;
                            yes = true;
                        }else if (arrayMatrix[i][j] == arrayMatrix[i][x]){
                            arrayMatrix[i][j] = arrayMatrix[i][j] * 2;
                            arrayMatrix[i][x] = 0;

                            totalScore = totalScore + arrayMatrix[i][j];
                            yes = true;
                        }

                        break;
                    }
                }
            }
        }
        return yes;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if (isGameOver()){
            return false;
        }
        float deltaX = e2.getX() - e1.getX();
        float deltaY = e2.getY() - e1.getY();
        if(Math.abs(deltaY)>Math.abs(deltaX)) {
            if(deltaY > 0){
                onSwipeDown();
            } else {
                onSwipeUp();
            }
        } else{
            if(deltaX > 0){
                onSwipeRight();
            } else {
                onSwipeLeft();
            }
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }


}

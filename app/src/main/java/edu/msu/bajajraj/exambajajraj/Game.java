package edu.msu.bajajraj.exambajajraj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Game {
    /**
     * Percentage of the display width or height that
     * is occupied by the puzzle.
     */
    final static float SCALE_IN_VIEW = 0.9f;

    /**
     * Paint for filling the area the puzzle is in
     */
    private Paint fillPaint;

    /**
     * Paint for outlining the area the puzzle is in
     */
    private Paint outlinePaint;

    public Game(Context context) {
        // Create paint for filling the area the puzzle will
        // be solved in.
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xffcccccc);
    }

    public void draw(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        int puzzleSize = (int)(minDim * SCALE_IN_VIEW);

        // Compute the margins so we center the puzzle
        int marginX = (wid - puzzleSize) / 2;
        int marginY = (hit - puzzleSize) / 2;

        //
        // Draw the outline of the puzzle
        //

        canvas.drawRect(marginX, marginY,
                marginX + puzzleSize, marginY + puzzleSize, fillPaint);
    }

}

package coms486.jsward.platformracer.display;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import coms486.jsward.platformracer.game.PlatformLevel;

public class DrawLevel implements Drawable {

    private static final String DEBUG_TAG = "DRAW_LEVEL";

    private PlatformLevel platformLevel;
    private Paint terrainPaint;

    private Matrix viewMatrix;
    private RectF viewBox;
    private RectF levelBox;
    private RectF displayWindow;

    public DrawLevel(PlatformLevel platformLevel){
        this.platformLevel = platformLevel;

        terrainPaint = new Paint();
        terrainPaint.setColor(Color.GREEN);
        viewMatrix = new Matrix();

        levelBox = new RectF(0, 0, 500, 25);
        viewBox = new RectF(0, 0, 50, 25);
        displayWindow = new RectF();
    }



    @Override
    public void draw(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        displayWindow.set(0, 0, w, h);

        if (viewMatrix.setRectToRect(viewBox, displayWindow, Matrix.ScaleToFit.FILL)) {
            float[] pts1 = {0,0};
            float[] pts2 = {1, 50};
            float[] pts3 = {10,4};
            float[] pts4 = {12, 5};
            viewMatrix.mapPoints(pts1);
            viewMatrix.mapPoints(pts2);
            viewMatrix.mapPoints(pts3);
            viewMatrix.mapPoints(pts4);

            canvas.drawRect(pts1[1], pts1[0], pts2[1], pts2[0],terrainPaint);
            canvas.drawRect(pts3[1], pts3[0], pts4[1], pts4[0],terrainPaint);


        } else {
            Log.d(DEBUG_TAG, "Error in DrawLevel: couldn't create transform matrix");
        }



    }

}

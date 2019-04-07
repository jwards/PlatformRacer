package coms486.jsward.platformracer.display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import jsward.platformracer.common.game.PlatformLevel;
import jsward.platformracer.common.game.Player;

public class DrawLevel implements Drawable {

    private static final String DEBUG_TAG = "DRAW_LEVEL";
    public static final int PLAYER_X_OFFSET = 600;

    //hitbox for the current sprite
    //need to tick this if sprite changes/ sprite is scaled differently
    private float[] playerHitbox = {
            33, 14, 60, 14,   //HEAD
            33, 135, 60, 135, //FEET
            22, 31, 22, 109,   //LEFT ARM
            71, 31, 71, 109   //RIGHT ARM
    };
    
    private float[] transformedHitbox = new float[playerHitbox.length];


    private Player player;
    private Paint terrainPaint;

    private ArrayList<Bitmap> playerSprite;
    private int animationFrame;
    private int animationCounter;
    private Matrix playerMatrix;
    private int playerSpriteCx;
    private int playerSpriteCy;

    private Matrix viewMatrix;
    private RectF viewBox;
    private RectF displayWindow;

    private float[] transformedPoints;


    //1080x2076
    public DrawLevel(Player player, ArrayList<Bitmap> playerSprite){
        this.player = player;
        this.playerSprite = playerSprite;

        terrainPaint = new Paint();
        terrainPaint.setColor(Color.GREEN);
        viewMatrix = new Matrix();
        playerMatrix = new Matrix();
        playerSpriteCx = playerSprite.get(0).getWidth()/2;
        playerSpriteCy = playerSprite.get(0).getHeight() / 2;

        viewBox = new RectF(0, 0, 300, 150);
        displayWindow = new RectF(0,0,2076,1080);
        transformedPoints = new float[PlatformLevel.lpts.length];

        getPlayerHitbox();
        for (int i = 0; i < playerHitbox.length; i++) {
            Log.d(DEBUG_TAG, "playerhitbox: " + transformedHitbox[i]);
        }
    }

    public float[] getPlayerHitbox(){
        Matrix matrix = new Matrix();
        matrix.setRectToRect(viewBox, displayWindow, Matrix.ScaleToFit.FILL);
        matrix.invert(matrix);
        matrix.mapPoints(transformedHitbox, playerHitbox);
        return transformedHitbox;
    }


    @Override
    public void draw(Canvas canvas) {
        float[] playerpt = {player.getX(), player.getY()};
        viewMatrix.setRectToRect(viewBox, displayWindow, Matrix.ScaleToFit.FILL);
        viewMatrix.mapPoints(playerpt);
        viewMatrix.postTranslate(-playerpt[0]+PLAYER_X_OFFSET, 0);
        viewMatrix.mapPoints(transformedPoints, PlatformLevel.lpts);
        for (int i = 0; i < transformedPoints.length; i = i + 4) {
            canvas.drawRect(transformedPoints[i], transformedPoints[i+1], transformedPoints[i+2], transformedPoints[i+3], terrainPaint);
        }

        drawPlayer(canvas,playerpt[1]);
    }

    private void drawPlayer(Canvas canvas,float transformedpy){
        playerMatrix.reset();
        if (player.isFalling()) {
            animationFrame = 5;
        } else if (player.isMoving()) {
            animationCounter++;
            if(animationCounter >4){
                animationCounter = 0;
                animationFrame = indexLooper(animationFrame + 1, 1, 3);
            }

            //display player facing in the correct direction
            if (player.getVx() > 0) {
                playerMatrix.setScale(1, 1, playerSpriteCx, playerSpriteCy);
            } else {
                playerMatrix.setScale(-1, 1, playerSpriteCx, playerSpriteCy);
            }
        } else {
            animationFrame = 0;
            animationCounter = 0;
        }
        playerMatrix.postTranslate(PLAYER_X_OFFSET,transformedpy);
        canvas.drawBitmap(playerSprite.get(animationFrame),playerMatrix,null);
    }

    private int indexLooper(int nextIndex,int lowerBound,int upperBound){
        if (nextIndex > upperBound) {
            return lowerBound;
        }
        if (nextIndex < lowerBound) {
            return upperBound;
        }
        return nextIndex;
    }
}

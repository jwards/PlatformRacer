package coms486.jsward.platformracer.display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import jsward.platformracer.common.game.PlatformLevel;
import jsward.platformracer.common.game.Player;

public class DrawLevel implements Drawable {

    private static final String DEBUG_TAG = "DRAW_LEVEL";
    public int PLAYER_X_OFFSET;

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

    private Point displaySize;
    private float scale;

    private Matrix playerScale;

    private Matrix viewMatrix;
    private RectF viewBox;
    private RectF displayWindow;

    private float[] transformedPoints;


    //1080x2076
    public DrawLevel(Player player, ArrayList<Bitmap> playerSprite, Point displaySize){
        this.player = player;
        this.playerSprite = playerSprite;
        this.displaySize = displaySize;


        terrainPaint = new Paint();
        terrainPaint.setColor(Color.GREEN);
        viewMatrix = new Matrix();
        playerMatrix = new Matrix();

        playerSpriteCx = playerSprite.get(0).getWidth()/2;
        playerSpriteCy = playerSprite.get(0).getHeight() / 2;

        //view box in terms of game coords
        viewBox = new RectF(0, 0, 300, 150);

        //display box in terms of display pixels
        displayWindow = new RectF(0,0,displaySize.x,displaySize.y);

        transformedPoints = new float[PlatformLevel.lpts.length];

        getPlayerHitbox();

        for (int i = 0; i < playerHitbox.length; i++) {
            Log.d(DEBUG_TAG, "playerhitbox: " + transformedHitbox[i]);
        }

        //set player x offset
        PLAYER_X_OFFSET = (int)(0.27f * displaySize.x);

        //calculate scale to draw player
        //.1388 is 150/1080 which is the original scale
        scale = (displaySize.y*0.1388f)/150;

        playerScale = new Matrix();
        playerScale.setScale(scale,scale);
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
        playerMatrix.postConcat(playerScale);
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

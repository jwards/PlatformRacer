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
    private ArrayList<Player> otherPlayers;
    private Paint terrainPaint;

    private ArrayList<Bitmap> playerSprite;
    private Matrix playerMatrix;
    private PlayerAnimator playerAnimator;


    private Point displaySize;
    private float scale;

    private Matrix playerScale;

    private Matrix viewMatrix;
    private RectF viewBox;
    private RectF displayWindow;

    private float[] transformedPoints;

    public DrawLevel(Player player,ArrayList<Player> others, ArrayList<Bitmap> playerSprite, Point displaySize){
        this.player = player;
        this.otherPlayers = others;
        this.playerSprite = playerSprite;
        this.displaySize = displaySize;


        terrainPaint = new Paint();
        terrainPaint.setColor(Color.GREEN);
        viewMatrix = new Matrix();
        playerMatrix = new Matrix();

        playerAnimator = new PlayerAnimator(playerSprite, playerSprite.get(0).getWidth() / 2, playerSprite.get(0).getHeight() / 2);

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

        playerAnimator.drawPlayer(player,canvas,playerMatrix,playerScale,PLAYER_X_OFFSET,playerpt[1]);

        if(otherPlayers!=null) {
            for (Player p : otherPlayers) {
                playerpt[0] = p.getX();
                playerpt[1] = p.getY();
                viewMatrix.mapPoints(playerpt);
                //the player matrix is reset each time so we can resuse it
                playerAnimator.drawPlayer(p, canvas, playerMatrix, playerScale,(int) playerpt[0], playerpt[1]);
            }
        }
    }

}

package coms486.jsward.platformracer.display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

import jsward.platformracer.common.game.Player;

public class PlayerAnimator {

    private ArrayList<Bitmap> playerSprite;
    private int animationFrame;
    private int animationCounter;
    private int playerSpriteCx;
    private int playerSpriteCy;

    public PlayerAnimator(ArrayList<Bitmap> sprites,int spriteCx,int spriteCy){
        this.playerSprite = sprites;
        this.playerSpriteCx = spriteCx;
        this.playerSpriteCy = spriteCy;
        animationCounter = 0;
        animationFrame = 0;
    }

    public void drawPlayer(Player player, Canvas canvas, Matrix playerMatrix, Matrix scale,int xOffset,float py){
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
        playerMatrix.postConcat(scale);
        playerMatrix.postTranslate(xOffset,py);
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

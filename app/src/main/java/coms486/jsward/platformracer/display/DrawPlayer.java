package coms486.jsward.platformracer.display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.util.ArrayList;

import coms486.jsward.platformracer.game.Player;

public class DrawPlayer implements Drawable{

    public static final int PLAYER_X_OFFSET = 400;

    private ArrayList<Bitmap> sprite;
    private Matrix matrix;
    private RectF hitbox;
    private int animationFrame;
    private int counter;



    private Player player;

    public DrawPlayer(Player p,ArrayList<Bitmap> sprite){
        this.sprite = sprite;
        player = p;

        animationFrame = 0;
        counter = 0;

        matrix = new Matrix();
    }


    @Override
    public void draw(Canvas canvas) {

        if (player.isFalling()) {
            animationFrame = 3;
        } else if (player.isMoving()) {
            counter++;
            if(counter>5){
                counter = 0;
                animationFrame = indexLooper(animationFrame + 1, 0, 2);
            }
        } else {
            animationFrame = 0;
            counter = 0;
        }
        matrix.setTranslate(PLAYER_X_OFFSET,player.getY());
        canvas.drawBitmap(sprite.get(animationFrame),matrix,null);
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

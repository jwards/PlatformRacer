package coms486.jsward.platformracer.display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

public class SVButton implements Drawable{
    private Matrix btnMatrix;
    private Matrix btnMatrixInverse;
    private RectF btnBox;
    private float width;
    private float height;
    private long tag;
    Bitmap sprite;


    public SVButton(float width, float height,float px,float py, long tag, Bitmap sprite){
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.tag=tag;
        btnBox = new RectF(0, 0, sprite.getWidth(), sprite.getHeight());
        btnMatrix = new Matrix();
        btnMatrixInverse = new Matrix();
        btnMatrix.invert(btnMatrixInverse);

        float sx,sy;
        sx = width/sprite.getWidth();
        sy = height / sprite.getHeight();
        setPosition(px,py,sx,sy);

    }

    public boolean wasClicked(float[] pointDown,float[] pointUp){
        return inButton(pointDown)&&inButton(pointUp);
    }

    public void setPosition(float x,float y,float sx,float sy) {
        btnMatrix.setTranslate(x, y);
        btnMatrix.preScale(sx, sy);
        btnMatrix.mapRect(btnBox);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, btnMatrix, null);
    }

    public void setTag(long tag){
        this.tag = tag;
    }

    public long getTag(){
        return tag;
    }


    public boolean inButton(float[] point){
        btnMatrixInverse.mapPoints(point);
        return btnBox.contains(point[0],point[1]);
    }


}

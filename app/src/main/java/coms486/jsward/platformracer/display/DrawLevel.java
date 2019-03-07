package coms486.jsward.platformracer.display;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class DrawLevel implements Drawable {


    @Override
    public void draw(Canvas canvas) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        Rect floor = new Rect(0, height - height / 3, width, height);
        Rect sky = new Rect(0, 0, width, height / 3 * 2);
        Paint blue = new Paint();
        Paint green = new Paint();
        blue.setColor(Color.BLUE);
        green.setColor(Color.GREEN);
        canvas.drawRect(floor, green);
        canvas.drawRect(sky, blue);
    }

}

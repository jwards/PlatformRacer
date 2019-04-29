package coms486.jsward.platformracer.display;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerDraw implements Drawable {

    private int x,y;
    private Date time;
    private SimpleDateFormat format;
    private Paint paint;

    public TimerDraw(int x, int y, Date time){
        this.x= x;
        this.y = y;
        this.time = time;
        format = new SimpleDateFormat("mm:ss.SS");
        paint = new Paint();
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(60);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(format.format(time),x,y,paint);
    }
}

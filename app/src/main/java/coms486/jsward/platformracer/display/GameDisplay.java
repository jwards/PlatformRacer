package coms486.jsward.platformracer.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;

public class GameDisplay extends SurfaceView implements SurfaceHolder.Callback {

    private List<SVButton> buttons;

    public GameDisplay(Context context) {
        super(context);
        init(context);
    }

    public GameDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameDisplay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        buttons = new LinkedList<>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void addButton(SVButton button){
        buttons.add(button);
    }

    public List<SVButton> getButtons(){
        return buttons;
    }


    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        drawBackground(canvas);

        for (SVButton svb : buttons) {
            svb.draw(canvas);
        }
    }

    private void drawBackground(Canvas canvas){
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

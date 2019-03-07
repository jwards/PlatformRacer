package coms486.jsward.platformracer.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class GameDisplay extends SurfaceView implements SurfaceHolder.Callback {

    private List<SVButton> buttons;
    private Paint background;

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
        background = new Paint();
        background.setColor(Color.GRAY);
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
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),background);
    }

    public void drawUI(Canvas canvas){
        for (SVButton svb : buttons) {
            svb.draw(canvas);
        }
    }



}

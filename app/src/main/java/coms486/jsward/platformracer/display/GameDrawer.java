package coms486.jsward.platformracer.display;

import android.graphics.Canvas;

import java.util.ArrayList;

public class GameDrawer implements Drawable{

    private GameDisplay gameDisplay;

    //TODO add method to specify draw order
    private ArrayList<Drawable> drawables;

    public GameDrawer(GameDisplay gameDisplay) {
        this.gameDisplay = gameDisplay;
        this.drawables = new ArrayList<>();
    }

    public void addDrawable(Drawable d){
        drawables.add(d);
    }

    public void removeDrawable(Drawable d){
        drawables.remove(d);
    }


    @Override
    public void draw(Canvas canvas) {
        gameDisplay.draw(canvas);
        for (Drawable d: drawables) {
            d.draw(canvas);
        }
        gameDisplay.drawUI(canvas);
    }
}

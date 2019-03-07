package coms486.jsward.platformracer.game;

import android.graphics.Rect;

import java.util.ArrayList;

public class PlatformLevel {

    private int sizex = 500;
    private int sizey = 25;

    private ArrayList<Rectangle> level;


    public PlatformLevel() {
        level = new ArrayList<>();
        level.add(new Rectangle(0,0,1,sizex));
        level.add(new Rectangle(10,4,1,5));
    }


    public ArrayList<Rectangle> getLevel(){
        return level;
    }


    public class Rectangle{
        public int x,y;
        public int height,length;

        public Rectangle(int x, int y, int height, int length) {
            this.x= x;
            this.y=y;
            this.height=height;
            this.length = length;
        }
    }
}

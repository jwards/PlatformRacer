package jsward.platformracer.common.game;

import java.util.ArrayList;

public class PlatformLevel {

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    private int sizex = 1000;
    private int sizey = 150;

    private ArrayList<RectangleF> levelObjects;
    private float[] playerHitbox = {
            4.7687864f, 1.9444445f,
            8.670521f, 1.9444445f,
            4.7687864f, 18.75f,
            8.670521f, 18.75f,
            3.1791909f, 4.305556f,
            3.1791909f, 15.138889f,
            10.260116f, 4.305556f,
            10.260116f, 15.138889f
    };

    public PlatformLevel() {
        levelObjects = new ArrayList<>();
        loadLevelObjects(lpts);
    }

    public int getHeight(){
        return sizey;
    }

    public int getWidth(){
        return sizex;
    }

    private static final int ITERATIONS= 3;

    public void detectCollision(Player p){
        boolean collisionX, collisionYBottom, collisionYTop;

        for (int i = 0; i < ITERATIONS; i++) {
            float nextdx = p.getVx();
            float nextdy = p.getVy();

            float projdx, projdy, orgdx, orgdy;

            collisionX = false;
            collisionYBottom = false;
            collisionYTop = false;

            orgdx = nextdx;
            orgdy = nextdy;

            float vectorLength;
            int segments;

            for(int obj = 0;obj<levelObjects.size()&& !collisionX && !collisionYBottom && !collisionYTop; obj++) {
                //0:up 1:down 2:left 3:right
                for (int dir = 0; dir < 4; dir++) {
                    if (dir == UP && nextdy > 0) continue;
                    if (dir == DOWN && nextdy < 0) continue;
                    if (dir == LEFT && nextdx > 0) continue;
                    if (dir == RIGHT && nextdx < 0) continue;
                    projdx = projdy = 0;

                    vectorLength = (float) Math.sqrt(nextdx * nextdx + nextdy * nextdy);
                    segments = 0;

                    while(!levelObjects.get(obj).contains(playerHitbox[dir*4] + p.getX() + projdx,playerHitbox[dir*4+1] +p.getY()+projdy)
                            && !levelObjects.get(obj).contains(playerHitbox[dir*4+2] + p.getX() + projdx,playerHitbox[dir*4+3] +p.getY()+projdy)
                            &&segments<vectorLength){
                        projdx += nextdx/vectorLength;
                        projdy += nextdy / vectorLength;
                        segments++;
                    }

                    //collision was found
                    if(segments<vectorLength){
                        if (segments > 0) {
                            projdx -= nextdx / vectorLength;
                            projdy -= nextdy / vectorLength;
                        }
                        //left right
                        if(dir>=2 && dir<=3) nextdx = projdx;
                        //up down
                        if(dir >= 0&& dir <= 1) nextdy = projdy;
                    }
                }
            }


            //detect collisions
            for (int obj = 0; obj < levelObjects.size() && !collisionX && !collisionYBottom && !collisionYTop; obj++) {
                //0:up 1:down 2:left 3:right
                for (int dir = 0; dir < 4; dir++) {
                    if (dir == DOWN && nextdy < 0) continue;
                    if (dir == LEFT && nextdx > 0) continue;
                    if (dir == RIGHT && nextdx < 0) continue;
                    //Left or Right
                    projdx = (dir >= 2 ? nextdx : 0);
                    //Up or down
                    projdy = (dir < 2 ? nextdy : 0);

                    while(levelObjects.get(obj).contains(playerHitbox[dir*4] + p.getX() + projdx,playerHitbox[dir*4+1] +p.getY()+projdy)
                        || levelObjects.get(obj).contains(playerHitbox[dir*4+2] + p.getX() + projdx,playerHitbox[dir*4+3] +p.getY()+projdy)){
                        if (dir == UP) ++projdy;
                        if(dir == DOWN) --projdy;
                        if(dir == LEFT) ++projdx;
                        if(dir == RIGHT) --projdx;
                    }

                    if(dir>= 2 && dir <= 3) nextdx = projdx;
                    if(dir>=0 && dir<=1) nextdy = projdy;
                }

                if (nextdy > orgdy && orgdy < 0) {
                    collisionYTop = true;
                }
                if (nextdy < orgdy && orgdy > 0) {
                    collisionYBottom = true;
                }
                if(Math.abs(nextdx-orgdx)>0.01f){
                    collisionX = true;
                }

                if (collisionX && collisionYTop && p.getVy() < 0) {
                    p.setVy(0);
                    nextdy = 0;
                }
            }


            //resolve collisions
            float px = p.getX(), py = p.getY();
            if(collisionYBottom || collisionYTop){
                py += nextdy;
                p.setVy(0);

                if(collisionYBottom){
                    p.setCanJump(true);
                }
            }

            if(collisionX){
                px += nextdx;
                p.setVx(0);
            }
            p.setPosition(px,py);
        }

        float px = p.getX(); float py = p.getY();
        p.setPosition(px + p.getVx(), py + p.getVy());
    }

    private void loadLevelObjects(float[] pts){
        for (int i = 0; i < pts.length; i=i+4) {
            levelObjects.add(new RectangleF(pts[i+0], pts[i+1], pts[i+2], pts[i+3]));
        }
    }

    public final static float[] lpts2 ={
            0, 0, 1000, 1,
            0, 1, 1, 150,
            999, 1, 1000, 150,
            1, 149, 999, 150
    };

    //test level
    public final static float[] lpts = {
            0, 0, 2, 150,
            4, 0, 2, 150,
           0,148,50,150};

}

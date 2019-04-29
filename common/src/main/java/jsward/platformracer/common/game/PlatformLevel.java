package jsward.platformracer.common.game;

import java.util.ArrayList;

public class PlatformLevel {

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    private int sizex = 10000;
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

    public int getStart(){
        return beginX;
    }

    public int getEnd(){
        return stopX;
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

                p.setCanJump(collisionYBottom);
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

    //start and stop points for the level
    //need to be translated into game coordinates
    private final int beginX = 45;
    private final int stopX =  2773;

    //test level
    public final static float[] lpts = {0,0,2,150,
            4,0,6,150,
            2385,29,2676,32,
            2301,31,2323,37,
            2845,40,2860,150,
            2363,42,2366,76,
            2340,50,2346,59,
            2124,56,2133,69,
            2309,56,2340,59,
            2346,56,2359,59,
            2193,58,2284,63,
            2111,64,2119,65,
            1392,68,1393,82,
            1390,69,1392,82,
            1393,69,1394,81,
            1950,69,2065,74,
            2155,69,2182,74,
            1388,70,1390,84,
            2076,70,2104,78,
            1386,71,1388,85,
            1394,71,1395,81,
            1385,72,1386,86,
            1395,72,1396,80,
            1383,73,1385,86,
            1896,73,1922,79,
            1381,74,1383,88,
            1396,74,1397,80,
            1379,75,1381,89,
            1378,76,1379,90,
            1397,76,1398,79,
            1376,77,1378,90,
            1374,78,1376,91,
            1372,79,1374,93,
            1371,80,1372,94,
            1369,81,1371,94,
            1367,82,1369,95,
            1390,82,1391,83,
            1365,83,1367,97,
            577,84,679,92,
            1363,84,1365,98,
            1840,84,1873,87,
            1362,85,1363,99,
            1360,86,1362,99,
            1383,86,1384,87,
            1358,87,1360,101,
            1356,88,1358,102,
            1355,89,1356,103,
            1353,90,1355,103,
            1376,90,1377,91,
            1351,91,1353,104,
            1374,91,1375,92,
            1349,92,1351,106,
            1348,93,1349,107,
            563,94,569,97,
            1346,94,1348,107,
            1369,94,1370,95,
            1450,94,1554,106,
            1344,95,1346,108,
            1367,95,1368,96,
            1790,95,1810,100,
            1342,96,1344,110,
            1340,97,1342,111,
            1339,98,1340,112,
            2253,98,2335,104,
            1337,99,1339,112,
            1360,99,1361,100,
            1335,100,1337,114,
            1713,100,1725,111,
            1748,100,1764,108,
            1333,101,1335,115,
            1332,102,1333,116,
            1330,103,1332,116,
            1353,103,1354,104,
            1328,104,1330,117,
            1351,104,1352,105,
            1326,105,1328,119,
            510,106,561,112,
            1325,106,1326,120,
            1073,107,1121,113,
            1323,107,1325,120,
            1346,107,1347,108,
            1321,108,1323,121,
            1344,108,1345,109,
            1319,109,1321,123,
            475,110,502,123,
            1317,110,1319,124,
            1316,111,1317,125,
            1679,111,1688,123,
            2660,111,2709,150,
            1314,112,1316,125,
            1337,112,1338,113,
            1312,113,1314,127,
            1310,114,1312,128,
            1309,115,1310,129,
            186,116,216,118,
            1307,116,1309,129,
            1330,116,1331,117,
            182,117,186,118,
            216,117,226,118,
            1305,117,1307,130,
            1328,117,1329,118,
            994,118,1054,128,
            1303,118,1305,132,
            1302,119,1303,133,
            179,120,248,122,
            1300,120,1302,133,
            1323,120,1324,121,
            248,121,253,122,
            1298,121,1300,134,
            1321,121,1322,122,
            1656,121,1663,132,
            1296,122,1298,136,
            2498,122,2535,127,
            2547,122,2594,128,
            1294,123,1296,137,
            163,124,170,126,
            180,124,267,125,
            1293,124,1294,138,
            160,125,163,126,
            170,125,172,126,
            180,125,204,126,
            208,125,272,126,
            1291,125,1293,138,
            1314,125,1315,126,
            1289,126,1291,140,
            2335,126,2359,132,
            1287,127,1289,141,
            2375,127,2418,132,
            2434,127,2488,132,
            46,128,112,130,
            141,128,173,130,
            239,128,287,129,
            683,128,963,141,
            1286,128,1287,142,
            43,129,46,130,
            112,129,119,130,
            135,129,141,130,
            243,129,292,130,
            1284,129,1286,142,
            1307,129,1308,130,
            1282,130,1284,143,
            1305,130,1306,131,
            2629,130,2660,142,
            2709,130,2845,137,
            329,131,446,150,
            1280,131,1282,145,
            2,132,4,134,
            6,132,172,133,
            256,132,305,133,
            1279,132,1280,146,
            6,133,170,134,
            260,133,307,134,
            1277,133,1279,146,
            1300,133,1301,134,
            1275,134,1277,147,
            1298,134,1299,135,
            1273,135,1275,145,
            2,136,4,138,
            6,136,160,137,
            275,136,311,137,
            1271,136,1273,142,
            1553,136,1641,139,
            6,137,57,138,
            104,137,155,138,
            281,137,312,138,
            1270,137,1271,140,
            2599,137,2629,142,
            1291,138,1292,139,
            2,140,4,142,
            6,140,38,141,
            297,140,312,141,
            554,140,595,142,
            6,141,35,142,
            300,141,311,142,
            1160,141,1261,146,
            1272,142,1273,143,
            1284,142,1285,143,
            471,143,492,145,
            1282,143,1283,144,
            1274,145,1275,147,
            1277,146,1278,147,
            1275,147,1276,148,};

}

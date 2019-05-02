package jsward.platformracer.common.game;

import java.util.ArrayList;
import java.util.Date;

import jsward.platformracer.common.network.GameUpdatePacket;

public class GameCore {


    private ArrayList<Player> players;
    private ArrayList<PlayerController> controllers;

    private Date gameTime;
    private long startTime = 0;
    private boolean started = false;

    private PlatformLevel platformLevel;

    public GameCore(PlatformLevel platformLevel, Date gameTime){
        this.platformLevel = platformLevel;
        this.gameTime = gameTime;
        players = new ArrayList<>();
        controllers = new ArrayList<>();

    }

    public PlayerController getPlayerController(String playerId){
        for (PlayerController pc : controllers) {
            if (pc.getId().equals(playerId)) {
                return pc;
            }
        }
        return null;
    }

    public void addPlayerController(String playerId){
        Player p = getPlayer(playerId);
        if(p!=null) {
            controllers.add(new PlayerController(platformLevel, p));
        }
    }

    public void addPlayer(String id){
        Player player = new Player(0, 1, 1f,6f,3f,-15f,id);
        players.add(player);
    }

    public Player getPlayer(String id){
        for(Player p:players){
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Player> getAllPlayers() {
        return players;
    }

    public void onGameStart(){
        startTime = System.currentTimeMillis();
        started = true;
    }

    public long getTime(){
        return gameTime.getTime();
    }

    public void tick(){
        if(started) {
            gameTime.setTime(System.currentTimeMillis() - startTime);
            for (PlayerController pc : controllers) {
                pc.tick();
            }
        }
    }

    public void initGame(GameUpdatePacket gup){
        for(Player p: gup.players){
            addPlayer(p.getId());
            addPlayerController(p.getId());
        }
    }

    public void updatePlayer(Player toUpdate,Player reference){
        updateLocalPlayer(toUpdate, reference);
        toUpdate.controls = reference.controls;
        toUpdate.setVy(reference.getVy());
        toUpdate.setVx(reference.getVx());
    }

    public void updateLocalPlayer(Player toUpdate, Player reference) {
        toUpdate.setPosition(reference.getX(), reference.getY());
        toUpdate.setCanJump(reference.canJump());
    }
}

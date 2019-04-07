package jsward.platformracer.common.game;

import jsward.platformracer.common.network.GameUpdatePacket;

public class GameCore {


    private Player player;
    private Player opponent;
    private PlayerController playerController;

    public GameCore(PlatformLevel platformLevel){
        player = new Player(0, 1, 1f,6f,3f,-15f);
        playerController = new PlayerController(platformLevel, player);
    }

    public PlayerController getPlayerController(){
        return playerController;
    }

    public Player getPlayer(){
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void tick(){
        playerController.tick();
    }

    public void update(GameUpdatePacket gup){
        gup.updatePlayer(player);
    }
}
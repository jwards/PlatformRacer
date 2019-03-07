package coms486.jsward.platformracer.game;

public class GameCore {

    private Player player;
    private PlayerController playerController;

    public GameCore(){
        player = new Player(0, 1, .3f);
        playerController = new PlayerController(null, player);
    }

    public PlayerController getPlayerController(){
        return playerController;
    }

    public Player getPlayer(){
        return player;
    }

    public void tick(){
        playerController.update();
    }


}

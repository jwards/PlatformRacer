package jsward.platformracer.common.game;

import java.util.ArrayList;

import jsward.platformracer.common.network.GameUpdatePacket;

public class GameCore {


    private Player player;
    private ArrayList<Player> others;

    private PlayerController playerController;

    public GameCore(PlatformLevel platformLevel){
        player = new Player(0, 1, 1f,6f,3f,-15f);
        others = new ArrayList<>();
        playerController = new PlayerController(platformLevel, player);
    }

    public PlayerController getPlayerController(){
        return playerController;
    }

    public Player getPlayer(){
        return player;
    }

    public ArrayList<Player> getOpponents() {
        return others;
    }

    public void tick(){
        playerController.tick();
    }

    public void update(GameUpdatePacket gup){
        updatePlayer(player, gup.client);

        //the order of the players is not guarenteed but that shouldn't matter since other players
        //are not distinguishable anyways.
        for (int i = 0; i < others.size(); i++) {
            updatePlayer(others.get(i), gup.otherPlayers.get(i));
        }

    }

    private void updatePlayer(Player toUpdate,Player reference){
        toUpdate.setVy(reference.getVy());
        toUpdate.setVx(reference.getVx());
        toUpdate.setPosition(reference.getX(), reference.getY());
        toUpdate.setCanJump(reference.canJump());
    }
}

package jsward.platformracer.common.network;

import java.io.Serializable;

public class LoginPacket implements Serializable {

    private final String username;
    private final String password;
    private final String userid;
    private final boolean newUser;

    public LoginPacket(String username,String password){
        this.username = username;
        this.password = password;
        this.userid = "";
        this.newUser = false;
    }

    public LoginPacket(String userid,boolean newUser){
        this.userid = userid;
        this.newUser = newUser;
        this.username = "";
        this.password = "";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserid() {
        return userid;
    }

    public boolean isNewUser() {
        return newUser;
    }
}

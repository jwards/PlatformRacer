package jsward.platformracer.common.network;

import java.io.Serializable;

public class LoginPacket implements Serializable {

    private final String username;
    private final String password;
    private final String userid;
    private final boolean newUser;

    //format of a request
    public LoginPacket(String username,String password){
        this.username = username;
        this.password = password;
        this.userid = "";
        this.newUser = false;
    }

    //format of a reply
    public LoginPacket(String username,String userid,boolean newUser){
        this.userid = userid;
        this.newUser = newUser;
        this.username = username;
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

    //returns if this is a valid login
    //if it is valid, the fields will be  set
    public boolean isValid(){
        return  (userid.length() != 0 && username.length() != 0);
    }
}

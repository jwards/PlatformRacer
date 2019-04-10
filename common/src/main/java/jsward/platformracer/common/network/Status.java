package jsward.platformracer.common.network;

import java.util.HashMap;
import java.util.Map;

public enum Status {

    OK,
    BAD,
    BEGIN;

    private static Map<Integer, Status> map = new HashMap<>();

    //creates mapping from int to enum
    static {
        for(Status s:Status.values()){
            map.put(s.ordinal(),s);
        }
    }

    public static Status valueOf(int status){
        return map.get(status);
    }
}

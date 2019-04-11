package jsward.platformracer.common.network;

import java.util.HashMap;
import java.util.Map;

public enum ReqType {

    REQ_LOBBY_LIST,
    REQ_JOIN,
    REQ_CREATE,
    REQ_DESTROY;


    private static Map<Integer, ReqType> map = new HashMap<>();

    //creates mapping from int to enum
    static {
        for(ReqType r:ReqType.values()){
            map.put(r.ordinal(),r);
        }
    }

    public static ReqType valueOf(int status){
        return map.get(status);
    }
}

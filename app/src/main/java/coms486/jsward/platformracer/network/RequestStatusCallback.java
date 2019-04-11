package coms486.jsward.platformracer.network;

import jsward.platformracer.common.network.ReqType;
import jsward.platformracer.common.network.Status;

public interface RequestStatusCallback extends NCallback {
    void onResponse(ReqType type, Status response);
}

package coms486.jsward.platformracer.network;

import jsward.platformracer.common.network.ReqType;

public interface NetRequest {
    public ReqType getType();
    public NCallback getCallback();
    public String toString();
}

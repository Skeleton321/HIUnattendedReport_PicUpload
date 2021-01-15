package net.qzwxsaedc.hiunattendedreport.event;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.HashMap;
import java.util.Map;

class HttpBaseEvent {
    private final Map<String, HttpEventAdapter> adapterMap;
    HttpBaseEvent(){
        adapterMap = new HashMap<>();
    }

    public boolean _call(String uri, ChannelHandlerContext ctx, FullHttpRequest req) {
        if(!adapterMap.containsKey(uri))    return false;
        try {
            adapterMap.get(uri).called(ctx, req);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void _register(String uri, HttpEventAdapter func) {
        adapterMap.put(uri, func);
    }
}

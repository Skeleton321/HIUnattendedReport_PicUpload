package net.qzwxsaedc.hiunattendedreport.event;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class PostEvent extends HttpBaseEvent{
    private static final PostEvent instance = new PostEvent();

    private PostEvent(){
        super();
    }

    public static boolean call(String uri, ChannelHandlerContext ctx, FullHttpRequest req) {
        return instance._call(uri, ctx, req);
    }

    public static void register(String uri, HttpEventAdapter e){
        instance._register(uri, e);
    }
}

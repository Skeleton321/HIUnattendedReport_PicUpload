package net.qzwxsaedc.hiunattendedreport.event;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.IOException;

public interface HttpEventAdapter {
    void called(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception;
}

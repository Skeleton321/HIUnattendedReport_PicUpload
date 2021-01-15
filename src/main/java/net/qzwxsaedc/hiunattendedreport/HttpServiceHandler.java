package net.qzwxsaedc.hiunattendedreport;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import net.qzwxsaedc.hiunattendedreport.event.GetEvent;
import net.qzwxsaedc.hiunattendedreport.event.PostEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static net.qzwxsaedc.hiunattendedreport.Misc.*;

public class HttpServiceHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        if(req.method() == HttpMethod.POST && req.headers().contains("Expect") && req.headers().get("Expect").equals("100-continue")){
            ctx.write(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.CONTINUE));
        }

        StringBuilder content = new StringBuilder();
        content.append('[');
        for(Map.Entry<String, Object> entry : getRequestParams(req).entrySet())
            content.append(String.format("{\"%s\":\"%s\"},", entry.getKey(), entry.getValue().toString()));
        if(content.length() > 1)
            content.deleteCharAt(content.length() - 1);
        content.append(']');

        String response_html = String.format("{\"uri\":\"%s\",\"method\":\"%s\",\"content\":%s}", req.uri(), req.method(), content);
        System.out.println(response_html);

        boolean status = false;
        if(req.method() == HttpMethod.GET)
            status = GetEvent.call(req.uri(), ctx, req);
        if(req.method() == HttpMethod.POST)
            status = PostEvent.call(req.uri(), ctx, req);

        if(!status){
            try {
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.NOT_FOUND,
                        Unpooled.copiedBuffer(getHtmlPage("404.html"), CharsetUtil.UTF_8)
                );
                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8")
                        .set(CONTENT_LENGTH, response.content().readableBytes())
                        .set(CONNECTION, HttpHeaderValues.CLOSE);
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    public static Map<String, Object> getRequestParams(FullHttpRequest request){
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
        Map<String, Object> params = new HashMap<>();

        for (InterfaceHttpData data : httpPostData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        return params;
    }

    public static void send(ChannelHandlerContext ctx, String content){
        System.out.println(content);
        send(ctx, content, "application/json");
    }

    public static void send(ChannelHandlerContext ctx, String content, String content_type){
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(content, CharsetUtil.UTF_8)
        );
        response.headers().set(CONTENT_TYPE, content_type + "; charset=UTF-8")
                .set(CONTENT_LENGTH, response.content().readableBytes())
                .set(CONNECTION, HttpHeaderValues.CLOSE);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}

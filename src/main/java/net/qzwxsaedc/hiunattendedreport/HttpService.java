package net.qzwxsaedc.hiunattendedreport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.qzwxsaedc.hiunattendedreport.event.JVMShutdownEvent;

public class HttpService {
    private final EventLoopGroup worker;
    private final EventLoopGroup boss;
    private final ServerBootstrap boot;
    private ChannelFuture future;

    public HttpService(){
        worker = new NioEventLoopGroup(10);
        boss = new NioEventLoopGroup(10);
        boot = new ServerBootstrap();
        boot.group(boss, worker)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpServerCodec())
                                .addLast("httpAggregator",new HttpObjectAggregator(512*1024))
                                .addLast(new HttpServiceHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, false);
    }

    public void start(int port) throws InterruptedException {
        future = boot.bind(port).sync();
        JVMShutdownEvent.register(() -> {
            future.channel().close().sync();
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            System.out.println("server closed.");
        });
    }

    public void block() throws InterruptedException {
        future.channel().closeFuture().sync();
    }
}

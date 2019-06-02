package com.nikai.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * nio-chat-room com.nikai.netty
 *
 * @author: nikai
 * @Description:
 * @Date: Create in 15:50 2019/6/2
 * @Modified By:
 */
public class NettyServer {

    public static final String IP = "127.0.0.1";
    public static final int port = 6666;
    public static final int BIGGROUPSIZE = Runtime.getRuntime().availableProcessors();

    public static final int BIZHERADSIZE = 100;

    public static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIGGROUPSIZE);
    public static final EventLoopGroup workGroup = new NioEventLoopGroup(BIZHERADSIZE);

    public static void start() throws Exception {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
//            .handler()
//            .childOption()
//            .childAttr()
            .childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) {

                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 1024));
                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new TcpServerHandler());

                }
            });

        ChannelFuture channelFuture = serverBootstrap.bind(IP, port).sync();
        channelFuture.channel().closeFuture().sync();
        System.out.println("服务器开始运行....");


    }

    protected static void shutdown() {
        workGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("start server.....");
        NettyServer.start();

    }
}

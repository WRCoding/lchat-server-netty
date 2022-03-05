package top.ink.nettycore.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import top.ink.nettycore.handler.MessageChannelInit;

/**
 * desc: 服务启动器
 *
 * @author ink
 * date:2022-02-28 22:08
 */
public class ChatBootServer {

    private static final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static final NioEventLoopGroup workGroup = new NioEventLoopGroup();

    public static void init() {
        new ServerBootstrap()
                .group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MessageChannelInit()).bind(8077);
    }

    public static void main(String[] args) {
        ChatBootServer.init();
    }
}

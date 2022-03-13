package top.ink.nettycore.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.ink.nettycore.handler.MessageChannelInit;

import javax.annotation.Resource;

/**
 * desc: 服务启动器
 *
 * @author ink
 * date:2022-02-28 22:08
 */
@Component
public class ChatBootServer {

    @Resource
    private MessageChannelInit messageChannelInit;

    private static final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static final NioEventLoopGroup workGroup = new NioEventLoopGroup();

    public void init() throws InterruptedException {
        new ServerBootstrap()
                .group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(messageChannelInit).bind(8077).sync();
    }

}

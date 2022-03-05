package top.ink.nettycore.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import top.ink.nettycore.constant.ContentType;
import top.ink.nettycore.entity.message.chatmessage.ChatMessage;
import top.ink.nettycore.entity.message.chatmessage.TextMessage;
import top.ink.nettycore.protocol.MessageFrameDecoder;
import top.ink.nettycore.protocol.codec.MessageCodec;

import java.net.InetSocketAddress;

/**
 * desc: 测试客户端
 *
 * @author ink
 * date:2022-02-28 23:15
 */
public class ClientBoot {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new MessageFrameDecoder());
                        nioSocketChannel.pipeline().addLast(new LoggingHandler());
                        nioSocketChannel.pipeline().addLast(new MessageCodec());
                        nioSocketChannel.pipeline().addLast(new LoggingHandler());
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                            }
                        });
                    }
                }).connect(new InetSocketAddress("localhost", 8077));
        channelFuture.sync();
        Channel channel = channelFuture.channel();
        TextMessage message = new TextMessage();
        message.setContent("1111");
        message.setSender("xi");
        message.setReceiver("li");
        message.setSeqId("12123132");
        message.setContentType(ContentType.TEXT.type());
        channel.writeAndFlush(message);
        channel.closeFuture().sync();

    }
}

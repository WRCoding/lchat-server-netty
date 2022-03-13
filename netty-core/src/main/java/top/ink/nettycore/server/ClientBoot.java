package top.ink.nettycore.server;

import com.sun.xml.internal.stream.util.BufferAllocator;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import top.ink.nettycore.constant.ContentType;
import top.ink.nettycore.entity.message.Message;
import top.ink.nettycore.entity.message.chatmessage.ChatMessage;
import top.ink.nettycore.entity.message.chatmessage.TextMessage;
import top.ink.nettycore.entity.message.systemmessage.InitMessage;
import top.ink.nettycore.protocol.MessageFrameDecoder;
import top.ink.nettycore.protocol.codec.MessageCodec;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

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
//                        nioSocketChannel.pipeline().addLast(new MessageFrameDecoder());
//                        nioSocketChannel.pipeline().addLast(new LoggingHandler());
//                        nioSocketChannel.pipeline().addLast(new MessageCodec());
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        nioSocketChannel.pipeline().addLast(new LoggingHandler());
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                String d = (String) msg;
                                System.out.println(d);
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                }).connect(new InetSocketAddress("localhost", 8077));
        channelFuture.sync();
        Channel channel = channelFuture.channel();
        InitMessage message = new InitMessage();
        message.setSender("xi");
        message.setReceiver("li");
        message.setSeqId("12123132");
        System.out.println(message.getType());
        System.out.println(message.getMsgType());
        System.out.println(message.getContentType());
        System.out.println(message);
        channel.writeAndFlush("buf");
        channel.closeFuture().sync();

    }
}

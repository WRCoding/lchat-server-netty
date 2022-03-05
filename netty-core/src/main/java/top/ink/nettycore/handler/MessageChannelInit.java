package top.ink.nettycore.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import top.ink.nettycore.entity.message.Message;
import top.ink.nettycore.protocol.MessageFrameDecoder;
import top.ink.nettycore.protocol.codec.MessageCodec;

/**
 * desc: 消息处理器
 *
 * @author ink
 * date:2022-02-28 22:16
 */
public class MessageChannelInit extends ChannelInitializer<NioSocketChannel> {

    LoggingHandler LOGGING_HANDLER = new LoggingHandler();

    @Override
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
        nioSocketChannel.pipeline().addLast(new MessageFrameDecoder());
        nioSocketChannel.pipeline().addLast(LOGGING_HANDLER);
        nioSocketChannel.pipeline().addLast(new MessageCodec());
        nioSocketChannel.pipeline().addLast(LOGGING_HANDLER);
        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println(msg);
                ctx.channel().writeAndFlush(msg);
            }
        });
//        nioSocketChannel.pipeline().addLast();
    }
}

package top.ink.nettycore.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.ink.nettycore.entity.message.Message;
import top.ink.nettycore.entity.message.systemmessage.InitMessage;
import top.ink.nettycore.entity.message.systemmessage.SystemMessage;
import top.ink.nettycore.protocol.MessageFrameDecoder;
import top.ink.nettycore.protocol.codec.MessageCodec;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * desc: 消息处理器
 *
 * @author ink
 * date:2022-02-28 22:16
 */
@Component
@Slf4j
public class MessageChannelInit extends ChannelInitializer<NioSocketChannel> {

    @Resource
    private ChatMessageHandler chatMessageHandler;

    @Resource
    private MessageFrameDecoder messageFrameDecoder;

    @Resource
    private MessageCodec messageCodec;

    @Resource
    private SystemMessageHandler systemMessageHandler;

    LoggingHandler LOGGING_HANDLER = new LoggingHandler();

    @Override
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
//        nioSocketChannel.pipeline().addLast(messageFrameDecoder);
//        nioSocketChannel.pipeline().addLast(messageCodec);
//        nioSocketChannel.pipeline().addLast(messageCodec);
        nioSocketChannel.pipeline().addLast(new StringEncoder());
        nioSocketChannel.pipeline().addLast(new StringDecoder());
        nioSocketChannel.pipeline().addLast(LOGGING_HANDLER);
//        nioSocketChannel.pipeline().addLast(new IdleStateHandler(5, 0, 0));
//        nioSocketChannel.pipeline().addLast(new ChannelDuplexHandler() {
//
//            @Override
//            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                String d = (String) msg;
//                System.out.println(d);
//                super.channelRead(ctx, msg);
//            }
//
//            @Override
//            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//                IdleStateEvent event = (IdleStateEvent) evt;
//                // 触发了读空闲事件
//                if (event.state() == IdleState.READER_IDLE) {
//                    Channel channel = ctx.channel();
//                    InitMessage message = new InitMessage();
//                    message.setSender("heart");
//                    message.setReceiver("heart");
//                    message.setSeqId("2");
//                    InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
//                    log.info("已经 5s 没有读到数据了,port: " + address.getPort());
//                    ctx.writeAndFlush("message");
//                }
//            }
//        });
//        nioSocketChannel.pipeline().addLast(LOGGING_HANDLER);
//        nioSocketChannel.pipeline().addLast(chatMessageHandler);
//        nioSocketChannel.pipeline().addLast(systemMessageHandler);
    }
}

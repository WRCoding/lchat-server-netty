package top.ink.nettycore.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.ink.nettycore.constant.MsgType;
import top.ink.nettycore.entity.message.chatmessage.ChatMessage;
import top.ink.nettycore.entity.message.systemmessage.AckMessage;
import top.ink.nettycore.entity.message.systemmessage.InitMessage;
import top.ink.nettycore.entity.message.systemmessage.QuitMessage;
import top.ink.nettycore.entity.message.systemmessage.SystemMessage;
import top.ink.nettycore.server.session.Session;
import top.ink.nettycore.util.RedisUtil;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * desc: 聊天消息处理
 *
 * @author ink
 * date:2022-03-06 10:12
 */
@Slf4j
@Component
public class SystemMessageHandler extends SimpleChannelInboundHandler<SystemMessage> {

    @Resource
    private Session session;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("---SystemMessageHandler---");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SystemMessage systemMessage) throws Exception {
        System.out.println(systemMessage.toString());
        switch (MsgType.getMsgType(systemMessage.getMsgType())) {
            case INIT:
                handlerInit(ctx, (InitMessage)systemMessage);
                break;
            case ACK:
                handlerAck(ctx, (AckMessage)systemMessage);
                break;
            case QUIT:
                handlerQuit(ctx, (QuitMessage)systemMessage);
                break;
            default:
                throw new UnsupportedOperationException(MsgType.getMsgType(systemMessage.getMsgType()) + "类型不支持");
        }
    }

    private void handlerInit(ChannelHandlerContext ctx, InitMessage initMessage) {
        Channel channel = ctx.channel();
        session.register(channel, initMessage.getSender());
    }

    private void handlerAck(ChannelHandlerContext ctx, AckMessage ackMessage) {

    }


    private void handlerQuit(ChannelHandlerContext ctx, QuitMessage quitMessage) {

    }


}

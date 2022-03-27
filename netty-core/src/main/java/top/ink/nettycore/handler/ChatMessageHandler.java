package top.ink.nettycore.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.ink.nettycore.constant.MsgType;
import top.ink.nettycore.entity.message.Message;
import top.ink.nettycore.entity.message.chatmessage.ChatMessage;
import top.ink.nettycore.entity.message.chatmessage.TextMessage;
import top.ink.nettycore.entity.message.systemmessage.AckMessage;
import top.ink.nettycore.entity.message.systemmessage.InitMessage;
import top.ink.nettycore.entity.message.systemmessage.QuitMessage;
import top.ink.nettycore.server.session.Session;
import top.ink.nettycore.util.RedisUtil;

import javax.annotation.Resource;

/**
 * desc: 聊天消息处理
 *
 * @author ink
 * date:2022-03-06 10:12
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ChatMessageHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Resource
    private Session session;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage chatMessage) throws Exception {
        System.out.println("ChatMessageHandler:channelRead0");
        System.out.println(chatMessage.toString());
        switch (MsgType.getMsgType(chatMessage.getMsgType())) {
            case SINGLE:
                handlerSingle(ctx,chatMessage);
                break;
            case GROUP:
                handlerGroup(ctx,chatMessage);
                break;
            default:
                throw new UnsupportedOperationException(MsgType.getMsgType(chatMessage.getMsgType()) + "类型不支持");
        }
    }

    private void handlerSingle(ChannelHandlerContext ctx, ChatMessage chatMessage) {
        TextMessage textMessage = (TextMessage) chatMessage;
        String receiver = textMessage.getReceiver();
        String sender = textMessage.getSender();
        log.info("收到{}, 发送给{}的消息: {}",sender,receiver,textMessage);
        if (session.exist(receiver)){
            Channel channel = this.session.getSession(receiver);
            channel.writeAndFlush(textMessage);
        }else{
//            String content = textMessage.getContent();
//            if (content.length() < 4){
//                Channel channel = this.session.getSession(sender);
//                channel.writeAndFlush(createAck(chatMessage));
//            }
        }
    }

    private void handlerGroup(ChannelHandlerContext ctx, ChatMessage chatMessage) {

    }

    private AckMessage createAck(ChatMessage message){
        AckMessage ack = new AckMessage();
        ack.setReceiver(message.getSender());
        ack.setSender(message.getReceiver());
        ack.setMsgSeq(message.getMsgSeq());
        return ack;
    }
}

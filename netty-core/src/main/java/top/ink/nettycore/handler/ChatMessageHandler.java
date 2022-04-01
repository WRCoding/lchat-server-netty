package top.ink.nettycore.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;


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
            Channel channel = session.getSession(receiver);
            channel.writeAndFlush(textMessage);
        }else{
            sendMsg2Kafka(textMessage);

        }
    }

    private void sendMsg2Kafka(ChatMessage chatMessage) {
        String message = JSON.toJSONString(chatMessage);
        kafkaTemplate.send("chat-netty", message).addCallback(success -> {
            log.info("成功发送消息到,topic:{} ,partition:{} ,offset:{}", success.getRecordMetadata().topic(),
                    success.getRecordMetadata().partition(), success.getRecordMetadata().offset());
            Channel channel = this.session.getSession(chatMessage.getSender());
            channel.writeAndFlush(createAck(chatMessage));
        }, error -> {
            log.error("消息发送失败: {}", error.getMessage());
        });
    }

    private void handlerGroup(ChannelHandlerContext ctx, ChatMessage chatMessage) {

    }

    private AckMessage createAck(ChatMessage message){
        AckMessage ack = new AckMessage();
        ack.setReceiver(message.getSender());
        ack.setSender(message.getReceiver());
        ack.setMsgSeq(message.getMsgSeq());
        log.info("ack : {}", ack);
        return ack;
    }
}

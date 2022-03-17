package top.ink.nettycore.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.ink.nettycore.entity.message.chatmessage.ChatMessage;
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
public class ChatMessageHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Resource
    private RedisUtil redisUtil;


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage) throws Exception {
        System.out.println("ChatMessageHandler:channelRead0");
        System.out.println(chatMessage.toString());
        channelHandlerContext.writeAndFlush(chatMessage);
    }
}

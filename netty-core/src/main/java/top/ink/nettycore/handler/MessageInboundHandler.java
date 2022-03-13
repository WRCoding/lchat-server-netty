package top.ink.nettycore.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import top.ink.nettycore.constant.Type;
import top.ink.nettycore.entity.message.Message;
import top.ink.nettycore.entity.message.chatmessage.ChatMessage;
import top.ink.nettycore.entity.message.systemmessage.SystemMessage;
import top.ink.nettycore.util.RedisUtil;

import javax.annotation.Resource;

/**
 * desc: message入站处理器
 * @author ink
 * date:2022-03-06 09:37
 */
@Slf4j
public class MessageInboundHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private RedisUtil redisUtil;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        ctx.fireChannelRead(message);
//        switch (Type.getType(message.getType())){
//            case CHAT:
//                ctx.fireChannelRead((ChatMessage)message);
//                break;
//            case SYSTEM:
//                ctx.fireChannelRead((SystemMessage)message);
//                break;
//            default:
//                throw new UnsupportedOperationException(Type.getType(message.getType())+"类型不支持");
//        }
    }
}

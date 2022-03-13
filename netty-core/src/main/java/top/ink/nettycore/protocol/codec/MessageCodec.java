package top.ink.nettycore.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.ink.nettycore.constant.Type;
import top.ink.nettycore.entity.message.Message;
import top.ink.nettycore.entity.message.chatmessage.ChatMessage;
import top.ink.nettycore.entity.message.systemmessage.SystemMessage;
import top.ink.nettycore.protocol.Algorithm;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * desc: 消息编解码器
 *
 * @author ink
 * date:2022-02-28 21:20
 */
@Component
public class MessageCodec extends ByteToMessageCodec<Message> {

    @Value("${message.serializer}")
    String serializer = "JSON";


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        System.out.println("encode: ");
        byteBuf.writeBytes(new byte[]{'i', 'n', 'k'});
        byteBuf.writeByte(1);
        byteBuf.writeInt(0xFFFF);
        //0-JSON
        byteBuf.writeByte(Algorithm.valueOf(serializer).ordinal());
        //内容类型
        byteBuf.writeByte(message.getContentType());
        //消息类型
        byteBuf.writeByte(message.getMsgType());
        //类型
        byteBuf.writeByte(message.getType());
        byte[] bytes = Algorithm.valueOf(serializer).serialize(message);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] a = new byte[3];
        byteBuf.readBytes(a,0,3);
        byte version = byteBuf.readByte();
        int padding = byteBuf.readInt();
        byte serializer = byteBuf.readByte();
        byte contentType = byteBuf.readByte();
        byte msgType = byteBuf.readByte();
        byte type = byteBuf.readByte();
        int len = byteBuf.readInt();
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes, 0, len);
        Algorithm algorithm = Algorithm.values()[serializer];
        if (Type.CHAT.type() == type){
            //聊天消息
            Class<? extends ChatMessage> chatMessageClass = ChatMessage.getChatMessageClass(contentType);
            ChatMessage chatMessage = algorithm.deserialize(chatMessageClass, bytes);
            System.out.println("decode: "+chatMessage);
            list.add(chatMessage);
        }else {
            //系统消息
            Class<? extends SystemMessage> systemMessageClass = SystemMessage.getSystemMessageClass(msgType);
            SystemMessage systemMessage = algorithm.deserialize(systemMessageClass, bytes);
            list.add(systemMessage);
        }
    }
}

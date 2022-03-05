package top.ink.nettycore.entity.message.chatmessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ink.nettycore.constant.ContentType;
import top.ink.nettycore.constant.MsgType;
import top.ink.nettycore.constant.Type;
import top.ink.nettycore.entity.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * desc: ChatMessage
 * @author ink
 * date:2022-02-27 19:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ChatMessage extends Message {

    private static final Map<Byte, Class<? extends ChatMessage>> CHAT_MESSAGE_CLASSES = new HashMap<>();

    static {
        CHAT_MESSAGE_CLASSES.put(ContentType.TEXT.type(), TextMessage.class);
        CHAT_MESSAGE_CLASSES.put(ContentType.IMAGE.type(), ImageMessage.class);
    }

    public static Class<? extends ChatMessage> getChatMessageClass(byte messageType) {
        return CHAT_MESSAGE_CLASSES.get(messageType);
    }

    @Override
    public byte getType() {
        return Type.CHAT.type();
    }

    @Override
    public byte getMsgType() {
        //默认为单聊
        return MsgType.SINGLE.type();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

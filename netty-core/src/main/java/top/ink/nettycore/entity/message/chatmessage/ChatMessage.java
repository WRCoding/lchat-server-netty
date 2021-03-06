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
public abstract class ChatMessage extends Message {

    private static final Map<Byte, Class<? extends ChatMessage>> CHAT_MESSAGE_CLASSES = new HashMap<>();

    public ChatMessage() {
        this.setType();
        this.setMsgType();
    }

    static {
        CHAT_MESSAGE_CLASSES.put(ContentType.TEXT.type(), TextMessage.class);
        CHAT_MESSAGE_CLASSES.put(ContentType.IMAGE.type(), ImageMessage.class);
    }

    public static Class<? extends ChatMessage> getChatMessageClass(byte messageType) {
        return CHAT_MESSAGE_CLASSES.get(messageType);
    }

    @Override
    public void setType() {
        this.type = Type.CHAT.type();
    }

    @Override
    public void setMsgType() {
        this.msgType = MsgType.SINGLE.type();
    }

}

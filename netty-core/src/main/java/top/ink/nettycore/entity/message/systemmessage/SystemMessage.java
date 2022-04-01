package top.ink.nettycore.entity.message.systemmessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ink.nettycore.constant.ContentType;
import top.ink.nettycore.constant.MsgType;
import top.ink.nettycore.constant.Type;
import top.ink.nettycore.entity.message.Message;
import top.ink.nettycore.entity.message.chatmessage.ChatMessage;
import top.ink.nettycore.entity.message.chatmessage.ImageMessage;
import top.ink.nettycore.entity.message.chatmessage.TextMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * desc: SystemMessage
 *
 * @author ink
 * date:2022-02-27 19:59
 */
@EqualsAndHashCode(callSuper = true)
public abstract class SystemMessage extends Message {

    private static final Map<Byte, Class<? extends SystemMessage>> SYSTEM_MESSAGE_CLASSES = new HashMap<>();

    public SystemMessage() {
        this.setContentType();
        this.setType();
    }

    static {
        SYSTEM_MESSAGE_CLASSES.put(MsgType.QUIT.type(), QuitMessage.class);
        SYSTEM_MESSAGE_CLASSES.put(MsgType.INIT.type(), InitMessage.class);
        SYSTEM_MESSAGE_CLASSES.put(MsgType.ACK.type(), AckMessage.class);
    }

    public static Class<? extends SystemMessage> getSystemMessageClass(byte messageType) {
        return SYSTEM_MESSAGE_CLASSES.get(messageType);
    }

    @Override
    public void setType() {
        super.type = Type.SYSTEM.type();
    }

    @Override
    public byte getType() {
        return Type.SYSTEM.type();
    }

    @Override
    public byte getContentType() {
        return ContentType.NULL.type();
    }

    @Override
    public void setContentType() {
        this.contentType = (ContentType.NULL.type());
    }
}

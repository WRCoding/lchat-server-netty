package top.ink.nettycore.entity.message.chatmessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.ink.nettycore.constant.ContentType;

import java.nio.charset.StandardCharsets;

/**
 * desc: TextMessage
 *
 * @author ink
 * date:2022-02-27 20:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class TextMessage extends ChatMessage {

    /**
     * 内容
     */
    private String content;

    /**
     * 发生方
     */
    private String sender;

    /**
     * 接收方
     */
    private String receiver;


    @Override
    public byte getContentType() {
        return ContentType.TEXT.type();
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' + super.toString() +
                '}';
    }
}

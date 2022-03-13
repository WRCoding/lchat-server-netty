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

    public TextMessage() {
        super();
        this.setContentType();
    }

    @Override
    public void setContentType() {
        this.contentType = ContentType.TEXT.type();
    }


    @Override
    public String toString() {
        return "TextMessage{" +
                "seqId='" + seqId + '\'' +
                ", type=" + type +
                ", msgType=" + msgType +
                ", contentType=" + contentType +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                '}';
    }
}

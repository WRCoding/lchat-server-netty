package top.ink.nettycore.entity.message.chatmessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ink.nettycore.constant.ContentType;

/**
 * desc: ImageMessage
 *
 * @author ink
 * date:2022-02-27 20:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageMessage extends ChatMessage{

    /** 图片路径 */
    private String content;

    /** 发生方 */
    private String sender;

    /** 接收方 */
    private String receiver;

    public ImageMessage() {
        super();
        this.setContentType();
    }

    @Override
    public void setContentType() {
        this.contentType = ContentType.IMAGE.type();
    }

    @Override
    public String toString() {
        return "ImageMessage{" +
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

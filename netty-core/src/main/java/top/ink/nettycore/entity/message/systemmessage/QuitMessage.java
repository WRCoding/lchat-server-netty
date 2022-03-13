package top.ink.nettycore.entity.message.systemmessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ink.nettycore.constant.MsgType;

/**
 * desc: QuitMessage
 *
 * @author ink
 * date:2022-02-27 20:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuitMessage extends SystemMessage{

    /** 发生方 */
    private String sender;

    /** 接收方 */
    private String receiver;

    public QuitMessage() {
        super();
        this.setMsgType();
    }

    @Override
    public void setMsgType() {
        this.msgType = MsgType.QUIT.type();
    }

    @Override
    public String toString() {
        return "QuitMessage{" +
                "seqId='" + seqId + '\'' +
                ", type=" + type +
                ", msgType=" + msgType +
                ", contentType=" + contentType +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                '}';
    }
}

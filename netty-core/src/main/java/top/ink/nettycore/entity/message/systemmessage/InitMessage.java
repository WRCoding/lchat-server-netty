package top.ink.nettycore.entity.message.systemmessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ink.nettycore.constant.MsgType;

/**
 * desc: InitMessage
 *
 * @author ink
 * date:2022-02-27 20:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InitMessage extends SystemMessage{

    /** 发生方 */
    private String sender;

    /** 接收方 */
    private String receiver;

    public InitMessage() {
        super();
        this.setMsgType();
    }

    @Override
    public void setMsgType() {
        super.msgType = MsgType.INIT.type();
    }

    @Override
    public String toString() {
        return "InitMessage{" +
                "msgSeq='" + msgSeq + '\'' +
                ", type=" + type +
                ", msgType=" + msgType +
                ", contentType=" + contentType +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                '}';
    }
}

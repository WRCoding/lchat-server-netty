package top.ink.nettycore.entity.message.systemmessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ink.nettycore.constant.MsgType;

/**
 * desc: AckMessage
 *
 * @author ink
 * date:2022-02-27 20:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AckMessage extends SystemMessage{

    public AckMessage() {
        super();
        this.setMsgType();
    }

    @Override
    public void setMsgType() {
        this.msgType = MsgType.ACK.type();
    }

    @Override
    public String toString() {
        return "AckMessage{" +
                "msgSeq=" + msgSeq +
                ", type=" + type +
                ", msgType=" + msgType +
                ", contentType=" + contentType +
                '}';
    }
}

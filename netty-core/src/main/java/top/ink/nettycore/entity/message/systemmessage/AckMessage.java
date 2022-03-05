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

    /** 发生方 */
    private String sender;

    /** 接收方 */
    private String receiver;

    @Override
    public byte getMsgType() {
        return MsgType.ACK.type();
    }
}

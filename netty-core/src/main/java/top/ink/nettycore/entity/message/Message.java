package top.ink.nettycore.entity.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ink.nettycore.entity.BaseEntity;

/**
 * desc: message
 * @author ink
 * date:2022-02-22 23:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class Message extends BaseEntity {
    /** 序列号 */
    private String seqId;

    /** 类型type：Chat,System */
    private byte type;

    /** 消息类型 */
    private byte msgType;

    /** 内容类型 */
    private byte contentType;


    /**
     * Description: 返回类型
     * @return byte
     * Author: ink
     * Date: 2022/2/27
    */
    public abstract byte getType();

    /**
     * Description: 返回消息类型
     * @return byte
     * Author: ink
     * Date: 2022/2/27
    */
    public abstract byte getMsgType();

    /**
     * Description: 返回内容类型
     * @return byte
     * Author: ink
     * Date: 2022/2/27
     */
    public abstract byte getContentType();



}

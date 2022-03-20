package top.ink.nettycore.entity.message;

import lombok.EqualsAndHashCode;
import top.ink.nettycore.entity.BaseEntity;

/**
 * desc: message
 * @author ink
 * date:2022-02-22 23:21
 */
@EqualsAndHashCode(callSuper = true)
public abstract class Message extends BaseEntity {
    /** 序列号 */
    protected String msgSeq;

    /** 类型type：Chat,System */
    protected byte type;

    /** 消息类型 */
    protected byte msgType;

    /** 内容类型 */
    protected byte contentType;


    /**
     * Description: 设置seqId
     * @param msgSeq
     * return void
     * Author: ink
     * Date: 2022/3/6
    */
    public void setMsgSeq(String msgSeq){
        this.msgSeq = msgSeq;
    }

    /**
     * Description: 返回seqId
     * @return byte
     * Author: ink
     * Date: 2022/3/6
     */
    public String getMsgSeq(){
        return this.msgSeq;
    }

    /**
     * Description: 返回类型
     * Author: ink
     * Date: 2022/2/27
    */
    public abstract void setType();


    /**
     * Description: 返回类型
     * @return byte
     * Author: ink
     * Date: 2022/2/27
    */
    public byte getType(){
        return type;
    }

    /**
     * Description: 设置消息类型
     * @return byte
     * Author: ink
     * Date: 2022/2/27
     */
    public abstract void setMsgType();

    /**
     * Description: 返回消息类型
     * @return byte
     * Author: ink
     * Date: 2022/2/27
    */
    public byte getMsgType(){
        return msgType;
    }

    /**
     * Description: 设置内容类型
     * @return byte
     * Author: ink
     * Date: 2022/2/27
     */
    public abstract void setContentType();

    /**
     * Description: 返回内容类型
     * @return byte
     * Author: ink
     * Date: 2022/2/27
     */
    public byte getContentType(){
        return contentType;
    }



}

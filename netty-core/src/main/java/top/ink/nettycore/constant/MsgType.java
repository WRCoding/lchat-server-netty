package top.ink.nettycore.constant;

/**
 * desc: MsgType-Enum
 *
 * @author ink
 * date:2022-02-27 20:34
 */
public enum MsgType {

    /** 单聊 */
    SINGLE(0),

    /** 群聊 */
    GROUP(2),

    /** 退出 */
    QUIT(4),

    /** 初始化 */
    INIT(6),

    /** ACK */
    ACK(8);

    private byte type;

    MsgType(int type) {
        this.type = (byte) type;
    }

    public byte type() {
        return type;
    }
}
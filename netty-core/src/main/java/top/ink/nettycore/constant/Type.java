package top.ink.nettycore.constant;

/**
 * desc: Type-Enum
 *
 * @author ink
 * date:2022-02-27 20:38
 */
public enum Type {

    /** 聊天 */
    CHAT(0),

    /** 系统 */
    SYSTEM(2);


    private byte type;

    Type(int type) {
        this.type = (byte) type;
    }

    public byte type() {
        return type;
    }
}

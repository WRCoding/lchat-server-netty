package top.ink.nettycore.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * desc: decoder
 * @author ink
 * date:2022-02-28 22:20
 */
public class MessageFrameDecoder extends LengthFieldBasedFrameDecoder {

    public MessageFrameDecoder(){
        this(1024,12,4,0,0);
    }

    public MessageFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}

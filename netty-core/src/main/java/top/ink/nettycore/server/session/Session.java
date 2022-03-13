package top.ink.nettycore.server.session;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * desc: 会话管理
 *
 * @author ink
 * date:2022-03-06 16:01
 */
@Service
public interface Session {

    /**
     * Description: 注册会话
     * @param channel
     * @param lid
     * return void
     * Author: ink
     * Date: 2022/3/6
    */
    void register(Channel channel, String lid);

    /**
     * Description: 取消会话
     * @param lid
     * return void
     * Author: ink
     * Date: 2022/3/6
    */
    void unRegister(String lid);
}

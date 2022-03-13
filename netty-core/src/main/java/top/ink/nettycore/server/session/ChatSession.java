package top.ink.nettycore.server.session;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.ink.nettycore.util.RedisUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;


/**
 * desc: 会话管理
 *
 * @author ink
 * date:2022-03-06 16:07
 */
@Slf4j
@Service
public class ChatSession implements Session {

    @Resource
    private RedisUtil redisUtil;


    @Override
    public void register(Channel channel, String lid) {
        NioSocketChannel sc = (NioSocketChannel) channel;
        InetSocketAddress address = sc.remoteAddress();
        log.info("用户: {}, ip: {}, port:{} 登录", lid, address.getHostString(), address.getPort());
    }

    @Override
    public void unRegister(String lid) {
        redisUtil.delete(lid);
    }
}

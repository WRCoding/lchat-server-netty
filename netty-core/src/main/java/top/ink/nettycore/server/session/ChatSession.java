package top.ink.nettycore.server.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.ink.nettycore.entity.message.Message;
import top.ink.nettycore.entity.message.systemmessage.InitMessage;
import top.ink.nettycore.util.RedisUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;


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

    private final ConcurrentHashMap<String,Channel> idChannelMap = new ConcurrentHashMap<>();


    @Override
    public void register(Channel channel, Message message) {
        InitMessage initMessage = (InitMessage) message;
        NioSocketChannel sc = (NioSocketChannel) channel;
        String[] ids = channel.id().asLongText().split("-");
        StringBuffer channelId = new StringBuffer(ids[0]);
        channelId.append("-").append(ids[3]).append("-").append(ids[4]);
        InetSocketAddress address = sc.remoteAddress();
        log.info("用户: {}, ip: {}, port:{} 登录, channelId: {}",initMessage.getSender(), address.getHostString(), address.getPort(), channelId);
        redisUtil.valuePut(initMessage.getSender(),channelId.toString());
        idChannelMap.put(channelId.toString(),channel);
    }

    @Override
    public void unRegister(String lid) {
        redisUtil.delete(lid);
    }

    @Override
    public Channel getSession(String lid) {
        String channelId = redisUtil.valueGet(lid);
        return idChannelMap.get(channelId);
    }

    @Override
    public boolean exist(String lid) {
        return redisUtil.hasKey(lid);
    }
}

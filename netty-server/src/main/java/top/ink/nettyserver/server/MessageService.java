package top.ink.nettyserver.server;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ink.nettycore.server.session.ChatSession;
import top.ink.nettycore.server.session.Session;
import top.ink.nettyserver.entity.common.Response;
import top.ink.nettyserver.entity.message.Message;
import top.ink.nettyserver.mapper.MessageMapper;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * desc: 消息类
 *
 * @author ink
 * date:2022-03-31 22:38
 */
@Service
@Slf4j
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private Session session;

    private final ConcurrentHashMap<String, Integer> waitAckLidMap = new ConcurrentHashMap<>();

    private boolean START = false;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public Response<List<Message>> getOffLineMessage(String lid) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper();
        queryWrapper.eq("receiver", lid);
        List<Message> messages = messageMapper.selectList(queryWrapper);
        waitAckLidMap.put(lid, 1);
        if (!START){
            execute();
        }
        return Response.success(messages);
    }

    private synchronized void execute() {
        if (!START){
            START = true;
            executor.scheduleAtFixedRate(() -> {
                if (!waitAckLidMap.isEmpty()){
                    Iterator<Map.Entry<String, Integer>> iterator = waitAckLidMap.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<String, Integer> elem = iterator.next();
                        if (elem.getValue() == 0){
                            iterator.remove();
                        }else{
                            elem.setValue(elem.getValue() - 1);
                            session.notify(elem.getKey(),"AOL:" + elem.getKey() + ":server");
                        }
                    }
                }
            },5, 2, TimeUnit.SECONDS);
        }
    }

    /**
     * Description: 确认离线消息已经收到
     *
     * @param lid return void
     *            Author: ink
     *            Date: 2022/3/31
     */
    public void confirm(String lid) {
        log.info("确认: {}, 收到离线消息", lid);
        QueryWrapper<Message> queryWrapper = new QueryWrapper();
        queryWrapper.eq("receiver", lid);
        waitAckLidMap.remove(lid);
        messageMapper.delete(queryWrapper);
    }
}

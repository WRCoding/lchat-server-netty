package top.ink.nettyserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ink.nettyserver.entity.common.Response;
import top.ink.nettyserver.entity.message.Message;
import top.ink.nettyserver.server.MessageService;

import javax.annotation.Resource;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * desc: 获取消息接口
 *
 * @author ink
 * date:2022-03-31 22:38
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;



    @GetMapping("/offLine/{lid}")
    public Response<List<Message>> getOffLineMessage(@PathVariable("lid") String lid){
        return messageService.getOffLineMessage(lid);
    }

    @GetMapping("/confirm/{lid}")
    public void confirm(@PathVariable("lid") String lid){
        messageService.confirm(lid);
    }
}

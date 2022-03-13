package top.ink.nettyserver;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import top.ink.nettycore.server.ChatBootServer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author wanglongjun
 */
@SpringBootApplication
@ComponentScan(basePackages = {"top.ink.nettycore","top.ink.nettyserver"})
@MapperScan(basePackages = {"top.ink.nettyserver.mapper"})
@Slf4j
public class NettyServerApplication {

    @Resource
    private ChatBootServer chatBootServer;

    private static ChatBootServer server;

    @PostConstruct
    public void init(){
        server = chatBootServer;
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(NettyServerApplication.class, args);
        server.init();
    }

}

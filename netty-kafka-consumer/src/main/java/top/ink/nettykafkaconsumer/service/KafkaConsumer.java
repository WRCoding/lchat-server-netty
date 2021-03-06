package top.ink.nettykafkaconsumer.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import top.ink.nettykafkaconsumer.entity.Message;
import top.ink.nettykafkaconsumer.mapper.MessageMapper;

import javax.annotation.Resource;

/**
 * desc: kafka消费者
 *
 * @author ink
 * date:2022-03-30 22:02
 */
@Service
@Slf4j
public class KafkaConsumer {

    @Resource
    private MessageMapper messageMapper;

    @KafkaListener(topics = {"chat-netty"})
    public void consumer(ConsumerRecord<String, String> record){
        log.info("receive: topic:{}, partition:{}, value:{},offset:{}", record.topic(), record.partition(), record.value(),
                record.offset());
        Message message = JSON.parseObject(record.value(), Message.class);
        if (message != null) {
            log.info("消费Kafka: {}", message);
            messageMapper.insert(message);
        }
    }
}

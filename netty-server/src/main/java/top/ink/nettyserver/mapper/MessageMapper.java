package top.ink.nettyserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ink.nettyserver.entity.message.Message;

/**
 * desc: 消息Mapper
 *
 * @author ink
 * date:2022-03-30 22:26
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}

package top.ink.nettyserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ink.nettyserver.entity.user.User;

/**
 * desc: 用户mapper
 *
 * @author ink
 * date:2022-03-13 10:22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

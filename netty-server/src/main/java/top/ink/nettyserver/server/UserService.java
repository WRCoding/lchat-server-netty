package top.ink.nettyserver.server;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.ink.nettycore.util.RedisUtil;
import top.ink.nettyserver.LcIdGenerate;
import top.ink.nettyserver.entity.common.Response;
import top.ink.nettyserver.entity.common.ResponseCode;
import top.ink.nettyserver.entity.dto.LoginDTO;
import top.ink.nettyserver.entity.dto.UserDTO;
import top.ink.nettyserver.entity.user.User;
import top.ink.nettyserver.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.Date;

/**
 * desc: 用户相关类
 *
 * @author ink
 * date:2022-03-13 10:07
 */
@Service
@Slf4j
public class UserService {

    @Resource
    private UserMapper userMapper;


    /**
     * Description: 注册用户
     * @param loginDTO
     * return top.ink.nettyserver.entity.common.Response<top.ink.nettyserver.entity.dto.UserDTO>
     * Author: ink
     * Date: 2022/3/13
    */
    public Response<UserDTO> register(LoginDTO loginDTO) {
        if (!StringUtils.hasText(loginDTO.getUserName()) || !StringUtils.hasText(loginDTO.getPassWord())) {
            return Response.error(ResponseCode.PARAM_EMPTY, "用户名或密码不能为空");
        }
        User user = new User();
        UserDTO userDTO = new UserDTO();
        user.setUserName(loginDTO.getUserName());
        user.setPassword(loginDTO.getPassWord());
        //设置默认头像
        user.setAvatar("https://lchat-server.oss-cn-shenzhen.aliyuncs.com/avatar/default/avatar.jpg");
        //设置默认背景图
        user.setBackground("https://lchat-server.oss-cn-shenzhen.aliyuncs.com/background/default/background.jpg");
        user.setLid(LcIdGenerate.generateUserId());
        userMapper.insert(user);
        BeanUtils.copyProperties(user,userDTO);
        log.info("register userDTO: {}",userDTO);
        return Response.success(userDTO);
    }



    /**
     * Description: 登录
     * @param loginDTO
     * return top.ink.nettyserver.entity.common.Response<top.ink.nettyserver.entity.dto.UserDTO>
     * Author: ink
     * Date: 2022/3/13
    */
    public Response<UserDTO> login(LoginDTO loginDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", loginDTO.getUserName());
        final User user = userMapper.selectOne(queryWrapper);
        if (user == null || !user.getPassword().equals(loginDTO.getPassWord())) {
            return Response.error(ResponseCode.PARAM_FAIL, "用户名或者密码错误");
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        userDTO.setDays(calDays(user));
        log.info("login userDTO: {}", userDTO);
        return Response.success(userDTO);
    }

    private int calDays(User user) {
        long betweenDay = DateUtil.between(new Date(user.getCreated()), new Date(), DateUnit.DAY);
        return betweenDay == 0 ? 1 : (int) betweenDay;
    }
}
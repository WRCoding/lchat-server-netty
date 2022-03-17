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
import top.ink.nettyserver.entity.dto.FriendDTO;
import top.ink.nettyserver.entity.dto.LoginDTO;
import top.ink.nettyserver.entity.dto.UserDTO;
import top.ink.nettyserver.entity.user.Friend;
import top.ink.nettyserver.entity.user.User;
import top.ink.nettyserver.mapper.FriendMapper;
import top.ink.nettyserver.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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


    @Resource
    private FriendMapper friendMapper;

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
        UserDTO userDTO = UserDTO.copy(user);
        userDTO.setDays(calDays(user));
        log.info("login userDTO: {}", userDTO);
        return Response.success(userDTO);
    }

    private int calDays(User user) {
        long betweenDay = DateUtil.between(new Date(user.getCreated()), new Date(), DateUnit.DAY);
        return betweenDay == 0 ? 1 : (int) betweenDay;
    }

    /**
     * Description: 搜索用户
     * @param key
     * return top.ink.nettyserver.entity.common.Response<java.util.List<top.ink.nettyserver.entity.dto.UserDTO>>
     * Author: ink
     * Date: 2022/3/13
    */
    public Response<List<UserDTO>> search(String key) {
        if (!StringUtils.hasText(key)) {
            return Response.error(ResponseCode.ERROR, "关键字不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", key);
        List<User> list = userMapper.selectList(queryWrapper);
        return Response.success(UserDTO.copyList(list));
    }

    /**
     * Description: 添加好友
     * @param friendDTO
     * return top.ink.nettyserver.entity.common.Response<top.ink.nettyserver.entity.dto.UserDTO>
     * Author: ink
     * Date: 2022/3/14
    */
    public Response<List<UserDTO>> addFriend(FriendDTO friendDTO) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lid",friendDTO.getLid()).eq("friend_lid",friendDTO.getFriendLid());
        Friend one = friendMapper.selectOne(queryWrapper);
        if (one != null){
            return Response.error(ResponseCode.PARAM_FAIL,"已经是好友");
        }
        friendMapper.insert(Friend.copy(friendDTO));
        return Response.success(getFriendInfoByLid(friendDTO.getLid()));
    }

    private List<UserDTO> getFriendInfoByLid(String lid) {
        List<String> friendLids = friendMapper.findFriendsByLid(lid);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("lid", friendLids);
        return UserDTO.copyList(userMapper.selectList(queryWrapper));
    }

    /**
     * Description: 获取lid的所有好友信息
     * @param lid
     * return top.ink.nettyserver.entity.common.Response<java.util.List<top.ink.nettyserver.entity.dto.UserDTO>>
     * Author: ink
     * Date: 2022/3/17
    */
    public Response<List<UserDTO>> friends(String lid) {
        if (!StringUtils.hasText(lid)){
            return Response.error(ResponseCode.PARAM_EMPTY,"lid为空");
        }
        return Response.success(getFriendInfoByLid(lid));
    }
}

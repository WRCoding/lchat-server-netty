package top.ink.nettyserver.server;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
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
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Value("${endpoint}")
    private String endPoint;
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;
    @Value("${bucket}")
    private String bucket;

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


    public Response<UserDTO> uploadFile(MultipartFile file, String lid, boolean flag) {
        String url = handlerFile(file, flag);
        if (!StringUtils.hasText(url)) {
            return Response.error(ResponseCode.ERROR, flag ? "背景图" : "头像" + "上传失败");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lid", lid);
        final User user = userMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(user)) {
            return Response.error(ResponseCode.PARAM_FAIL, "该用户不存在");
        }
        if (flag) {
            user.setBackground(url);
        } else {
            user.setAvatar(url);
        }
        return update(user);
    }

    public Response<UserDTO> update(User user) {
        userMapper.updateById(user);
        UserDTO userDTO = UserDTO.copy(user);
        userDTO.setDays(calDays(user));
        return Response.success(userDTO);
    }

    private String handlerFile(MultipartFile file, boolean flag) {
        String url = "";
        if (file != null) {
            String originalFilename = "";
            if (file.getOriginalFilename() != null && !"".equals(originalFilename = file.getOriginalFilename())) {
                File localFile = new File(originalFilename);
                try (FileOutputStream outputStream = new FileOutputStream(localFile)) {
                    outputStream.write(file.getBytes());
                    file.transferTo(localFile);
                    url = uploadLocalFileToOSS(localFile, flag);
                } catch (IOException e) {
                    log.error("handlerFileList cause {}", e.getMessage());
                } finally {
                    if (!localFile.delete()) {
                        log.error("本地文件删除失败: {}", localFile.getName());
                    }
                }
            }
        }
        return url;
    }

    private String uploadLocalFileToOSS(File localFile, boolean flag) {
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId,
                accessKeySecret);
        boolean isImage = true;
        try {
            BufferedImage image = ImageIO.read(localFile);
            isImage = image != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        String filePre = flag ? "background" : "avatar";
        String fileAddress = filePre + "/" + dateStr + "/"
                + UUID.randomUUID().toString().replace("-", "")
                + "-" + localFile.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileAddress, localFile);
        String fileUrl;
        if (isImage) {
            fileUrl = "https://" + bucket + "." + endPoint + "/" + fileAddress;
        } else {
            fileUrl = "非图片 不可预览 文件路径为: " + fileAddress;
        }
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        ossClient.setBucketAcl(bucket, CannedAccessControlList.PublicRead);
        if (result != null) {
            log.info("OSS文件上传成功，URL: {}", fileUrl);
        }
        ossClient.shutdown();
        return fileUrl;
    }

    public Response<UserDTO> updateUser(String lid, String name, String desc) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lid", lid);
        final User user = userMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(user)) {
            return Response.error(ResponseCode.PARAM_FAIL, "该用户不存在");
        }
        user.setDescription(desc);
        user.setUserName(name);
        return update(user);
    }

}

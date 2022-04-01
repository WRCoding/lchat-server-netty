package top.ink.nettyserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ink.nettyserver.entity.common.Response;
import top.ink.nettyserver.entity.dto.FriendDTO;
import top.ink.nettyserver.entity.dto.LoginDTO;
import top.ink.nettyserver.entity.dto.UserDTO;
import top.ink.nettyserver.entity.user.Friend;
import top.ink.nettyserver.entity.user.User;
import top.ink.nettyserver.server.UserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * desc: 用户接口
 * @author ink
 * date:2022-03-05 20:49
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Response<UserDTO> register(@RequestBody LoginDTO loginDTO){
        return userService.register(loginDTO);
    }

    @PostMapping("/login")
    public Response<UserDTO> login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }

    @GetMapping("/search")
    public Response<List<UserDTO>> search(String key){
        return userService.search(key);
    }

    @PostMapping("/addFriend")
    public Response<List<UserDTO>> addFriend(@RequestBody FriendDTO friendDTO){
        return userService.addFriend(friendDTO);
    }

    @GetMapping("/friends")
    public Response<List<UserDTO>> friends(String lid){
        return userService.friends(lid);
    }

    @PostMapping("/uploadFile")
    public Response<UserDTO> uploadFile(MultipartFile file, String lid, boolean flag){
        return userService.uploadFile(file,lid,flag);
    }

    @PostMapping("/update")
    public Response<UserDTO> updateUser(String lid, String name, String desc){
        log.info("lid: {}, name: {}, desc: {}", lid, name, desc);
        return userService.updateUser(lid, name, desc);
    }

}

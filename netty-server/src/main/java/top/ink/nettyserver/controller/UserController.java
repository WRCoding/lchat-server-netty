package top.ink.nettyserver.controller;

import org.springframework.web.bind.annotation.*;
import top.ink.nettyserver.entity.common.Response;
import top.ink.nettyserver.entity.dto.LoginDTO;
import top.ink.nettyserver.entity.dto.UserDTO;
import top.ink.nettyserver.entity.user.User;
import top.ink.nettyserver.server.UserService;

import javax.annotation.Resource;

/**
 * desc: 用户接口
 * @author ink
 * date:2022-03-05 20:49
 */
@RestController
@RequestMapping("/user")
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

}

package top.ink.nettyserver.entity.dto;

import lombok.Data;


/**
 * @author 林北
 * @description 用户信息实体
 * @date 2021-08-07 16:24
 */

@Data
public class UserDTO {

    private String lid;

    private String userName;

    private String avatar;

    private String description;

    private String background;

    private Integer days;
}

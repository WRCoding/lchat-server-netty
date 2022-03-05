package top.ink.nettyserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.ink.nettycore.entity.BaseEntity;


/**
 * @author 林北
 * @description 用户信息实体
 * @date 2021-08-07 16:24
 */

@Data
@TableName("ink_user_info")
public class UserInfo extends BaseEntity {

    private String lid;

    private String userName;

    private String password;

    private String avatar;

    private String description;

    private String background;

    @TableField(exist = false)
    private Integer days;
}

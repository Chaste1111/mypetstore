package csu.web.mypetstore.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户账户实体类
 * 对应数据库表：ACCOUNT, PROFILE, SIGNON（用户账户信息分布在多个表中）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("ACCOUNT")
public class Account implements Serializable {

    private static final long serialVersionUID = 8751282105532159742L;

    /**
     * 用户名，主键
     */
    @TableId(value = "USERID", type = IdType.INPUT)
    private String username;

    /**
     * 密码（存储在 SIGNON 表）
     */
    @TableField(exist = false)
    private String password;

    /**
     * 邮箱地址
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 名字
     */
    @TableField("FIRSTNAME")
    private String firstName;

    /**
     * 姓氏
     */
    @TableField("LASTNAME")
    private String lastName;

    /**
     * 账户状态
     */
    @TableField("STATUS")
    private String status;

    /**
     * 地址1
     */
    @TableField("ADDR1")
    private String address1;

    /**
     * 地址2
     */
    @TableField("ADDR2")
    private String address2;

    /**
     * 城市
     */
    @TableField("CITY")
    private String city;

    /**
     * 州/省份
     */
    @TableField("STATE")
    private String state;

    /**
     * 邮政编码
     */
    @TableField("ZIP")
    private String zip;

    /**
     * 国家
     */
    @TableField("COUNTRY")
    private String country;

    /**
     * 电话号码
     */
    @TableField("PHONE")
    private String phone;

    /**
     * 收藏的商品分类ID（存储在 PROFILE 表）
     */
    @TableField(exist = false)
    private String favoriteCategoryId;

    /**
     * 语言偏好（存储在 PROFILE 表）
     */
    @TableField(exist = false)
    private String languagePreference;

    /**
     * 是否显示我的列表（存储在 PROFILE 表）
     */
    @TableField(exist = false)
    private boolean listOption;

    /**
     * 是否显示横幅（存储在 PROFILE 表）
     */
    @TableField(exist = false)
    private boolean bannerOption;

    /**
     * 横幅名称（存储在 BANNERDATA 表）
     */
    @TableField(exist = false)
    private String bannerName;
}
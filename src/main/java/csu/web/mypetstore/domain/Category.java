package csu.web.mypetstore.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品分类实体类
 * 对应数据库表：CATEGORY
 */
@Data
@TableName("CATEGORY")
public class Category implements Serializable {

    private static final long serialVersionUID = 3992469837058393712L;

    /**
     * 分类ID，主键
     */
    @TableId(value = "CATEGORYID", type = IdType.INPUT)
    private String categoryId;

    /**
     * 分类名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 分类描述
     */
    @TableField("`DESC`")
    private String description;
}
package csu.web.mypetstore.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品实体类
 * 对应数据库表：PRODUCT
 */
@Data
@TableName("PRODUCT")
public class Product implements Serializable {

    private static final long serialVersionUID = -7492639752670189553L;

    /**
     * 产品ID，主键
     */
    @TableId(value = "PRODUCTID", type = IdType.INPUT)
    private String productId;

    /**
     * 分类ID
     */
    @TableField("CATEGORY")
    private String categoryId;

    /**
     * 产品名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 产品描述
     */
    @TableField("DESC")
    private String description;
}
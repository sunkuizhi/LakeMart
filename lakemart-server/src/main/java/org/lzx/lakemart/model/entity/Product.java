package org.lzx.lakemart.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Getter
@Setter
@ToString
@TableName("tb_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 商品描述
     */
    @TableField("description")
    private String description;

    /**
     * 售价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 库存
     */
    @TableField("stock")
    private Integer stock;

    /**
     * 所属分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 商品主图URL
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 1上架 0下架
     */
    @TableField("status")
    private Integer status;

    /**
     * 销量（冗余，便于排序）
     */
    @TableField("sales_count")
    private Integer salesCount;

    /**
     * 上架时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}

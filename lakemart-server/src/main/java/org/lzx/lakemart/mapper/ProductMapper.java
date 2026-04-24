package org.lzx.lakemart.mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.lakemart.model.entity.Product;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // ProductMapper.java
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
    void increaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}

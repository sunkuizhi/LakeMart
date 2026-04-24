package org.lzx.lakemart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.lakemart.model.entity.CartItem;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {

}

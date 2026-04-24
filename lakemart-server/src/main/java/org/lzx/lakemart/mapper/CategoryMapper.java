package org.lzx.lakemart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.lakemart.model.entity.Category;

/**
 * <p>
 * 商品分类表 Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}

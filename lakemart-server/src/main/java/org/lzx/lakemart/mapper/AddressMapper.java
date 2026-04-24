package org.lzx.lakemart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.lakemart.model.entity.Address;

/**
 * <p>
 * 收货地址表 Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2026-04-21
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {

}

package org.lzx.lakemart.mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.lakemart.model.entity.User;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // UserMapper.java
    void addPoints(@Param("userId") Long userId, @Param("points") Integer points);
    // UserMapper.java
    Integer selectPointsByUserId(@Param("userId") Long userId);
    void updatePoints(@Param("userId") Long userId, @Param("points") Integer points);
}

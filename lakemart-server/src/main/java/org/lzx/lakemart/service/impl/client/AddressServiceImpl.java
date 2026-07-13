package org.lzx.lakemart.service.impl.client;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.AddressMapper;
import org.lzx.lakemart.model.entity.Address;
import org.lzx.lakemart.model.vo.AddressVO;
import org.lzx.lakemart.service.client.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    /**
     * 添加收货地址
     * @param userId   用户ID
     * @param address  地址信息（不含userId）
     */
    @Override
    @Transactional
    public void addAddress(Long userId, Address address) {
        address.setUserId(userId);
        address.setCreateTime(LocalDateTime.now());
        // 如果新增的地址是默认地址，需要清除该用户其他地址的默认标记
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefaultFlag(userId, null);
        } else if (address.getIsDefault() == null) {
            // 如果没指定是否默认，则默认非默认
            address.setIsDefault(0);
        }
        baseMapper.insertAddress(address);
    }

    /**
     * 更新收货地址（需校验所有权）
     * @param userId   用户ID
     * @param address  待更新的地址信息（必须包含id）
     */
    @Override
    @Transactional
    public void updateAddress(Long userId, Address address) {
        // 校验地址存在且属于该用户
        Address exist = baseMapper.selectById(address.getId());
        if (exist == null || !exist.getUserId().equals(userId)) {
            throw new RuntimeException("地址不存在或无权限");
        }
        // 更新允许修改的字段
        if (address.getReceiverName() != null) exist.setReceiverName(address.getReceiverName());
        if (address.getReceiverPhone() != null) exist.setReceiverPhone(address.getReceiverPhone());
        if (address.getProvince() != null) exist.setProvince(address.getProvince());
        if (address.getCity() != null) exist.setCity(address.getCity());
        if (address.getDistrict() != null) exist.setDistrict(address.getDistrict());
        if (address.getDetailAddress() != null) exist.setDetailAddress(address.getDetailAddress());
        // 处理默认标记：如果要设为默认，需清除该用户其他默认地址
        if (address.getIsDefault() != null) {
            if (address.getIsDefault() == 1) {
                clearDefaultFlag(userId, address.getId());
            }
            exist.setIsDefault(address.getIsDefault());
        }
        baseMapper.updateById(exist);
    }

    /**
     * 删除收货地址（需校验所有权）
     * @param userId     用户ID
     * @param addressId  地址ID
     */
    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address exist = baseMapper.selectById(addressId);
        if (exist == null || !exist.getUserId().equals(userId)) {
            throw new RuntimeException("地址不存在或无权限");
        }
        baseMapper.deleteById(addressId);
    }
    /**
     * 获取用户的所有收货地址列表（按默认优先、创建时间倒序）
     * @param userId 用户ID
     * @return 地址VO列表
     */
    @Override
    public List<AddressVO> getUserAddresses(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreateTime);
        List<Address> list = baseMapper.selectList(wrapper);
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    /**
     * 获取用户的默认收货地址
     * @param userId 用户ID
     * @return 地址VO，可能为null
     */
    @Override
    public AddressVO getDefaultAddress(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId).eq(Address::getIsDefault, 1);
        Address address = baseMapper.selectOne(wrapper);
        return address == null ? null : toVO(address);
    }

    /**
     * 设置指定地址为默认地址（其他地址自动变为非默认）
     * @param userId    用户ID
     * @param addressId 地址ID
     */
    @Override
    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        // 先将该用户所有地址的 is_default 设为 0
        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Address::getUserId, userId).set(Address::getIsDefault, 0);
        baseMapper.update(null, wrapper);
        // 再将指定地址设为默认
        Address address = baseMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("地址不存在或无权限");
        }
        address.setIsDefault(1);
        baseMapper.updateById(address);
    }

    // ---------- 辅助方法 ----------
    /**
     * 清除指定用户的所有默认地址标记
     * @param userId    用户ID
     * @param excludeId 排除的地址ID（用于更新时避免清除当前正在修改的地址）
     */
    private void clearDefaultFlag(Long userId, Long excludeId) {
        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Address::getUserId, userId).eq(Address::getIsDefault, 1);
        if (excludeId != null) {
            wrapper.ne(Address::getId, excludeId);
        }
        Address update = new Address();
        update.setIsDefault(0);
        baseMapper.update(update, wrapper);
    }

    private AddressVO toVO(Address address) {
        return AddressVO.builder()
                .id(address.getId())
                .userId(address.getUserId())
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .province(address.getProvince())
                .city(address.getCity())
                .district(address.getDistrict())
                .detailAddress(address.getDetailAddress())
                .isDefault(address.getIsDefault())
                .createTime(address.getCreateTime() != null ? address.getCreateTime().toString() : null)
                .build();
    }
}
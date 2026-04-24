package org.lzx.lakemart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.entity.Address;
import org.lzx.lakemart.model.vo.AddressVO;
import java.util.List;

public interface AddressService extends IService<Address> {
    void addAddress(Long userId, Address address);
    void updateAddress(Long userId, Address address);   // 只保留带 userId 的安全版本
    void deleteAddress(Long userId, Long addressId);    // 参数顺序 userId 在前
    List<AddressVO> getUserAddresses(Long userId);
    AddressVO getDefaultAddress(Long userId);
    void setDefaultAddress(Long userId, Long addressId);
}
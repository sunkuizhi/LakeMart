package org.lzx.lakemart.controller;

import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.AddressAddRequest;
import org.lzx.lakemart.model.dto.AddressUpdateRequest;
import org.lzx.lakemart.model.entity.Address;
import org.lzx.lakemart.model.vo.AddressVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/add")
    public Result<String> addAddress(@AuthenticationPrincipal Long userId,
                                     @Valid @RequestBody AddressAddRequest request) {
        Address address = new Address();
        address.setReceiverName(request.getReceiverName());
        address.setReceiverPhone(request.getReceiverPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetailAddress(request.getDetailAddress());
        address.setIsDefault(request.getIsDefault());
        addressService.addAddress(userId, address);
        return Result.success("地址添加成功");
    }

    @PutMapping("/update")
    public Result<String> updateAddress(@AuthenticationPrincipal Long userId,
                                        @Valid @RequestBody AddressUpdateRequest request) {
        // 先检查地址是否属于当前用户（可选，也可以在Service中校验）
        Address address = addressService.getById(request.getId());
        if (address == null || !address.getUserId().equals(userId)) {
            return Result.error("地址不存在或无权限");
        }
        // 只更新非空字段
        if (request.getReceiverName() != null) address.setReceiverName(request.getReceiverName());
        if (request.getReceiverPhone() != null) address.setReceiverPhone(request.getReceiverPhone());
        if (request.getProvince() != null) address.setProvince(request.getProvince());
        if (request.getCity() != null) address.setCity(request.getCity());
        if (request.getDistrict() != null) address.setDistrict(request.getDistrict());
        if (request.getDetailAddress() != null) address.setDetailAddress(request.getDetailAddress());
        if (request.getIsDefault() != null) address.setIsDefault(request.getIsDefault());
        // 调用带 userId 的更新方法
        addressService.updateAddress(userId, address);
        return Result.success("地址更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> deleteAddress(@AuthenticationPrincipal Long userId,
                                        @PathVariable("id") Long id) {
        // 注意参数顺序：userId, addressId
        addressService.deleteAddress(userId, id);
        return Result.success("地址删除成功");
    }

    @GetMapping("/list")
    public Result<List<AddressVO>> getAddressList(@AuthenticationPrincipal Long userId) {
        List<AddressVO> list = addressService.getUserAddresses(userId);
        return Result.success(list);
    }

    @GetMapping("/default")
    public Result<AddressVO> getDefaultAddress(@AuthenticationPrincipal Long userId) {
        AddressVO address = addressService.getDefaultAddress(userId);
        return Result.success(address);
    }

    @PutMapping("/default/{id}")
    public Result<String> setDefaultAddress(@AuthenticationPrincipal Long userId,
                                            @PathVariable("id") Long id) {
        addressService.setDefaultAddress(userId, id);
        return Result.success("设置默认地址成功");
    }
}
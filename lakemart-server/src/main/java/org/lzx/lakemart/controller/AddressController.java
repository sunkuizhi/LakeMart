package org.lzx.lakemart.controller;

import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.AddressAddRequest;
import org.lzx.lakemart.model.dto.AddressUpdateRequest;
import org.lzx.lakemart.model.entity.Address;
import org.lzx.lakemart.model.vo.AddressVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.security.SecurityUser;
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
    public Result<String> addAddress(@AuthenticationPrincipal SecurityUser securityUser,
                                     @Valid @RequestBody AddressAddRequest request) {
        Long userId = securityUser.getId();
        Address address = new Address();
        address.setUserId(userId);
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
    public Result<String> updateAddress(@AuthenticationPrincipal SecurityUser securityUser,
                                        @Valid @RequestBody AddressUpdateRequest request) {
        Long userId = securityUser.getId();
        Address address = addressService.getById(request.getId());
        if (address == null || !address.getUserId().equals(userId)) {
            return Result.error("地址不存在或无权限");
        }
        if (request.getReceiverName() != null) address.setReceiverName(request.getReceiverName());
        if (request.getReceiverPhone() != null) address.setReceiverPhone(request.getReceiverPhone());
        if (request.getProvince() != null) address.setProvince(request.getProvince());
        if (request.getCity() != null) address.setCity(request.getCity());
        if (request.getDistrict() != null) address.setDistrict(request.getDistrict());
        if (request.getDetailAddress() != null) address.setDetailAddress(request.getDetailAddress());
        if (request.getIsDefault() != null) address.setIsDefault(request.getIsDefault());
        addressService.updateAddress(userId, address);
        return Result.success("地址更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> deleteAddress(@AuthenticationPrincipal SecurityUser securityUser,
                                        @PathVariable("id") Long id) {
        Long userId = securityUser.getId();
        addressService.deleteAddress(userId, id);
        return Result.success("地址删除成功");
    }

    @GetMapping("/list")
    public Result<List<AddressVO>> getAddressList(@AuthenticationPrincipal SecurityUser securityUser) {
        Long userId = securityUser.getId();
        List<AddressVO> list = addressService.getUserAddresses(userId);
        return Result.success(list);
    }

    @GetMapping("/default")
    public Result<AddressVO> getDefaultAddress(@AuthenticationPrincipal SecurityUser securityUser) {
        Long userId = securityUser.getId();
        AddressVO address = addressService.getDefaultAddress(userId);
        return Result.success(address);
    }

    @PutMapping("/default/{id}")
    public Result<String> setDefaultAddress(@AuthenticationPrincipal SecurityUser securityUser,
                                            @PathVariable("id") Long id) {
        Long userId = securityUser.getId();
        addressService.setDefaultAddress(userId, id);
        return Result.success("设置默认地址成功");
    }
}
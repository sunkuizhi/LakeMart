package org.lzx.lakemart.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.BannerAddRequest;
import org.lzx.lakemart.model.dto.BannerUpdateRequest;
import org.lzx.lakemart.model.entity.Banner;
import org.lzx.lakemart.model.vo.BannerVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/banner")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/list")
    public Result<Page<BannerVO>> list(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<BannerVO> page = bannerService.adminQueryPage(pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/add")
    public Result<String> add(@Valid @RequestBody BannerAddRequest request) {
        bannerService.addBanner(request);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    public Result<String> update(@Valid @RequestBody BannerUpdateRequest request) {
        bannerService.updateBanner(request);
        return Result.success("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable("id") Long id) {
        bannerService.deleteBanner(id);
        return Result.success("删除成功");
    }

    @PutMapping("/status/{id}")
    public Result<String> updateStatus(@PathVariable("id") Long id, @RequestParam(name = "status") Integer status) {
        bannerService.updateStatus(id, status);
        return Result.success("状态更新成功");
    }
}
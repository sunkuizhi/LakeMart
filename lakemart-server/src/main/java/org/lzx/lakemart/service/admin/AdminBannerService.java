package org.lzx.lakemart.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.dto.BannerAddRequest;
import org.lzx.lakemart.model.dto.BannerUpdateRequest;
import org.lzx.lakemart.model.entity.Banner;
import org.lzx.lakemart.model.vo.BannerVO;

public interface AdminBannerService extends IService<Banner> {
    Page<BannerVO> adminQueryPage(Integer pageNum, Integer pageSize);
    void addBanner(BannerAddRequest request);
    void updateBanner(BannerUpdateRequest request);
    void deleteBanner(Long id);
    void updateStatus(Long id, Integer status);
}
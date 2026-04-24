package org.lzx.lakemart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.BannerMapper;
import org.lzx.lakemart.model.dto.BannerAddRequest;
import org.lzx.lakemart.model.dto.BannerUpdateRequest;
import org.lzx.lakemart.model.entity.Banner;
import org.lzx.lakemart.model.vo.BannerVO;
import org.lzx.lakemart.service.BannerService;
import org.lzx.lakemart.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.lzx.lakemart.util.ByteArrayMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {


    @Override
    public Page<BannerVO> adminQueryPage(Integer pageNum, Integer pageSize) {
        Page<Banner> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Banner::getSortOrder).orderByDesc(Banner::getCreateTime);
        Page<Banner> bannerPage = baseMapper.selectPage(page, wrapper);
        Page<BannerVO> voPage = new Page<>(bannerPage.getCurrent(), bannerPage.getSize(), bannerPage.getTotal());
        voPage.setRecords(bannerPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    @Transactional
    public void addBanner(BannerAddRequest request) {
        Banner banner = new Banner();
        banner.setTitle(request.getTitle());
        banner.setImageUrl(request.getImageUrl());
        banner.setLinkUrl(request.getLinkUrl());
        banner.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        banner.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        banner.setCreateTime(LocalDateTime.now());
        baseMapper.insert(banner);
    }

    @Override
    @Transactional
    public void updateBanner(BannerUpdateRequest request) {
        Banner exist = baseMapper.selectById(request.getId());
        if (exist == null) {
            throw new RuntimeException("轮播图不存在");
        }
        if (request.getTitle() != null) exist.setTitle(request.getTitle());
        if (request.getImageUrl() != null) exist.setImageUrl(request.getImageUrl());
        if (request.getLinkUrl() != null) exist.setLinkUrl(request.getLinkUrl());
        if (request.getSortOrder() != null) exist.setSortOrder(request.getSortOrder());
        if (request.getStatus() != null) exist.setStatus(request.getStatus());
        baseMapper.updateById(exist);
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        try {
            // 先检查是否存在
            Banner banner = baseMapper.selectById(id);
            if (banner == null) {
                throw new RuntimeException("轮播图不存在，id=" + id);
            }
            int rows = baseMapper.deleteById(id);
            if (rows == 0) {
                throw new RuntimeException("删除失败，影响行数为0");
            }
            log.info("成功删除轮播图 id={}", id);
        } catch (Exception e) {
            log.error("删除轮播图失败 id={}, error={}", id, e.getMessage(), e);
            throw new RuntimeException("删除轮播图失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Banner banner = new Banner();
        banner.setId(id);
        banner.setStatus(status);
        baseMapper.updateById(banner);
    }

    private BannerVO toVO(Banner banner) {
        return BannerVO.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .imageUrl(banner.getImageUrl())
                .linkUrl(banner.getLinkUrl())
                .sortOrder(banner.getSortOrder())
                .status(banner.getStatus())
                .createTime(banner.getCreateTime())
                .build();
    }

    @Override
    public List<BannerVO> getEnabledBanners() {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Banner::getStatus, 1)
                .orderByAsc(Banner::getSortOrder)
                .orderByDesc(Banner::getCreateTime);
        List<Banner> list = list(wrapper);
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }
}
package org.lzx.lakemart.service.impl.client;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.BannerMapper;
import org.lzx.lakemart.model.entity.Banner;
import org.lzx.lakemart.model.vo.BannerVO;
import org.lzx.lakemart.service.client.ClientBannerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientBannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements ClientBannerService {

    @Override
    public List<BannerVO> getEnabledBanners() {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Banner::getStatus, 1)
                .orderByAsc(Banner::getSortOrder)
                .orderByDesc(Banner::getCreateTime);
        List<Banner> list = list(wrapper);
        return list.stream().map(this::toVO).collect(Collectors.toList());
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
}
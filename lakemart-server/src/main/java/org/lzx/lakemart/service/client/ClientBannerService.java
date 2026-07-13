package org.lzx.lakemart.service.client;

import org.lzx.lakemart.model.entity.Banner;
import org.lzx.lakemart.model.vo.BannerVO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ClientBannerService extends IService<Banner> {
    List<BannerVO> getEnabledBanners();
}
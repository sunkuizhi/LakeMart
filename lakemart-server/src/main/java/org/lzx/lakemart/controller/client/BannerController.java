package org.lzx.lakemart.controller.client;

import org.lzx.lakemart.model.vo.BannerVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.client.ClientBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Autowired
    private ClientBannerService clientBannerService;
    @GetMapping("/list")
    public Result<List<BannerVO>> getBanners() {
        List<BannerVO> list = clientBannerService.getEnabledBanners();
        return Result.success(list);
    }
}
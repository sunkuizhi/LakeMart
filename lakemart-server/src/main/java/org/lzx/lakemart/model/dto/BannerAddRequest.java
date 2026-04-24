package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class BannerAddRequest {
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sortOrder;
    private Integer status;
}
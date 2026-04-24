package org.lzx.lakemart.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressSetDefaultRequest {
    @NotNull(message = "地址ID不能为空")
    private Long id;
}
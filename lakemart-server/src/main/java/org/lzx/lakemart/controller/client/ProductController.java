package org.lzx.lakemart.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.lzx.lakemart.model.dto.ProductQueryDTO;
import org.lzx.lakemart.model.entity.Product;
import org.lzx.lakemart.model.vo.ProductVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 如果需要显示商品分类名称，可以注入 CategoryService（可选）
    // @Autowired
    // private CategoryService categoryService;

    /**
     * 分页查询商品列表（用户端）
     */
    @PostMapping("/list")
    public Result<Page<ProductVO>> listProducts(@RequestBody ProductQueryDTO query) {
        Page<ProductVO> page = productService.queryPage(query);
        return Result.success(page);
    }

    /**
     * 商品详情
     */
    @GetMapping("/detail/{id}")
    public Result<ProductVO> getProductDetail(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        if (product == null || product.getStatus() == 0) {
            return Result.error("商品不存在或已下架");
        }
        // 补充分类名（如果注入了 CategoryService，可以查询）
        String categoryName = null;
        // if (categoryService != null) {
        //     Category category = categoryService.getById(product.getCategoryId());
        //     if (category != null) categoryName = category.getName();
        // }

        ProductVO vo = ProductVO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .categoryId(product.getCategoryId())
                .categoryName(categoryName)
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .salesCount(product.getSalesCount())
                .createTime(product.getCreateTime())
                .build();
        return Result.success(vo);
    }
}
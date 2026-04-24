package org.lzx.lakemart.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.ProductAddRequest;
import org.lzx.lakemart.model.dto.ProductPageQueryDTO;
import org.lzx.lakemart.model.dto.ProductUpdateRequest;
import org.lzx.lakemart.model.entity.Product;
import org.lzx.lakemart.model.vo.ProductVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.ProductService;
import org.lzx.lakemart.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/product")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传商品图片到 MinIO
     * @param file 上传的图片文件
     * @return 图片访问 URL
     */
    @PostMapping("/uploadImage")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> uploadProductImage(@RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "type", defaultValue = "product") String type) {
        String imageUrl = minioUtil.uploadFile(file, type);
        // 使用 Result.success 重载，将 URL 放入 data
        // 直接构造，明确指定 code、message、data
        return Result.successData(imageUrl);
    }

    /**
     * 分页查询商品列表（管理端）
     */
    @PostMapping("/list")
    public Result<Page<ProductVO>> listProducts(@RequestBody ProductPageQueryDTO query) {
        Page<ProductVO> page = productService.adminQueryPage(query);
        return Result.success(page);
    }

    /**
     * 添加商品
     */
    @PostMapping("/add")
    public Result<String> addProduct(@Valid @RequestBody ProductAddRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategoryId(request.getCategoryId());
        product.setImageUrl(request.getImageUrl());
        productService.addProduct(product);
        return Result.success("添加成功");
    }

    /**
     * 更新商品
     */
    @PutMapping("/update")
    public Result<String> updateProduct(@Valid @RequestBody ProductUpdateRequest request) {
        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategoryId(request.getCategoryId());
        product.setImageUrl(request.getImageUrl());
        productService.updateProduct(product);
        return Result.success("更新成功");
    }

    /**
     * 上下架商品
     */
    @PutMapping("/status/{id}")
    public Result<String> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        productService.updateStatus(id, status);
        return Result.success("状态更新成功");
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return Result.success("删除成功");
    }
}
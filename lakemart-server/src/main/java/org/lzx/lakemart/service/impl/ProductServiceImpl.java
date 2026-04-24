package org.lzx.lakemart.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.lzx.lakemart.model.dto.ProductPageQueryDTO;
import org.lzx.lakemart.model.entity.Category;
import org.lzx.lakemart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.lzx.lakemart.model.dto.ProductQueryDTO;
import org.lzx.lakemart.model.entity.Product;
import org.lzx.lakemart.mapper.ProductMapper;
import org.lzx.lakemart.model.vo.ProductVO;
import org.lzx.lakemart.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private CategoryService categoryService;   // 新增注入
    @Override
    public Page<ProductVO> queryPage(ProductQueryDTO query) {
        // 构建分页对象
        Page<Product> page = new Page<>(query.getPageNum(), query.getPageSize());
        // 构建查询条件
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (query.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, query.getCategoryId());
        }
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.like(Product::getName, query.getKeyword());
        }
        // 状态为上架（用户端只显示上架商品）
        wrapper.eq(Product::getStatus, 1);
        // 排序
        if (query.getSortBy() != null) {
            boolean isAsc = "asc".equalsIgnoreCase(query.getSortOrder());
            switch (query.getSortBy()) {
                case "price":
                    wrapper.orderBy(true, isAsc, Product::getPrice);
                    break;
                case "salesCount":
                    wrapper.orderBy(true, isAsc, Product::getSalesCount);
                    break;
                case "createTime":
                    wrapper.orderBy(true, isAsc, Product::getCreateTime);
                    break;
                default:
                    wrapper.orderByDesc(Product::getCreateTime);
            }
        } else {
            wrapper.orderByDesc(Product::getCreateTime);
        }
        // 查询
        Page<Product> productPage = baseMapper.selectPage(page, wrapper);
        // 转换为 ProductVO
        Page<ProductVO> voPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        List<ProductVO> voList = productPage.getRecords().stream().map(product -> {
            // 查询分类名称（可以缓存，这里简单处理）
            Category category = categoryService.getById(product.getCategoryId());
            return ProductVO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .categoryId(product.getCategoryId())
                    .categoryName(category != null ? category.getName() : null)
                    .imageUrl(product.getImageUrl())
                    .status(product.getStatus())
                    .salesCount(product.getSalesCount())
                    .createTime(product.getCreateTime())
                    .build();
        }).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }
    @Override
    public Page<ProductVO> adminQueryPage(ProductPageQueryDTO query) {
        Page<Product> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(Product::getName, query.getName());
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, query.getCategoryId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(Product::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(Product::getCreateTime);
        Page<Product> productPage = baseMapper.selectPage(page, wrapper);
        // 转换为 VO
        Page<ProductVO> voPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        List<ProductVO> voList = productPage.getRecords().stream().map(product -> {
            // 补充分类名
            Category category = categoryService.getById(product.getCategoryId());
            return ProductVO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .categoryId(product.getCategoryId())
                    .categoryName(category != null ? category.getName() : null)
                    .imageUrl(product.getImageUrl())
                    .status(product.getStatus())
                    .salesCount(product.getSalesCount())
                    .createTime(product.getCreateTime())
                    .build();
        }).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public void addProduct(Product product) {
        product.setSalesCount(0);
        product.setStatus(1); // 默认上架
        product.setCreateTime(LocalDateTime.now());
        baseMapper.insert(product);
    }

    @Override
    public void updateProduct(Product product) {
        // 只更新允许修改的字段
        Product exist = baseMapper.selectById(product.getId());
        if (exist == null) {
            throw new RuntimeException("商品不存在");
        }
        if (product.getName() != null) exist.setName(product.getName());
        if (product.getDescription() != null) exist.setDescription(product.getDescription());
        if (product.getPrice() != null) exist.setPrice(product.getPrice());
        if (product.getStock() != null) exist.setStock(product.getStock());
        if (product.getCategoryId() != null) exist.setCategoryId(product.getCategoryId());
        if (product.getImageUrl() != null) exist.setImageUrl(product.getImageUrl());
        baseMapper.updateById(exist);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        baseMapper.updateById(product);
    }

    @Override
    public void deleteProduct(Long id) {
        // 检查是否有订单关联，如果有则不允许删除（或者逻辑删除）
        baseMapper.deleteById(id);
    }



}

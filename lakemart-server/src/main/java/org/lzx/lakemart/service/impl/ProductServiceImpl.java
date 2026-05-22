package org.lzx.lakemart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.ProductMapper;
import org.lzx.lakemart.model.dto.ProductPageQueryDTO;
import org.lzx.lakemart.model.dto.ProductQueryDTO;
import org.lzx.lakemart.model.entity.Category;
import org.lzx.lakemart.model.entity.Product;
import org.lzx.lakemart.model.vo.ProductVO;
import org.lzx.lakemart.service.CategoryService;
import org.lzx.lakemart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public Page<ProductVO> queryPage(ProductQueryDTO query) {
        // 用户端查询（保持不变）
        Page<Product> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        if (query.getCategoryId() != null) {
            if (Boolean.TRUE.equals(query.getIncludeChildren())) {
                List<Long> categoryIds = new ArrayList<>();
                categoryIds.add(query.getCategoryId());
                List<Long> subIds = categoryService.getAllSubCategoryIds(query.getCategoryId());
                if (subIds != null && !subIds.isEmpty()) {
                    categoryIds.addAll(subIds);
                }
                wrapper.in(Product::getCategoryId, categoryIds);
            } else {
                wrapper.eq(Product::getCategoryId, query.getCategoryId());
            }
        } else if (query.getParentCategoryId() != null) {
            List<Long> categoryIds = categoryService.getAllSubCategoryIds(query.getParentCategoryId());
            if (!categoryIds.isEmpty()) {
                wrapper.in(Product::getCategoryId, categoryIds);
            } else {
                wrapper.eq(Product::getCategoryId, -1L);
            }
        }

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.like(Product::getName, query.getKeyword());
        }
        wrapper.eq(Product::getStatus, 1);

        // 排序逻辑略（与原代码相同）
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

        Page<Product> productPage = baseMapper.selectPage(page, wrapper);
        Page<ProductVO> voPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        List<ProductVO> voList = productPage.getRecords().stream().map(product -> {
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

        // 管理端分类筛选：支持包含子分类
        if (query.getCategoryId() != null) {
            if (Boolean.TRUE.equals(query.getIncludeChildren())) {
                List<Long> categoryIds = new ArrayList<>();
                categoryIds.add(query.getCategoryId());
                List<Long> subIds = categoryService.getAllSubCategoryIds(query.getCategoryId());
                if (subIds != null && !subIds.isEmpty()) {
                    categoryIds.addAll(subIds);
                }
                wrapper.in(Product::getCategoryId, categoryIds);
            } else {
                wrapper.eq(Product::getCategoryId, query.getCategoryId());
            }
        }

        if (query.getStatus() != null) {
            wrapper.eq(Product::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(Product::getCreateTime);

        Page<Product> productPage = baseMapper.selectPage(page, wrapper);
        Page<ProductVO> voPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        List<ProductVO> voList = productPage.getRecords().stream().map(product -> {
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

    // 其他方法（addProduct, updateProduct, updateStatus, deleteProduct）保持不变，此处省略
    // 请确保你的原文件中有这些方法的完整实现
    @Override
    public void addProduct(Product product) {
        product.setSalesCount(0);
        product.setStatus(1);
        product.setCreateTime(LocalDateTime.now());
        baseMapper.insert(product);
    }

    @Override
    public void updateProduct(Product product) {
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
        baseMapper.deleteById(id);
    }
}
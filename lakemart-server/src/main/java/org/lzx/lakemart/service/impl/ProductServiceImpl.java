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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public Page<ProductVO> queryPage(ProductQueryDTO query) {
        Page<Product> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        // 分类筛选逻辑：优先精确匹配 categoryId，否则使用 parentCategoryId（包含子分类）
        if (query.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, query.getCategoryId());
        } else if (query.getParentCategoryId() != null) {
            List<Long> categoryIds = categoryService.getAllSubCategoryIds(query.getParentCategoryId());
            if (!categoryIds.isEmpty()) {
                wrapper.in(Product::getCategoryId, categoryIds);
            } else {
                // 如果传入的父分类不存在或无子分类（实际不可能无自身），让结果为空
                wrapper.eq(Product::getCategoryId, -1L);
            }
        }

        // 模糊搜索商品名称
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.like(Product::getName, query.getKeyword());
        }

        // 用户端只显示上架商品
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

        Page<Product> productPage = baseMapper.selectPage(page, wrapper);

        // 转换为 ProductVO，并补充分类名称
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
        if (query.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, query.getCategoryId());
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
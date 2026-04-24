package org.lzx.lakemart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.CategoryMapper;
import org.lzx.lakemart.mapper.ProductMapper;
import org.lzx.lakemart.model.entity.Category;
import org.lzx.lakemart.model.entity.Product;
import org.lzx.lakemart.model.vo.CategoryVO;
import org.lzx.lakemart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<CategoryVO> getTree() {
        // 查询所有启用状态的分类
        List<Category> all = list(new LambdaQueryWrapper<Category>().eq(Category::getStatus, 1));
        // 找出所有顶级分类（parentId = 0）
        return all.stream()
                .filter(c -> c.getParentId() == 0L)
                .map(c -> buildTree(c, all))
                .collect(Collectors.toList());
    }


    private CategoryVO buildTree(Category category, List<Category> all) {
        CategoryVO vo = CategoryVO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParentId())
                .sortOrder(category.getSortOrder())
                .status(category.getStatus())
                .createTime(category.getCreateTime())
                .build();
        // 递归填充子分类
        List<CategoryVO> children = all.stream()
                .filter(c -> c.getParentId().equals(category.getId()))
                .map(c -> buildTree(c, all))
                .sorted(Comparator.comparing(CategoryVO::getSortOrder))
                .collect(Collectors.toList());
        vo.setChildren(children);
        return vo;
    }
    @Override
    public List<CategoryVO> getAdminTree() {
        // 查询所有分类（不限制状态）
        List<Category> all = list();
        return buildTree(all, 0L);
    }

    @Override
    @Transactional
    public void addCategory(Category category) {
        category.setCreateTime(LocalDateTime.now());
        baseMapper.insert(category);
    }

    @Override
    @Transactional
    public void updateCategory(Category category) {
        Category exist = baseMapper.selectById(category.getId());
        if (exist == null) {
            throw new RuntimeException("分类不存在");
        }
        if (category.getName() != null) exist.setName(category.getName());
        if (category.getParentId() != null) exist.setParentId(category.getParentId());
        if (category.getSortOrder() != null) exist.setSortOrder(category.getSortOrder());
        if (category.getStatus() != null) exist.setStatus(category.getStatus());
        baseMapper.updateById(exist);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // 检查是否有子分类
        long childCount = count(new LambdaQueryWrapper<Category>().eq(Category::getParentId, id));
        if (childCount > 0) {
            throw new RuntimeException("请先删除子分类");
        }
        // 检查是否有商品关联
        long productCount = productMapper.selectCount(new LambdaQueryWrapper<Product>().eq(Product::getCategoryId, id));
        if (productCount > 0) {
            throw new RuntimeException("该分类下存在商品，无法删除");
        }
        baseMapper.deleteById(id);
    }

    // 辅助方法：构建树
    private List<CategoryVO> buildTree(List<Category> all, Long parentId) {
        return all.stream()
                .filter(c -> c.getParentId().equals(parentId))
                .map(c -> {
                    CategoryVO vo = CategoryVO.builder()
                            .id(c.getId())
                            .name(c.getName())
                            .parentId(c.getParentId())
                            .sortOrder(c.getSortOrder())
                            .status(c.getStatus())
                            .build();
                    vo.setChildren(buildTree(all, c.getId()));
                    return vo;
                })
                .sorted(Comparator.comparing(CategoryVO::getSortOrder))
                .collect(Collectors.toList());
    }

}
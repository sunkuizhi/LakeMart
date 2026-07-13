package org.lzx.lakemart.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.exception.BusinessException;
import org.lzx.lakemart.mapper.CategoryMapper;
import org.lzx.lakemart.mapper.ProductMapper;
import org.lzx.lakemart.model.entity.Category;
import org.lzx.lakemart.model.entity.Product;
import org.lzx.lakemart.model.vo.CategoryVO;
import org.lzx.lakemart.service.admin.AdminCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements AdminCategoryService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<CategoryVO> getAdminTree() {
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
        deleteChildren(id);
        long productCount = productMapper.selectCount(new LambdaQueryWrapper<Product>().eq(Product::getCategoryId, id));
        if (productCount > 0) {
            throw new BusinessException("该分类下存在商品，无法删除");
        }
        baseMapper.deleteById(id);
    }

    private void deleteChildren(Long parentId) {
        List<Category> children = this.lambdaQuery().eq(Category::getParentId, parentId).list();
        for (Category child : children) {
            deleteChildren(child.getId());
            baseMapper.deleteById(child.getId());
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Category category = this.getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        category.setStatus(status);
        this.updateById(category);
        updateChildrenStatus(id, status);
    }

    private void updateChildrenStatus(Long parentId, Integer status) {
        List<Category> children = this.lambdaQuery().eq(Category::getParentId, parentId).list();
        for (Category child : children) {
            child.setStatus(status);
            this.updateById(child);
            updateChildrenStatus(child.getId(), status);
        }
    }

    @Override
    public List<Long> getAllSubCategoryIds(Long parentId) {
        List<Long> ids = new ArrayList<>();
        ids.add(parentId);
        collectChildrenIds(parentId, ids);
        return ids;
    }

    private void collectChildrenIds(Long parentId, List<Long> ids) {
        List<Category> children = this.lambdaQuery().eq(Category::getParentId, parentId).list();
        for (Category child : children) {
            ids.add(child.getId());
            collectChildrenIds(child.getId(), ids);
        }
    }

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
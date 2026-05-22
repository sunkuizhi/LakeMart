// package org.lzx.lakemart.service.impl;
//
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
// import org.lzx.lakemart.exception.BusinessException;
// import org.lzx.lakemart.mapper.CategoryMapper;
// import org.lzx.lakemart.mapper.ProductMapper;
// import org.lzx.lakemart.model.entity.Category;
// import org.lzx.lakemart.model.entity.Product;
// import org.lzx.lakemart.model.vo.CategoryVO;
// import org.lzx.lakemart.service.CategoryService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.List;
// import java.util.stream.Collectors;
//
// @Service
// public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
//     @Autowired
//     private ProductMapper productMapper;
//     @Override
//     public List<CategoryVO> getTree() {
//         // 查询所有启用状态的分类
//         List<Category> all = list(new LambdaQueryWrapper<Category>().eq(Category::getStatus, 1));
//         // 找出所有顶级分类（parentId = 0）
//         return all.stream()
//                 .filter(c -> c.getParentId() == 0L)
//                 .map(c -> buildTree(c, all))
//                 .collect(Collectors.toList());
//     }
//
//
//     private CategoryVO buildTree(Category category, List<Category> all) {
//         CategoryVO vo = CategoryVO.builder()
//                 .id(category.getId())
//                 .name(category.getName())
//                 .parentId(category.getParentId())
//                 .sortOrder(category.getSortOrder())
//                 .status(category.getStatus())
//                 .createTime(category.getCreateTime())
//                 .build();
//         // 递归填充子分类
//         List<CategoryVO> children = all.stream()
//                 .filter(c -> c.getParentId().equals(category.getId()))
//                 .map(c -> buildTree(c, all))
//                 .sorted(Comparator.comparing(CategoryVO::getSortOrder))
//                 .collect(Collectors.toList());
//         vo.setChildren(children);
//         return vo;
//     }
//     @Override
//     public List<CategoryVO> getAdminTree() {
//         // 查询所有分类（不限制状态）
//         List<Category> all = list();
//         return buildTree(all, 0L);
//     }
//
//     @Override
//     @Transactional
//     public void addCategory(Category category) {
//         category.setCreateTime(LocalDateTime.now());
//         baseMapper.insert(category);
//     }
//
//     @Override
//     @Transactional
//     public void updateCategory(Category category) {
//         Category exist = baseMapper.selectById(category.getId());
//         if (exist == null) {
//             throw new RuntimeException("分类不存在");
//         }
//         if (category.getName() != null) exist.setName(category.getName());
//         if (category.getParentId() != null) exist.setParentId(category.getParentId());
//         if (category.getSortOrder() != null) exist.setSortOrder(category.getSortOrder());
//         if (category.getStatus() != null) exist.setStatus(category.getStatus());
//         baseMapper.updateById(exist);
//     }
//
//     @Override
//     @Transactional
//     public void deleteCategory(Long id) {
//         // 递归删除所有子分类
//         deleteChildren(id);
//         // 删除当前分类（如果有商品关联，仍需检查）
//         long productCount = productMapper.selectCount(new LambdaQueryWrapper<Product>().eq(Product::getCategoryId, id));
//         if (productCount > 0) {
//             throw new BusinessException("该分类下存在商品，无法删除");
//         }
//         baseMapper.deleteById(id);
//     }
//
//     private void deleteChildren(Long parentId) {
//         List<Category> children = this.lambdaQuery().eq(Category::getParentId, parentId).list();
//         for (Category child : children) {
//             deleteChildren(child.getId()); // 递归删除子节点
//             baseMapper.deleteById(child.getId());
//         }
//     }
//
//     // 辅助方法：构建树
//     private List<CategoryVO> buildTree(List<Category> all, Long parentId) {
//         return all.stream()
//                 .filter(c -> c.getParentId().equals(parentId))
//                 .map(c -> {
//                     CategoryVO vo = CategoryVO.builder()
//                             .id(c.getId())
//                             .name(c.getName())
//                             .parentId(c.getParentId())
//                             .sortOrder(c.getSortOrder())
//                             .status(c.getStatus())
//                             .build();
//                     vo.setChildren(buildTree(all, c.getId()));
//                     return vo;
//                 })
//                 .sorted(Comparator.comparing(CategoryVO::getSortOrder))
//                 .collect(Collectors.toList());
//     }
//     @Override
//     @Transactional
//     public void updateStatus(Long id, Integer status) {
//         // 1. 更新当前分类状态
//         Category category = this.getById(id);
//         if (category == null) {
//             throw new BusinessException("分类不存在");
//         }
//         category.setStatus(status);
//         this.updateById(category);
//
//         // 2. 递归更新所有子分类状态
//         updateChildrenStatus(id, status);
//     }
//
//     /**
//      * 递归更新子分类状态
//      * @param parentId 父分类ID
//      * @param status   目标状态（1启用 0禁用）
//      */
//     private void updateChildrenStatus(Long parentId, Integer status) {
//         // 查询所有直接子分类
//         List<Category> children = this.lambdaQuery()
//                 .eq(Category::getParentId, parentId)
//                 .list();
//         if (children.isEmpty()) {
//             return;
//         }
//         // 批量更新子分类状态
//         for (Category child : children) {
//             child.setStatus(status);
//             this.updateById(child);
//             // 递归处理孙子分类
//             updateChildrenStatus(child.getId(), status);
//         }
//     }
//     private void updateChildrenStatusBatch(Long parentId, Integer status) {
//         // 收集所有子孙分类ID
//         List<Long> ids = new ArrayList<>();
//         collectChildrenIds(parentId, ids);
//         if (!ids.isEmpty()) {
//             this.lambdaUpdate()
//                     .in(Category::getId, ids)
//                     .set(Category::getStatus, status)
//                     .update();
//         }
//     }
//
//     private void collectChildrenIds(Long parentId, List<Long> ids) {
//         List<Category> children = this.lambdaQuery()
//                 .eq(Category::getParentId, parentId)
//                 .list();
//         for (Category child : children) {
//             ids.add(child.getId());
//             collectChildrenIds(child.getId(), ids);
//         }
//     }
//
// }


package org.lzx.lakemart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.exception.BusinessException;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<CategoryVO> getTree() {
        List<Category> all = list(new LambdaQueryWrapper<Category>().eq(Category::getStatus, 1));
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
        List<Category> all = list();
        return buildTree(all, 0L);
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

    /**
     * 获取指定分类及其所有子分类的ID列表（包含自身）
     */
    @Override
    public List<Long> getAllSubCategoryIds(Long parentId) {
        List<Long> ids = new ArrayList<>();
        ids.add(parentId);
        collectChildrenIds(parentId, ids);
        return ids;
    }

    /**
     * 递归收集所有子分类ID
     */
    private void collectChildrenIds(Long parentId, List<Long> ids) {
        List<Category> children = this.lambdaQuery()
                .eq(Category::getParentId, parentId)
                .list();
        for (Category child : children) {
            ids.add(child.getId());
            collectChildrenIds(child.getId(), ids);
        }
    }
}
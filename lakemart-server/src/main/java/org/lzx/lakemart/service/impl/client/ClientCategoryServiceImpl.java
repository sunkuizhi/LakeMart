package org.lzx.lakemart.service.impl.client;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.CategoryMapper;
import org.lzx.lakemart.model.entity.Category;
import org.lzx.lakemart.model.vo.CategoryVO;
import org.lzx.lakemart.service.client.ClientCategoryService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientCategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ClientCategoryService {

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
}
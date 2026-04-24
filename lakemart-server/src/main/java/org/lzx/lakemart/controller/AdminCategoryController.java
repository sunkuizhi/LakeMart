package org.lzx.lakemart.controller;

import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.CategoryAddRequest;
import org.lzx.lakemart.model.dto.CategoryUpdateRequest;
import org.lzx.lakemart.model.entity.Category;
import org.lzx.lakemart.model.vo.CategoryVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/category")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/tree")
    public Result<List<CategoryVO>> getTree() {
        List<CategoryVO> tree = categoryService.getAdminTree();
        return Result.success(tree);
    }

    @PostMapping("/add")
    public Result<String> addCategory(@Valid @RequestBody CategoryAddRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setParentId(request.getParentId() != null ? request.getParentId() : 0L);
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        category.setStatus(1); // 默认启用
        categoryService.addCategory(category);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    public Result<String> updateCategory(@Valid @RequestBody CategoryUpdateRequest request) {
        Category category = new Category();
        category.setId(request.getId());
        category.setName(request.getName());
        category.setParentId(request.getParentId());
        category.setSortOrder(request.getSortOrder());
        category.setStatus(request.getStatus());
        categoryService.updateCategory(category);
        return Result.success("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return Result.success("删除成功");
    }
    @PutMapping("/status/{id}")
    public Result<String> updateStatus(@PathVariable("id") Long id, @RequestParam Integer status) {
        categoryService.updateStatus(id, status);
        return Result.success("操作成功");
    }
}
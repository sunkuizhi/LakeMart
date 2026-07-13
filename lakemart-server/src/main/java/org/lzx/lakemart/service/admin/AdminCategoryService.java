package org.lzx.lakemart.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.entity.Category;
import org.lzx.lakemart.model.vo.CategoryVO;
import java.util.List;

public interface AdminCategoryService extends IService<Category> {
    List<CategoryVO> getAdminTree();
    void addCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Long id);
    void updateStatus(Long id, Integer status);
    List<Long> getAllSubCategoryIds(Long parentId);
}
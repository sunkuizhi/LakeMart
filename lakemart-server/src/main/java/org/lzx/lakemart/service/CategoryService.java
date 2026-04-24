package org.lzx.lakemart.service;

import org.lzx.lakemart.model.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.vo.CategoryVO;

import java.util.List;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
public interface CategoryService extends IService<Category> {
    // CategoryService 接口
    List<CategoryVO> getTree();
    // 获取树形分类（管理端，包含禁用的分类）
    List<CategoryVO> getAdminTree();
    // 添加分类
    void addCategory(Category category);
    // 更新分类
    void updateCategory(Category category);
    // 删除分类（检查是否有子分类或关联商品）
    void deleteCategory(Long id);
    //禁用分类
    void updateStatus(Long id, Integer status);
}

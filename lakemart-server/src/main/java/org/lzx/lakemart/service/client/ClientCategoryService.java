package org.lzx.lakemart.service.client;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.entity.Category;
import org.lzx.lakemart.model.vo.CategoryVO;
import java.util.List;

public interface ClientCategoryService extends IService<Category> {
    List<CategoryVO> getTree();
}
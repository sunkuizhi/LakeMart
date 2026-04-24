package org.lzx.lakemart.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.lzx.lakemart.model.dto.ProductPageQueryDTO;
import org.lzx.lakemart.model.dto.ProductQueryDTO;
import org.lzx.lakemart.model.vo.ProductVO;
import org.lzx.lakemart.model.dto.ProductQueryDTO;
import org.lzx.lakemart.model.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.vo.ProductVO;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
public interface ProductService extends IService<Product> {
    Page<ProductVO> queryPage(ProductQueryDTO query);
    // 分页查询（管理端，可查所有状态）
    // 管理端
    Page<ProductVO> adminQueryPage(ProductPageQueryDTO query);
    // 添加商品
    void addProduct(Product product);
    // 更新商品
    void updateProduct(Product product);
    // 上下架
    void updateStatus(Long id, Integer status);
    // 删除商品（物理删除）
    void deleteProduct(Long id);


}

package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 产品数据访问接口
 * 继承 MyBatis Plus 的 BaseMapper，自动获得基础 CRUD 方法
 */
@Mapper
public interface ProductDao extends BaseMapper<Product> {

    /**
     * 根据分类ID获取产品列表
     */
    List<Product> getProductListByCategory(String categoryId);

    /**
     * 根据关键字搜索产品
     */
    List<Product> searchProductList(String keyword);

    /**
     * 根据产品ID查询产品详情
     */
    Product getProduct(String productId);
}
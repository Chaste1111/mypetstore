package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品分类数据访问接口
 * 继承 MyBatis Plus 的 BaseMapper，自动获得基础 CRUD 方法
 */
@Mapper
public interface CategoryDao extends BaseMapper<Category> {

    /**
     * 获取所有分类列表
     */
    List<Category> getCategoryList();

    /**
     * 根据分类ID查询分类详情
     */
    Category getCategory(String categoryId);
}
package service;

import com.jiao.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 18067
 * @Date 2021/9/13 20:52
 */
public interface ProductService {
    /**
     * @return 收益率平均值
     */
    BigDecimal queryAvgRate();

    /**
     * @param productType 产品类型
     * @param pageNo 页号
     * @param pageSize 每页数据大小
     * @return 产品的list(size是0或多条)
     */
   List<Product> queryProductyPage(Integer productType, Integer pageNo, Integer pageSize);

    /**
     * @param productType 类型
     * @return 获取该类型数目
     */
   Integer queryTotalRecordProductType(Integer productType);

   Product queryProductById(Integer id);

}

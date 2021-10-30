package com.jiao.mapper;


import com.jiao.model.Product;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductMapper {
    //收益率平均律
    BigDecimal selectAvgRate();
    //分页查询产品
    List<Product> selectPage(@Param("productType") Integer productType, @Param("offSet") Integer offSet, @Param("rows") Integer rows);
    //某类型产品总记录数量
    int selectRecordsProductType(Integer ptype);
    //查询详情
    Product selectProductById(Integer id);
    //减去投资金额
    Integer updateLeftMoney(@Param("productId") Integer productId, @Param("bidMoney") BigDecimal bidMoney);

    /**
     * 更新产品的状态和满标时间
     * @param productId 产品id
     * @return
     */
    Integer updateStatusAndFullTime(@Param("productId") Integer productId);

    List<Product> selectBeforeManBiaoProduct(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int updateStatus(@Param("id") Integer id, @Param("statusIncome") int statusIncome);
}

package com.jiao.service.impl;

import com.jiao.contants.YLBkeys;
import com.jiao.mapper.ProductMapper;
import com.jiao.util.YLBUtils;
import com.jiao.model.Product;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import service.ProductService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 18067
 * @Date 2021/9/13 20:53
 */
@DubboService(interfaceClass = ProductService.class, version = "1.0")
public class ProductServiceImpl implements ProductService {
    @Resource
    ProductMapper productMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public BigDecimal queryAvgRate() {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        BigDecimal avgRate = (BigDecimal) opsForValue.get(YLBkeys.PRODUCT_RATE_AVG);
        if (avgRate == null) {
            synchronized (this) {
                if (opsForValue.get(YLBkeys.PRODUCT_RATE_AVG) == null) {
                    avgRate = productMapper.selectAvgRate();
                    opsForValue.set(YLBkeys.PRODUCT_RATE_AVG, avgRate, 30, TimeUnit.MINUTES);
                }
            }
        }
        return avgRate;
    }

    /**
     * @param productType 产品类型
     * @param pageNo      页号
     * @param pageSize    每页数据大小
     * @return 产品的list(Size是0或者多条数据)
     */
    @Override
    public List<Product> queryProductyPage(Integer productType, Integer pageNo, Integer pageSize) {
        List<Product> productList = new ArrayList<>();
        //参数检查
        if (YLBUtils.checkProductType(productType)) {
            pageNo = YLBUtils.defaultPageNo(pageNo);
            pageSize = YLBUtils.defaultPageSize(pageSize);
            int offSet = YLBUtils.offSet(pageNo, pageSize);
            productList = productMapper.selectPage(productType, offSet, pageSize);
        }
        return productList;
    }

    @Override
    public Integer queryTotalRecordProductType(Integer productType) {
        int records = 0;
        if (YLBUtils.checkProductType(productType)) {
            records = productMapper.selectRecordsProductType(productType);
        }
        return records;
    }

    /**
     * @param id id
     * @return product对象
     */
    @Override
    public Product queryProductById(Integer id) {
        Product product = null;
        if (id != null && id > 0) {
            product = productMapper.selectProductById(id);
        }
        return product;
    }

}

package com.jiao.service.impl;

import com.jiao.mapper.BidMapper;

import com.jiao.model.exit.PhoneBid;
import org.apache.dubbo.config.annotation.DubboService;
import service.BidService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 18067
 * @Date 2021/9/15 8:18
 */
@DubboService(interfaceClass = BidService.class, version = "1.0")
public class BidServiceImpl implements BidService {
    @Resource
    BidMapper bidMapper;

    @Override
    public List<PhoneBid> queryBidByLoanId(Integer loanId) {
        List<PhoneBid> bidList = new ArrayList<>();
        if (loanId != null && loanId > 0) {
            Integer pageSize=3;
            bidList = bidMapper.selectBidByLoanId(loanId,3);
        }
        return bidList;
    }


}

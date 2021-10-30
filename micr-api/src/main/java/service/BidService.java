package service;

import com.jiao.model.Bid;
import com.jiao.model.exit.PhoneBid;

import java.util.List;

/**
 * @author 18067
 * @Date 2021/9/15 8:16
 */
public interface BidService {
    List<PhoneBid> queryBidByLoanId(Integer loanId);


}

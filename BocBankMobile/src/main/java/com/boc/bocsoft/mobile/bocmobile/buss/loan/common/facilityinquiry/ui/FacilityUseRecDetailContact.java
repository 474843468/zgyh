package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;

/**
 * Created by liuzc on 2016/10/18.
 */
public class FacilityUseRecDetailContact {
    public interface View{
        void queryCardNumSuccess(String cardNum);
        void queryCardNumFailed();

        /**007用款详情查询失败*/
        void eDrawingDetailFail(BiiResultErrorException biiResultErrorException);
        /**007用款详情查询成功*/
        void eDrawingDetailSuccess(PsnDrawingDetailResult eloanDrawDetailResult);
    }
    public interface  Presenter{
        //通过账户查询卡号
        void queryCardNum(String accountNum);
        /**用款详情查询*/
        void queryDrawingDetail(String loanActNum);
    }
}

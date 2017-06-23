package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.buyprocedure.BuyProcedureWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter.LikeAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.OnItemClickListener;
import com.boc.bocsoft.mobile.framework.zxing.encode.BarCodeEncoder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author wangyang
 *         2016/11/25 16:23
 */
public class TestResultFragment extends BaseAccountFragment implements OnItemClickListener {

    private BaseResultView resultView;

    private LikeAdapter likeAdapter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        resultView = new com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.widget.BaseResultView(getContext());
        return resultView;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_loss_success_title);
    }

    @Override
    public void setListener() {
        resultView.setNeedListener(new ResultBottom.OnClickListener() {
            @Override
            public void onClick(int id) {
                ToastUtils.show("您可能需要==" + id);
            }
        });
    }

    @Override
    public void initData() {
        resultView.addStatus(ResultHead.Status.SUCCESS, "ASDJLAJSD大时代");
        resultView.addTitle("加上了的空间阿里山看得见拉开");
        ((com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.widget.BaseResultView)resultView).addBuyProcedure(BuyProcedureWidget.CompleteStatus.PAY, new String[]{"asdasd", "asdasd", "qwwwwww"}, new String[]{"2012", "2013", "2015"});

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("test1", "123123123");
        map.put("test2", "82734987234");
        resultView.addTopDetail(map);
        resultView.addTopDetail(generateView());
        resultView.addTopDetail(map);

        LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
        map1.put("qwe", "sdfsdf");
        map1.put("超长标题超长标题超长标题", "xcvxcv");
        map1.put("超长文字", "dfgdfgdfgakjsdlkajsldkjalskjdlaksjdlkajsdlkjaslkdjalksdjlakjsdlkajsdlakjsdlkajsdlkjasdlkjalskdjlaksjdlkajsdlkajsdlkajsdlkjalsdkjalksdjlaksjdlakjsdlaksdjlj");
        resultView.addDetail(map1);

        resultView.setHint("aslkdjalksdjlkajsdlkajsdlkj");

        resultView.addBottomView(generateView());
        resultView.addNeedItem("TEST1", 1);
        resultView.addNeedItem("TEST2", 2);
        resultView.addNeedItem("TEST3", 3);


        List<WealthListBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            WealthListBean bean = new WealthListBean();
            bean.setProdName("产品" + i);
            bean.setYearlyRR((25 + i) + "%");
            bean.setPeriedTime((25 + i) + "25+i");
            list.add(bean);
        }

        likeAdapter = new LikeAdapter(getContext(), list);
        likeAdapter.setOnItemClickListener(this);
        resultView.addLikeView(likeAdapter);
    }

    private View generateView() {
        LinearLayout detailView = (LinearLayout) View.inflate(mContext, R.layout.boc_view_qrpay_result_detail_info, null);
        ImageView imageView = (ImageView) detailView.findViewById(R.id.iv_qrpay_code_bar);
        setBarCodeToView(imageView, "12312312324234");
        View.inflate(getContext(), R.layout.boc_divide_line, detailView);
        return detailView;
    }

    /**
     * 设置条形码
     */
    private void setBarCodeToView(ImageView imageView, String code) {
        Bitmap barCodeBitmap = initCodeBuilder().creatBarcode(mContext, code);
        if (barCodeBitmap == null) {
            imageView.setImageBitmap(null);
            Toast.makeText(mContext, "条形码生成错误，请重试", Toast.LENGTH_SHORT).show();
        } else {
            imageView.setImageBitmap(barCodeBitmap);
        }
    }

    /**
     * 初始化条形码的参数
     */
    private BarCodeEncoder initCodeBuilder() {
        return new BarCodeEncoder.Builder()
                .setOutputBitmapWidth(ResUtils.dip2px(mContext, 300))
                .setOutputBitmapHeight(ResUtils.dip2px(mContext, 60))
                .setIsDisplayCodeText(true)
                .setDisplayCodeTextSize(14)
                .setIsDisplayCodeTextBold(true)
                .setIsFormatCodeNumber(true)
                .build();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        ToastUtils.show("猜你喜欢产品===="+likeAdapter.getItem(position).getProdName());
    }
}

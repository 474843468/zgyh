package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model.QRCodeContentBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * 解析二维码
 * Created by wangf on 2016/11/9.
 */
public class QRCodeDataDecode {

    /***
     * 二维码的类型
     */
    public final static int QRCODE_ID_OTHER = -1;//其他类型二维码
    public final static int QRCODE_ID_C2B = 0;//C2B类型二维码
    public final static int QRCODE_ID_C2C = 1;//C2C类型二维码
    public final static int QRCODE_ID_WEALTH = 2;//理财类型二维码
    public final static int QRCODE_ID_LL = 101;//联龙二维码模块 - 基金type为2，外汇type为3，双向宝为4，账户贵金属为5


    /**
     * 解析中行二维码数据
     * @param rawResult
     * @return
     */
    public static QRCodeModel getQrContent(String rawResult) {
        return getQRCodeC2B(rawResult);
    }


    /**
     * 解析C2B类型二维码
     *
     * @param rawResult {"v":"BOC1.00","m":"111111111111111","t":"22222222","n":"测试商户"}
     * @return
     */
    private static QRCodeModel getQRCodeC2B(String rawResult) {
        try {
            QRCodeContentBean qrContentC2B = new QRCodeContentBean();
            JSONObject jsonObject = new JSONObject(rawResult);
            qrContentC2B.setV(jsonObject.getString("v"));
            qrContentC2B.setM(jsonObject.getString("m"));
            qrContentC2B.setT(jsonObject.getString("t"));
            qrContentC2B.setN(jsonObject.getString("n"));

            QRCodeModel qrCodeModel = new QRCodeModel();
            qrCodeModel.setQrCodeId(QRCODE_ID_C2B);
            qrCodeModel.setQrContentC2B(qrContentC2B);
            return qrCodeModel;
        } catch (JSONException e) {
            return getQRCodeC2C(rawResult);
        }
    }

    /**
     * 解析C2C类型二维码
     *
     * @param rawResult 6213151215121121
     * @return
     */
    private static QRCodeModel getQRCodeC2C(String rawResult) {
        try {
//            if (!StringUtils.isEmpty(rawResult)) {
//                if (rawResult.length() == 19) {
//                    if ("62".equals(rawResult.substring(0, 2))) {
//                        QRCodeModel qrCodeModel = new QRCodeModel();
//                        qrCodeModel.setQrCodeId(QRCODE_ID_C2C);
//                        qrCodeModel.setQrContentC2C(rawResult);
//                        return qrCodeModel;
//                    }
//                }
//            }
            return getQRCodeWealth(rawResult);
        } catch (Exception e) {
            return getQRCodeWealth(rawResult);
        }
    }


    /**
     * 解析 理财 基金 类型二维码
     *
     * @param rawResult
     * boc://bocphone?type=1&prodCode=1234&productKind=111
     * @return
     */
    private static QRCodeModel getQRCodeWealth(String rawResult) {
        QRCodeModel qrCodeModel = new QRCodeModel();
        try {
            if (!StringUtils.isEmpty(rawResult)) {
                String[] allArray = rawResult.split("\\?");
                if ("boc://bocphone".equals(allArray[0])) {
                    String[] paramsArray = allArray[1].split("&");
                    if ("type".equals(paramsArray[0].split("=")[0])) {
                        String type = paramsArray[0].split("=")[1];
                        if ("1".equals(type)) {//理财
                            if ("prodCode".equals(paramsArray[1].split("=")[0]) && "productKind".equals(paramsArray[2].split("=")[0])) {
                                String prodCode = paramsArray[1].split("=")[1];
                                String productKind = paramsArray[2].split("=")[1];
                                DetailsRequestBean requestBean = new DetailsRequestBean();
                                requestBean.setProdCode(prodCode);
                                requestBean.setProdKind(productKind);

                                qrCodeModel.setQrCodeId(QRCODE_ID_WEALTH);
                                qrCodeModel.setQrContentWealth(requestBean);
                                return qrCodeModel;
                            }
                        } else if ("2".equals(type) || "3".equals(type) || "4".equals(type) || "5".equals(type)) {
                            qrCodeModel.setQrCodeId(QRCODE_ID_LL);
                            qrCodeModel.setQrContentLL(rawResult);//联龙模块二维码需要将二维码所有信息传出
                            return qrCodeModel;
                        } else {
                            qrCodeModel.setQrCodeId(QRCODE_ID_OTHER);
                            return qrCodeModel;
                        }
                    }
                }
            }
            // TODO: 2016/10/19 其他类型二维码数据
            qrCodeModel.setQrCodeId(QRCODE_ID_OTHER);
            return qrCodeModel;
        } catch (Exception e) {
            // TODO: 2016/10/19 其他类型二维码数据
            qrCodeModel.setQrCodeId(QRCODE_ID_OTHER);
            return qrCodeModel;
        }
    }


    /**
     * 二维码数据模型
     */
    public static class QRCodeModel {
        private int qrCodeId;
        private QRCodeContentBean qrContentC2B;//C2B
        private String qrContentC2C;//C2C
        private DetailsRequestBean qrContentWealth;//理财
        private String qrContentLL;////联龙二维码模块 - 基金type为2，外汇type为3，双向宝为4，账户贵金属为5


        public int getQrCodeId() {
            return qrCodeId;
        }

        public void setQrCodeId(int qrCodeId) {
            this.qrCodeId = qrCodeId;
        }

        public QRCodeContentBean getQrContentC2B() {
            return qrContentC2B;
        }

        public void setQrContentC2B(QRCodeContentBean qrContentC2B) {
            this.qrContentC2B = qrContentC2B;
        }

        public String getQrContentC2C() {
            return qrContentC2C;
        }

        public void setQrContentC2C(String qrContentC2C) {
            this.qrContentC2C = qrContentC2C;
        }

        public DetailsRequestBean getQrContentWealth() {
            return qrContentWealth;
        }

        public void setQrContentWealth(DetailsRequestBean qrContentWealth) {
            this.qrContentWealth = qrContentWealth;
        }

        public String getQrContentLL() {
            return qrContentLL;
        }

        public void setQrContentLL(String qrContentLL) {
            this.qrContentLL = qrContentLL;
        }
    }

}

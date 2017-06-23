package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.bean;

import java.util.List;

/**
 * Created by guokai on 2016/9/24.
 */
public class JsonBean {


    /**
     * A : 18-30
     * B : 31-50
     * C : 51-64
     * D : 65岁及以上
     * title : 1. 您的年龄是？
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String A;
        private String B;
        private String C;
        private String D;
        private String E;
        private String title;

        public String getE() {
            return E;
        }

        public void setE(String e) {
            E = e;
        }



        public String getA() {
            return A;
        }

        public void setA(String A) {
            this.A = A;
        }

        public String getB() {
            return B;
        }

        public void setB(String B) {
            this.B = B;
        }

        public String getC() {
            return C;
        }

        public void setC(String C) {
            this.C = C;
        }

        public String getD() {
            return D;
        }

        public void setD(String D) {
            this.D = D;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

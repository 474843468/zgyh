package com.boc.bocsoft.mobile.bocmobile.base.widget.riskassessment.bean;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocop.sdk.util.StringUtil;

import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

public class FundQuestions {




    /**
     * optionhead : ["A","B","C","D"]
     * optioncontent : ["18-30","31-50","51-64","65岁及以上"]
     * score : [1,2,3,4]
     * title : 1. 您的年龄是？
     */

    private List<Question> list;
    public List<Question> getList() {
        return list;
    }

    public void setList(List<Question> list) {
        this.list = list;
    }

    public static class Question {

        private String title;
        private List<String> optionhead;
        private List<String> optioncontent;
        private List<Integer> score;
        //记录选中的题支索引
        private int selectedIndex = -1;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getOptionhead() {
            return optionhead;
        }

        public void setOptionhead(List<String> optionhead) {
            this.optionhead = optionhead;
        }

        public List<String> getOptioncontent() {
            return optioncontent;
        }

        public void setOptioncontent(List<String> optioncontent) {
            this.optioncontent = optioncontent;
        }


        public void setScore(List<Integer> score) {
            this.score = score;
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        public int getScore(){
            LogUtil.e("selectedIndex:"+selectedIndex+ " score:"+score.size());
            if (selectedIndex <= -1 && selectedIndex >= score.size()){
                return 0;
            }
            return score.get(selectedIndex);

        }

        public boolean isQuestionVaild() {
            if (StringUtil.isNullOrEmpty(title)) {
                return false;
            }
            if (score == null || optionhead == null || optioncontent == null){
                return false;
            }
            if ((score.size() != optioncontent.size()) || (optioncontent.size()!= optionhead.size())
                    || (score.size() != optionhead.size())){
                return false;
            }
            return true;
        }

        public String getOption(){
            if (selectedIndex <= -1 && selectedIndex >= score.size()){
                return "";
            }
            return optionhead.get(selectedIndex);
        }
    }

    public int getTotalScore(){
        int result = 0;
        if (list == null || list.size() <= 0){
            return result;
        }
        for (Question bean:list){
            result += bean.getScore();
        }
        if (result < 0){
            result = 0;
        }
        if (result > 100){
            result = 100;
        }
        return result;
    }

    public String getQuestionTitle(int index){
        if (list == null || list.size() <= 0){
            return "";
        }
        if (index < 0 || index >= list.size()) {
            return "";
        }
        return list.get(index).getTitle();

    }

    public Question getQuestion(int index){
        if (list == null || list.size() <= 0){
            return null;
        }
        if (index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    public String getSelectedOptions(){
        if (list == null || list.size() <= 0){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Question bean:list){
            builder.append(bean.getOption());
        }
        return builder.toString();
    }

}

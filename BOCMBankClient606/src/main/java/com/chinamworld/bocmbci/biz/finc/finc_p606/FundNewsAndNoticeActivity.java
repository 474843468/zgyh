package com.chinamworld.bocmbci.biz.finc.finc_p606;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.fundnotice.FundnoticeRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.fundnotice.FundnoticeResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.fundnotice.FundnoticeResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsContent.NewsContentRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsContent.NewsContentResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsContent.NewsContentResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsList.NewsListRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsList.NewsListResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsList.NewsListResult;
import com.chinamworld.bocmbci.net.GsonTools;
import com.chinamworld.bocmbci.net.HttpHelp;
import com.chinamworld.bocmbci.net.model.BaseResponseData;
import com.chinamworld.bocmbci.net.model.IHttpErrorCallBack;
import com.chinamworld.bocmbci.net.model.IHttpResponseCallBack;
import com.chinamworld.bocmbci.net.model.IOkHttpErrorCode;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.refreshliseview.IRefreshLayoutListener;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class FundNewsAndNoticeActivity  extends FincBaseActivity{

    private String isNewsOrNotice;//0:公告 1：新闻
    private ListView notice_listview;
    private RefreshLayout notice_pull_refresh;
    /**刷新状态标示**/
    boolean isRefreshFlag = false;
    /** 总记录条数--默认0 */
    private int recordNumber = 0;
    /** 起始索引 */
    private int pageNo = 1;//
    private CommonAdapter<FundnoticeResult.FundnoticeItem> noticeAdapter;//公告
    private CommonAdapter<NewsListResult.NewsListItem> newsAdapter;//新闻
    private String fundCode;
    private String pageSize = "20";
    private List<FundnoticeResult.FundnoticeItem> noticeList = new ArrayList<>();//公告列表
    private List<NewsListResult.NewsListItem> newsList = new ArrayList<>();//新闻列表
    private String isNextPage = "N";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finc_more_notice);
        isNewsOrNotice = getIntent().getStringExtra("isNewsOrNotice");
        setTitle(isNewsOrNotice.equals("0") ? "更多公告" : "更多新闻");
        fundCode = (String)fincControl.fundDetails.get("fundCode");
        init();
    }

    private void init(){
        notice_listview = (ListView) findViewById(R.id.notice_query_list);
        notice_pull_refresh = (RefreshLayout) findViewById(R.id.notice_pull_refresh);
        if(isNewsOrNotice.equals("0")){
            getFundnotice(fundCode,pageNo+"",pageSize);
        }else{
            getNewsList(fundCode,pageNo+"",pageSize);
        }
        notice_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isNewsOrNotice.equals("0")){
                    fincControl.noticeItem = noticeList.get(i);
                    getNewsContent(noticeList.get(i).getIoriid());
                }else{
                    fincControl.newsItem = newsList.get(i);
                    getNewsContent(newsList.get(i).getContentId());
                }
            }
        });
        notice_pull_refresh.setOnRefreshListener(new IRefreshLayoutListener(){
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
                //TODO： 加载更多
                isRefreshFlag = true;
                if("N".equals(isNextPage)){
                    notice_pull_refresh.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                }else{
                    pageNo++;
                    if(isNewsOrNotice.equals("0")){
                        getFundnotice(fundCode,pageNo+"",pageSize);
                    }else{
                        getNewsList(fundCode,pageNo+"",pageSize);
                    }
                }

            }
        });
    }

    /**
     * 获得基金公告列表，分页数据
     * @param fundId 基金Id
     * @param pageNo 页数
     * @param pageSize 每页最大条数
     */
    private void getFundnotice(String fundId,String pageNo,String pageSize){
        FundnoticeRequestParams requestParams = new FundnoticeRequestParams(fundId,pageNo,pageSize);
        HttpHelp h= HttpHelp.getInstance();
        h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                FundnoticeResponseData data = GsonTools.fromJson(response,FundnoticeResponseData.class);
                FundnoticeResult body = data.getBody();
                List<FundnoticeResult.FundnoticeItem> itemList = body.getItem();
                isNextPage = body.getIsNextPage();
                noticeList.addAll(itemList);
                if(noticeAdapter == null){
                    noticeAdapter = new CommonAdapter<FundnoticeResult.FundnoticeItem>(FundNewsAndNoticeActivity.this, noticeList,myNoticeAdapter);
                    notice_listview.setAdapter(noticeAdapter);
                }else{
                    noticeAdapter.notifyDataSetChanged();
                    if(StringUtil.isNullOrEmpty(itemList)){
                        notice_pull_refresh.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                    }else{
                        notice_pull_refresh.loadmoreCompleted(RefreshDataStatus.Successed);
                    }
                }
                return false;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
    }

    /**
     * 获得基金新闻列表，分页数据
     * @param tags 标签、关键字
     * @param pageNo 页数
     * @param pageSize 每页最大条数
     */
    private void getNewsList(String tags,String pageNo,String pageSize){
        NewsListRequestParams requestParams = new NewsListRequestParams(tags,pageNo,pageSize);
        HttpHelp h= HttpHelp.getInstance();
        h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                NewsListResponseData data = GsonTools.fromJson(response,NewsListResponseData.class);
                NewsListResult body = data.getBody();
                List<NewsListResult.NewsListItem> itemList = body.getItem();
                isNextPage = body.getIsNextPage();
                newsList.addAll(itemList);
                if(newsAdapter == null){
                    newsAdapter = new CommonAdapter<NewsListResult.NewsListItem>(FundNewsAndNoticeActivity.this,newsList,myNewsAdapter);
                    notice_listview.setAdapter(newsAdapter);
                }else{
                    newsAdapter.notifyDataSetChanged();
                    if(StringUtil.isNullOrEmpty(itemList)){
                        notice_pull_refresh.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                    }else{
                        notice_pull_refresh.loadmoreCompleted(RefreshDataStatus.Successed);
                    }
                }
                return false;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
    }

    /**
     * 获取基金新闻内容
     * @param contentId
     */
    private void getNewsContent(String contentId){
        NewsContentRequestParams requestParams = new NewsContentRequestParams(contentId);
        HttpHelp h= HttpHelp.getInstance();
        h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                NewsContentResponseData data = GsonTools.fromJson(response,NewsContentResponseData.class);
                NewsContentResult body = data.getBody();
                fincControl.contentDetail = body;
                Map map = new HashMap();
                map.put("isNewsOrNotice",isNewsOrNotice);
                ActivityIntentTools.intentToActivityWithData(FundNewsAndNoticeActivity.this,FundNewsAndNoticeDetailActivity.class,map);
                return false;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
    }

    private ICommonAdapter<FundnoticeResult.FundnoticeItem> myNoticeAdapter = new ICommonAdapter<FundnoticeResult.FundnoticeItem>(){

        @Override
        public View getView(int i, FundnoticeResult.FundnoticeItem item, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = layoutInflater.inflate(
                        R.layout.finc_productdetail_listview_item, null);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.item_titile);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(item.getStrcaption());
            return convertView;
        }
    };

    private ICommonAdapter<NewsListResult.NewsListItem> myNewsAdapter = new ICommonAdapter<NewsListResult.NewsListItem>(){

        @Override
        public View getView(int i, NewsListResult.NewsListItem item, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = layoutInflater.inflate(
                        R.layout.finc_productdetail_listview_item, null);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.item_titile);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(item.getTitle());
            return convertView;
        }
    };

    private class ViewHolder {
        /** 标题 */
        public TextView title;
    }
}

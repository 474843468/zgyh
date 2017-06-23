package com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieDu on 2016/7/27.
 */
public class LoadMoreListHelper<M extends LoadMoreListHelper.ViewModel> {

    private M viewModel;
    private PullToRefreshLayout prlRefreshLayout;
    private boolean isLoadMore;

    public LoadMoreListHelper(final M viewModel, final PullToRefreshLayout prlRefreshLayout,
            final LoadListListener<M> loadListListener) {
        isLoadMore = false;
        this.viewModel = viewModel;
        this.prlRefreshLayout = prlRefreshLayout;
        prlRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (viewModel.getList().size() < viewModel.getRecordNumber()) {
                    isLoadMore = true;
                    viewModel.setCurrentIndex(viewModel.getCurrentIndex() + 1);
                    loadListListener.loadData(viewModel);
                } else {
                    prlRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });
    }

    public void onLoadSuccess() {
        if (isLoadMore) {
            isLoadMore = false;
            prlRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        }
    }

    public void onLoadFailed() {
        if (isLoadMore) {
            isLoadMore = false;
            prlRefreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
            viewModel.setCurrentIndex(viewModel.getCurrentIndex() - 1);
        }
    }

    public M getViewModel() {
        return viewModel;
    }

    public void setViewModel(M viewModel) {
        this.viewModel = viewModel;
    }

    public PullToRefreshLayout getPrlRefreshLayout() {
        return prlRefreshLayout;
    }

    public void setPrlRefreshLayout(PullToRefreshLayout prlRefreshLayout) {
        this.prlRefreshLayout = prlRefreshLayout;
    }

    public static class ViewModel<T> {
        private int currentIndex;
        private int pageSize;
        private int recordNumber;
        private List<T> list = new ArrayList<>();

        public int getCurrentIndex() {
            return currentIndex;
        }

        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getRecordNumber() {
            return recordNumber;
        }

        public void setRecordNumber(int recordNumber) {
            this.recordNumber = recordNumber;
        }

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }
    }

    public interface LoadListListener<M extends LoadMoreListHelper.ViewModel> {
        void loadData(M viewModel);
    }
}

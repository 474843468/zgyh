package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Category;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Menu;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.dao.AppStateDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.dao.HomeMenuDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.event.MenuRefreshEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter.ModuleListPresenter;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页功能列表fragment
 * Created by lxw on 2016/7/18 0018.
 */
public class ModuleListFragment extends BussFragment implements ModuleContract.View {

    private final static String APP_KEY_HOME_MENU = "home_menu";

    private View mRoot;
    private ExpandableListView mModuleList;
    private TextView tv_remainder;
    private TextView tv_added_num;
    private LinearLayout ll_select_count;
    private ModuleListPresenter moduleListPresenter;
    private State mState = State.OK;
    private TextView actionButton;
    private AppStateDao appStateDao;

    protected View onCreateView(LayoutInflater inflater) {
        mRoot = inflater.inflate(R.layout.boc_fragment_module_list, null);
        return mRoot;
    }

    @Override
    public void beforeInitView() {

    }

    private AllMenuAdapter menuAdapter;

    @Override
    public void initView() {
        mModuleList = (ExpandableListView) mRoot.findViewById(R.id.lv_module_list);
        tv_remainder = (TextView) mRoot.findViewById(R.id.tv_remainder);
        tv_added_num = (TextView) mRoot.findViewById(R.id.tv_added_num);
        ll_select_count = (LinearLayout) mRoot.findViewById(R.id.ll_select_count);
        appStateDao = new AppStateDao();

        this.mTitleBarView.setTitle("更多");

        actionButton = this.mTitleBarView.setRightButton("编辑", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mState == State.OK) {
                    mState = State.EDIT;
                    actionButton.setText("保存");
                    actionButton.setTextColor(Color.parseColor("#ff6666"));
                    ll_select_count.setVisibility(View.VISIBLE);
                } else {
                    ll_select_count.setVisibility(View.GONE);
                    mState = State.OK;
                    actionButton.setText("编辑");
                    menuAdapter.updateDate();
                    actionButton.setTextColor(Color.parseColor("#ff6666"));
                    HomeMenuDao dao = new HomeMenuDao();
                    try {
                        dao.updateMenuList(selectMenus);
                        appStateDao.updateAppState(APP_KEY_HOME_MENU);
                        showToast("菜单定制成功");
                    } catch (Exception e) {
                        showToast("菜单定制失败");
                    }
                }
            }
        });
        actionButton.setTextColor(Color.parseColor("#ff6666"));
    }

    private List<Item> selectMenus;

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectMenus = (ArrayList) bundle.getSerializable("selectMenu");
        }
        moduleListPresenter = new ModuleListPresenter(this);
        moduleListPresenter.getListData();
        setSelectMenuNum();
        initExpandListView();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initExpandListView() {
        menuAdapter = new AllMenuAdapter();
        mModuleList.setAdapter(menuAdapter);
        for (int i = 0; i < menuAdapter.getGroupCount(); i++) {
            mModuleList.expandGroup(i);
        }
        mModuleList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return true;
            }
        });
    }

    /**
     * 设置选择的数目提示
     */
    public void setSelectMenuNum() {
        tv_added_num.setText(selectMenus.size() + "");
        tv_remainder.setText(ApplicationConst.MAX_MENU_NUM - selectMenus.size()  + "");
    }

    @Override
    public void setListener() {
        mModuleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, position + "", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void setPresenter(ModuleContract.Presenter presenter) {
        // moduleListPresenter = presenter;
    }

    @Override
    public void updateView(ArrayList<Item> items) {
//        menuAdapter.setSelectedDatas(items);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    enum State {
        OK,
        EDIT
    }

    public class AllMenuAdapter implements ExpandableListAdapter {
        private Menu menu;
        private ArrayList<String> titleNames = new ArrayList<>();
        private LinkedTreeMap<String, Category> categorys;

        public void updateDate() {

        }

        public AllMenuAdapter() {
            menu = ApplicationContext.getInstance().getMenu();
            categorys = menu.getFilterCategory(new String[]{"specialService", "commonService", "financeSupermarket"});
            for (LinkedTreeMap.Entry<String, Category> entry : categorys.entrySet()) {
                titleNames.add(entry.getKey());
            }
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getGroupCount() {
            return titleNames.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return ((Category) categorys.get(titleNames.get(groupPosition))).getChildItems().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return categorys.get(titleNames.get(groupPosition));
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return getChildItem(groupPosition, childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            LinearLayout inflate = (LinearLayout) View.inflate(ModuleListFragment.this.getActivity(),
                    R.layout.boc_item_hot_product, null);
            TextView textView = (TextView)inflate.findViewById(R.id.tv_product_name);
            textView.setBackgroundColor(mContext.getResources().getColor(R.color.boc_main_bg_color));
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            textView.setText(categorys.get(titleNames.get(groupPosition)).getTitle());
            return inflate;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View childView = View.inflate(ModuleListFragment.this.getActivity(),
                    R.layout.boc_item_home_module, null);
            ViewFinder finder = new ViewFinder(childView);
            ImageView ivIcon = finder.find(R.id.ivIcon);
            TextView tvTitle = finder.find(R.id.tvTitle);
            CheckBox cbSelect = finder.find(R.id.cbSelect);
            LinearLayout llContainer = finder.find(R.id.llContainer);


            if (mState == State.EDIT) {
                cbSelect.setVisibility(View.VISIBLE);
            } else {
                cbSelect.setVisibility(View.INVISIBLE);
            }
            final Item item = getChildItem(groupPosition, childPosition);
            cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    boolean isHave = selectMenus.contains(item);
                    if (isChecked) {
                        if (!isHave) {
                            selectMenus.add(item);
                        }
                        if (selectMenus.size() > ApplicationConst.MAX_MENU_NUM) {
                            buttonView.setChecked(false);
                            //buttonView.setChecked并未回调onCheckedChanged，手动删除
                            selectMenus.remove(item);
                            Toast.makeText(mContext, mContext.getString(R.string.boc_home_menu_max_add_num, "7"), Toast.LENGTH_SHORT).show();
                            //showErrorDialog(mContext.getString(R.string.boc_is_max_add_num, "7"));
                        }
                    } else {
                        if (isHave)
                            selectMenus.remove(item);
                    }
                    setSelectMenuNum();
                }
            });

            llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mState == State.OK){
                        Item item = getChildItem(groupPosition, childPosition);
                        ActivityManager.getAppManager().finishActivity();
                        ModuleActivityDispatcher.dispatch(mActivity, item.getModuleId());
                    }
                }
            });

            cbSelect.setChecked(selectMenus.contains(getChildItem(groupPosition, childPosition)));
            tvTitle.setText(getChildItem(groupPosition, childPosition).getTitle());
            ivIcon.setBackgroundResource(getResId(item.getIconId(), R.drawable.class));
            return childView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }

        //根据父pos和子在父中的pos获取子item
        private Item getChildItem(int groupPos, int childPos) {
            return ((Category) categorys.get(titleNames.get(groupPos))).getChildItems().get(childPos);
        }
    }

    @Override
    public boolean onBack() {
        BocEventBus.getInstance().post(new MenuRefreshEvent());
        return true;
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    private int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}

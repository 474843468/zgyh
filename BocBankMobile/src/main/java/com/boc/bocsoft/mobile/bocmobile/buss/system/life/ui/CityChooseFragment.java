package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter.ChooseAdapter;
import java.util.List;

/**
 * 城市选择
 * Created by dingeryue on 2016年08月23.
 */
public class CityChooseFragment extends BussFragment {

  private ListView lvList;
  private ChooseAdapter<LifeVo.CityVo> innAdapter;
  private OnLocationChooseListener chooseListener;

  @Override protected View onCreateView(LayoutInflater mInflater) {
    return mInflater.inflate(R.layout.fragment_city_choose, null);
  }

  @Override public void beforeInitView() {
  }

  @Override public void initView() {
    lvList = mViewFinder.find(R.id.lv_list);
  }

  @Override public void initData() {
    innAdapter = new ChooseAdapter<LifeVo.CityVo>() {
      @Override public void decorateView(ItemView textView) {
        textView.getPaint().setFakeBoldText(true);
        textView.itemHeight(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_88px));
        textView.notFullDivider(true);
      }

      @Override public void updateView(ItemView itemView, LifeVo.CityVo data, int pos) {
        itemView.setText(data.getName());
        itemView.itemLast(pos == getCount()-1).setTag(Integer.valueOf(pos));
      }
    };
    lvList.setAdapter(innAdapter);
    List<LifeVo.CityVo> cityVoList = getArguments().getParcelableArrayList("data");
    innAdapter.update(cityVoList);
  }

  @Override public void setListener() {
    innAdapter.setChooseCallBack(new ChooseAdapter.ChooseCallBack() {
      @Override public void onItemClick(View v, int pos, Object data) {
        popTo(LocationChooseFragment.class, false);
        if (chooseListener != null) {
          chooseListener.OnChoose(innAdapter.getItem(pos));
          chooseListener = null;
        }
        pop();
      }

      @Override public boolean onCheckChange(boolean oldCheck, int pos, Object data) {
        return false;
      }

      @Override public void onAfterCheckChange() {

      }
    });
  }

  @Override protected boolean isDisplayRightIcon() {
    return false;
  }

  @Override protected boolean getTitleBarRed() {
    return false;
  }

  @Override protected String getTitleValue() {
    return getArguments().getString("province", "");
  }

  public void setLocationChooseListener(OnLocationChooseListener chooseListener) {
    this.chooseListener = chooseListener;
  }

  public interface OnLocationChooseListener {
    void OnChoose(LifeVo.CityVo cityVo);

    void onCancel();
  }
}

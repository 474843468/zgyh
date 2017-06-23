package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.presenter.LifePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter.ChooseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyding on 16/8/2.
 */
public class LocationChooseFragment extends BussFragment implements LifeContract.LocationChooseView{

  private ListView lvList;

  private LifePresenter lifePresenter;
  private ChooseAdapter<LifeVo.ProvinceVo> adapter;
  private CityChooseFragment.OnLocationChooseListener locationChooseLisetener;

  //private HotCityView<LifeVo.CityVo> viewHotCity;

  @Override protected View onCreateView(LayoutInflater mInflater) {
    return mInflater.inflate(R.layout.fragment_life_location_choose,null);
  }

  @Override public void beforeInitView() {

  }

  @Override public void initView() {
    lvList = mViewFinder.find(R.id.lv_list);
    TextView textView = this.mTitleBarView.setRightButton("取消", new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (locationChooseLisetener != null) {
          locationChooseLisetener.onCancel();
          pop();
        }
      }
    });
    textView.setTextColor(getResources().getColor(R.color.boc_text_color_red));
    textView.getPaint().setFakeBoldText(true);
    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.boc_text_size_small));


  /*  viewHotCity = new HotCityView<LifeVo.CityVo>(lvList.getContext()){
      @Override public void updateView(TextView textView, LifeVo.CityVo cityVo, int pos) {
        super.updateView(textView,cityVo,pos);
        textView.setText(cityVo.getName());
      }
    };
    viewHotCity.setTitle("热门城市");*/
    //lvList.addHeaderView(viewHotCity);
  }

  @Override public void initData() {
    lifePresenter = new LifePresenter(null);
    lifePresenter.setLocationView(this);

    adapter =new ChooseAdapter<LifeVo.ProvinceVo>() {
      @Override public void decorateView(ItemView itemView) {
        itemView.getPaint().setFakeBoldText(true);
        itemView.notFullDivider(true);
        itemView.itemHeight(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_88px));
        itemView.itemRightDrawable(R.drawable.boc_arrow_right);
      }

      @Override public void updateView(ItemView itemView, LifeVo.ProvinceVo data, int pos) {
        itemView.itemName(data.getName());
        itemView.itemLast(pos == adapter.getCount()-1);
      }
    };
    lvList.setAdapter(adapter);

    lifePresenter.loadProvinceList();
  }

  @Override public void setListener() {
    adapter.setChooseCallBack(new ChooseAdapter.ChooseCallBack<LifeVo.ProvinceVo>() {
      @Override public void onItemClick(View v, int pos, LifeVo.ProvinceVo data) {
        lifePresenter.loadCitysByProvince(data,false);
      }

      @Override public boolean onCheckChange(boolean oldCheck, int pos, LifeVo.ProvinceVo data) {
        return false;
      }

      @Override public void onAfterCheckChange() {

      }
    });

   /* viewHotCity.setItemClickListener(new HotCityView.HotCityClickListener<LifeVo.CityVo>() {
      @Override public void onItemClick(int pos, LifeVo.CityVo s) {
        LifeVo.ProvinceVo provinceVo = s.getProvinceVo();
        if(provinceVo == null){
          pop();
          return;
        }
        lifePresenter.loadCitysByProvince(new LifeVo.ProvinceVo(provinceVo.getName(),provinceVo.getShortName()),true);
      }
    });*/
  }

  @Override public void updateProvinceData(List<LifeVo.ProvinceVo> provinceVoList) {
    adapter.update(provinceVoList);
  }

  @Override public void updateHotCities(List<LifeVo.CityVo> cityVos) {
    //viewHotCity.updateData(cityVos);
  }

  @Override public void updateCityData(List<LifeVo.CityVo> cityVoList) {
    if(cityVoList == null|| cityVoList.size()==0){
      ToastUtils.show("暂无可选城市");
      return;
    }

    if(cityVoList.size() == 1){
      onCityChoose(cityVoList.get(0));
      return;
    }

    CityChooseFragment cityChooseFragment = new CityChooseFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) cityVoList);
    bundle.putString("province",cityVoList.get(0).getProvinceVo().getName());
    cityChooseFragment.setArguments(bundle);

    cityChooseFragment.setLocationChooseListener(locationChooseLisetener);
    start(cityChooseFragment);
  }

  @Override public void onCityChoose(LifeVo.CityVo cityVo) {
    if(locationChooseLisetener != null){
      locationChooseLisetener.OnChoose(cityVo);
      pop();
    }
  }

  @Override public void showLoading(String loading) {
    showLoadingDialog(loading);
  }

  @Override public void closeLoading() {
    closeProgressDialog();
  }

  @Override public void setPresenter(LifeContract.Presenter presenter) {

  }

  @Override public boolean onBack() {

    if(locationChooseLisetener != null){
      locationChooseLisetener.onCancel();
    }

    return true;
  }

  @Override protected void titleLeftIconClick() {
    getActivity().onBackPressed();
  }


  @Override protected String getTitleValue() {
    return "选择城市";
  }

  @Override protected boolean getTitleBarRed() {
    return false;
  }

  @Override protected boolean isDisplayRightIcon() {
    return false;
  }

  @Override protected boolean isDisplayLeftIcon() {
    return false;
  }

  public void setOnLocationChooseListener(CityChooseFragment.OnLocationChooseListener chooseListener){
    this.locationChooseLisetener = chooseListener;
  }
}

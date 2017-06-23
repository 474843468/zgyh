package com.boc.bocsoft.mobile.bocmobile.buss.system.invest;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Menu;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.AssetDetailVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.AssetVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.InvestItemVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view.PieChartView.PieChartItem;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view.PieInfoView.InfoData;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dingeryue on 2016年09月06.
 */
public class InvestTools {
  private final static  String TAG = InvestTools.class.getSimpleName();

  /**
   * 获取格式画的价格净值 , 小数点后4位
   * @param price
   * @return
   */
  public static String getFormatFuncingPrice(String price){
     return MoneyUtils.getRoundNumber(price, 4, BigDecimal.ROUND_HALF_UP);
  }

  /**
   * 基金
   * @param price
   * @return
   */
  public static String getFundFormatPrice(BigDecimal price){
    if(price == null)return "";
    return price.setScale(4, BigDecimal.ROUND_HALF_UP).toString();
  }

  public static String getFundFormatPrice(String price){
    if(price == null)return "";
    try {
      BigDecimal decimal = new BigDecimal(price);
      return decimal.setScale(4, BigDecimal.ROUND_HALF_UP).toString();
    }catch (Exception e){
      return "";
    }
  }

  /**
   * 基金日增长率
   * @param bigDecimal
   * @return
   */
  public static String getDayIncomeRatio(BigDecimal bigDecimal){
    if(bigDecimal == null)return "--";
    String value = bigDecimal.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
    return value;
  }


  /**
   * 获取预期年化收益率 显示
   * @param yearRR
   * @param rateDetail
   * @return
   */
  public static String getRateShow(String yearRR,String rateDetail){
    if(StringUtils.isEmptyOrNull(rateDetail)){
      return addPercent(yearRR);
    }

    try {

      if(new BigDecimal(rateDetail).compareTo(BigDecimal.ZERO) == 0){
        return addPercent(yearRR);
      }
    }catch (Exception e){
      return addPercent(yearRR);
    }

    return yearRR+"~"+rateDetail +"%";
  }


  /**
   * 是否是理财产品
   * @param vo
   * @return true |false
   */
  public static boolean isFinancing(InvestItemVo vo){
    return vo == null?false:"1".equals(vo.getProductType());
  }

  /**
   * 是否为基金产品
   * @param vo
   * @return
   */
  public static boolean isFund(InvestItemVo vo){
    return vo == null?false:"0".equals(vo.getProductType());
  }

  private static String getModuleId(String id){
    Menu menu = ApplicationContext.getInstance().getMenu();
    Item item = menu.findItemById(id);
    return item == null?"":item.getModuleId();
  }

  /**
   * 数值型字符转换成 %形式
   * @param value
   * @return
   */
  public static String getPercentValue(String value){
    NumberFormat percentInstance = NumberFormat.getPercentInstance();

    try {
      return percentInstance.format(Float.parseFloat(value));
    }catch (Exception e){
      LogUtils.d(TAG,"百分比转换异常:"+e.getMessage());
      return "--";
    }
  }

  public static String getPercentValue(BigDecimal bigDecimal){
    if(bigDecimal == null)return "";
    return getPercentValue(bigDecimal.toString());
  }

  public static String bigDecimalToString(BigDecimal decimal){
    return decimal==null?"--":decimal.toString();
  }

  public static String getShowValue(String value){
    return StringUtils.isEmptyOrNull(value)?"--":value;
  }

  /**
   * 理财起购金额
   * @param low
   * @return
   */
  public static String getFuncingLowLimit(String low,String cur){
    //WealthProductAdapter
    //String result = MoneyUtils.formatMoney(low);
    String result = MoneyUtils.getLoanAmountShownRMB1(low, cur);// 起购金额
    return getShowValue(result);
  }

  /**
   * 为值添加 百分号 不转换
   * @param value
   * @return
   */
  public static String addPercent(String value){
   if(StringUtils.isEmptyOrNull(value)){
     return "--";
   }
    return value+"%";
  }

  /**
   * 为值添加 百分号 不转换
   * @param value
   * @return
   */
  public static String addPercent(BigDecimal value){
    if(value == null){
      return "--";
    }
    return value.toString()+"%";
  }

  /**
   * 剩余额度
   * @param availamt
   * @return
   */
  public static String getAvailamt(String availamt){
    //String loanAmountShownRMB = MoneyUtils.getLoanAmountShownRMB(availamt);
    //WealthProductAdapter

    BigDecimal surplus = new BigDecimal(availamt);// 剩余额度
    String vaule = availamt;
    if (surplus.compareTo(BigDecimal.valueOf(1000000000)) == 1) {
      vaule = ApplicationContext.getInstance().getString(R.string.boc_wealth_money_ample);// 额度充足
    } else {
      try {
        vaule = MoneyUtils.formatMoney(availamt);
      }catch (Exception e){
      }
    }
    return getShowValue(vaule);
  }


  // --- 资产 详情 配置信息
  private List<AssetConfigItem> configItems;
  private final int MAX_PIE_SIZE = 5;
  private int[][] colors = new int[5][];
  {

    configItems = new ArrayList<>();
    //全行基金
    configItems.add(new AssetConfigItem().title("基金")
        .field("fundAmt")
        .target(ModuleCode.MODEUL_MINE_FUNC));

    //TODO
    configItems.add(new AssetConfigItem().title("第三方存管")
        .field("tpccAmt")
        .target(ModuleCode.MODULE_THIRD_MANANGER_0000));
    configItems.add(new AssetConfigItem().title("理财")
        .field("xpadAmt")
        .target(ModuleCode.MODULE_BOCINVT_0100));

    configItems.add(new AssetConfigItem().title("债券")
        .field("bondAmt")
        .target(ModuleCode.MODULE_BOND_0002));

    configItems.add(new AssetConfigItem().title("保险")
        .field("ibasAmt")
        .target(ModuleCode.MODULE_SAFETY_0003));
/*    configItems.add(new AssetConfigItem().title("账户贵金属")
        .field("actGoldAmt")
        .target(getModuleId("goldAccount_0000")));*/

    //全行贵金属 不可点击
    configItems.add(new AssetConfigItem().title("实物贵金属")
        .field("metalAmt")
        .target(""));

    configItems.add(new AssetConfigItem().title("双向宝")
        .field("forexAmt")
        .target(ModuleCode.MODULE_ISFOREXSTORAGECASH_3));

    //TODO
    //贵金属积存
    configItems.add(new AssetConfigItem().title("贵金属积存")
        .field("jxjAmt")
        .target(ModuleCode.MODULE_GOLD_STORE_0000));

    configItems.add(new AssetConfigItem().title("T+D")
        .field("autd")
        .target(""));//不可点击

    colors[0] = new int[]{0xfffc4d75};
    colors[1] = new int[]{0xff4bafd1};
    colors[2] = new int[]{0xff3082d6};
    colors[3] = new int[]{0xfffb6070};
    colors[4] = new int[]{0xffadb9c2};
  }



  /**
   * 将资产信息model 拆分
   * @param assetVo
   * @return
   */
  public List<AssetDetailVo> buildDetailVo(AssetVo assetVo) {

    List<AssetDetailVo> detailVos = new ArrayList<>();

    for (AssetConfigItem item : configItems) {
      try {
        Field field = assetVo.getClass().getDeclaredField(item.field());
        field.setAccessible(true);
        String value = (String) field.get(assetVo);
        float v = parseNumber(value);
        BigDecimal bigDecimal = getBigDecimal(value);
        if (bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
          continue;
        }
        AssetDetailVo detailVo = new AssetDetailVo();
        detailVo.setName(item.title());
        detailVo.setType(item.type());
        detailVo.setMoney(value);
        detailVo.setMoneyNumber(v);
        detailVo.setMoneyBigDecimal(bigDecimal);
        detailVos.add(detailVo);
      } catch (Exception e) {
        e.printStackTrace();
        LogUtils.d("dding", "get  " + item.field() + " fail");
      }
    }

    // 按规则 从大到小排序
    Collections.sort(detailVos, new Comparator<AssetDetailVo>() {
      @Override public int compare(AssetDetailVo lhs, AssetDetailVo rhs) {
        return rhs.getMoneyBigDecimal().compareTo(lhs.getMoneyBigDecimal());
      }
    });
    return detailVos;
  }

  /**
   * 构建资产饼图数据
   * @param detailVos
   * @return
   */
  public List<PieChartItem> buildPieItems(List<AssetDetailVo> detailVos){
    List<PieChartItem>  items = new ArrayList<>();
    if(detailVos == null || detailVos.size() == 0){
      return items;
    }

    float total = 0;
    for(AssetDetailVo detailVo:detailVos){
      total+=detailVo.getMoneyNumber();
    }
    //合并后5个数据
    float start = 180;
    int index = 0;

    boolean isNeedOther = (detailVos.size()>MAX_PIE_SIZE);
    PieChartItem otherItem = null;
    BigDecimal otherBigDecimal = BigDecimal.ZERO;
    if(isNeedOther){
      otherItem = new PieChartItem();
      otherItem.name = "其他";
      otherItem.info="";
    }

    for(AssetDetailVo detailVo:detailVos){

      if(isNeedOther && index>=MAX_PIE_SIZE-1){

        otherBigDecimal = otherBigDecimal.add(detailVo.getMoneyBigDecimal());
      }else{
        PieChartItem item = new PieChartItem();
        item.name = detailVo.getName();
        item.info = MoneyUtils.transMoneyFormat(detailVo.getMoney(),"001");
        item.shadeColors = colors[index];
        item.startPos = start;
        item.swipe = detailVo.getMoneyNumber()*360.f/total  ;
        start +=item.swipe;
        items.add(item);
      }
      index++;
    }
    if(isNeedOther){
      otherItem.info = MoneyUtils.transMoneyFormat(otherBigDecimal,"001");
      otherItem.startPos = start;
      otherItem.shadeColors = colors[MAX_PIE_SIZE-1];
      items.add(otherItem);
    }

    PieChartItem item = items.get(items.size() - 1);
    //修正位置 闭合360
    item.swipe = 180+360 - item.startPos;

    return items;
  }

  /**
   * 构建资产信息数据
   * @param detailVos
   * @return
   */
  public List<InfoData>  buildInfoItems(List<AssetDetailVo> detailVos){
    List<InfoData>  items = new ArrayList<>();
    if(detailVos == null || detailVos.size() == 0){
      return items;
    }

    BigDecimal total = BigDecimal.ZERO;

    int index = 0;
    for(AssetDetailVo detailVo:detailVos){
      InfoData data = new InfoData();

      total = total.add(detailVo.getMoneyBigDecimal());

      data.shaderColors = index<MAX_PIE_SIZE?colors[index]:colors[MAX_PIE_SIZE-1];
      data.money = MoneyUtils.transMoneyFormat(detailVo.getMoney(),"001");
      data.target = findAssetItemConfig(detailVo.getType()).target();
      items.add(data);
      index++;
    }
    //计算百分比
    index = 0;
    for(AssetDetailVo detailVo:detailVos){
      try {
        BigDecimal moneyBigDecimal = detailVo.getMoneyBigDecimal();

        BigDecimal result =
            moneyBigDecimal.multiply(new BigDecimal(100)).divide(total, 2, BigDecimal.ROUND_HALF_UP);
        detailVo.setPercent(result.toString()+"%");
        items.get(index).name = detailVo.getName()+"("+detailVo.getPercent()+")";
        index++;
      }catch (Exception e){
      }
    }
    return  items;
  }

  public static BigDecimal getTotalMoney(List<AssetDetailVo> detailVos){
    BigDecimal total = new BigDecimal(0);
    for(AssetDetailVo detailVo:detailVos){
      total= total.add(detailVo.getMoneyBigDecimal());
    }
    return total;
  }


  public static String getTotalMoneyFormat(BigDecimal total){
    if(total == null)return "0.00";

    return MoneyUtils.transMoneyFormat(total,"001");
  }

  private float parseNumber(String str){
    if(StringUtils.isEmptyOrNull(str))return 0;

    try {
     return Float.parseFloat(str);
    }catch (Exception e){
      try {
        return Float.parseFloat(str.replace(",",""));
      }catch (Exception e2){}
    }
    return 0;
  }


  private BigDecimal getBigDecimal(String in){
    try {
      return new BigDecimal(in);
    }catch (Exception e){
      return BigDecimal.ZERO;
    }
  }

  private AssetConfigItem findAssetItemConfig(String type){
    for(AssetConfigItem item:configItems){
      if(item.type().equals(type))return item;
    }
    return new AssetConfigItem();
  }

  private class AssetConfigItem{
    private String title; // 标题
    private String type;
    private String field; //对应class 中的字段
    private int[] colors;//颜色
    private String target;//跳转目标

    public AssetConfigItem title(String title){
      this.title = title;
      return this;
    }

    public String title(){
      return title;
    }


    public AssetConfigItem type(String type){
      this.type = type;
      return this;
    }

    public String type(){
      return type;
    }

    public AssetConfigItem field(String field){
      this.field = field;
      type(field);
      return this;
    }

    public String field(){
      return field;
    }

    public AssetConfigItem colors(int[] colors){
      this.colors = colors;
      return this;
    }

    public int[] colors(){
      return colors;
    }

    public AssetConfigItem target(String target){
      this.target = target;
      return this;
    }

    public String target(){
      return this.target;
    }
  }

}

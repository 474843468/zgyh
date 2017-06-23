package com.chinamworld.bocmbci.biz.peopleservice.ui;

import android.content.Context;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;

/**
 * 标签工厂类，根据标签名字返回相应的对象
 * 
 * @author：秦
 * @version： 1.0
 * @see 包名：com.chinamworld.btwapview.ui
 */
public class BTCLableFactory {

	/**
	 * 创建标签实例对象
	 * 
	 * @param context
	 * @param elementName
	 * @return
	 */
	public static BTCDrawLable createDrawLableInstance(Context context,BTCElement element) {
		String elementName = element.getElementName();
	    if (BTCLable.SERVICE.equals(elementName)) {
			return new BTCService(context,element);
		} else if (BTCLable.MENU.equals(elementName)) {
			return new BTCMenu(context,element);
		} else if (BTCLable.ITEM.equals(elementName)) {
			return new BTCItem(context,element);
		}else if (BTCLable.FLOW.equals(elementName)) {
			return new BTCFlow(context,element);
		}else if (BTCLable.STEP.equals(elementName)) {
			return new BTCStep(context,element);
		}else if (BTCLable.ACTION.equals(elementName)) {
			return new BTCAction(context,element);
		}else if (BTCLable.COMM.equals(elementName)) {
			return new BTCComm(context,element);
		}else if (BTCLable.SECURITY.equals(elementName)) {
			return new BTCSecurity(context,element);
		}else if (BTCLable.REQUEST.equals(elementName)) {
			return new BTCUIRequest(context,element);
		}else if (BTCLable.RESPONSE.equals(elementName)||"reponse".equals(elementName)) {
			return new BTCResponse(context,element);
		}else if (BTCLable.DATALIST.equals(elementName)) {
			return new BTCDatalist(context,element);
		}else if (BTCLable.PARAM.equals(elementName)) {
			return new BTCParam(context,element);
		}else if (BTCLable.VIEW.equals(elementName)) {
			return new BTCView(context,element);
		}else if (BTCLable.HIDDENBOX.equals(elementName)) {
			return new BTCHiddenbox(context,element);
		}else if (BTCLable.PANEL.equals(elementName)) { 
			return new BTCPanel(context,element);
		}else if (BTCLable.PAGETIP.equals(elementName)) {
			return new BTCPagetip(context,element);
		}else if (BTCLable.NEWLINE.equals(elementName)) {
			return new BTCNewline(context,element);
		}	else if (BTCLable.TABLE.equals(elementName)) {
			return new BTCTable(context,element);
		}else if (BTCLable.ROW.equals(elementName)) {
			return new BTCRow(context,element);
		}	else if (BTCLable.CELL.equals(elementName)) {
			return new BTCCell(context,element);
		}else if (BTCLable.PAGINATION.equals(elementName)) {
			return new BTCPagination(context,element);
		}else if (BTCLable.PAGEITEM.equals(elementName)) {
			return new BTCPageitem(context,element);
		}else if (BTCLable.PAGE.equals(elementName)) {
			return new BTCPage(context,element);
		}	else if (BTCLable.BUTTON.equals(elementName)) {
			return new BTCButton(context,element);
		}else if (BTCLable.FONT.equals(elementName)) {
			return new BTCFont(context,element);
		}	else if (BTCLable.ACTCOMBO.equals(elementName)) {
			return new BTCActcombo(context,element);
		}else if (BTCLable.ACTUSE.equals(elementName)) {
			return new BTCActuse(context,element);
		}else if (BTCLable.TEXTBOX.equals(elementName)) {
			return new BTCTextbox(context,element);
		}else if (BTCLable.COMBO.equals(elementName)) {
			return new BTCCombo(context,element);
		}else if (BTCLable.OPTION.equals(elementName)) {
			return new BTCOption(context,element);
		}	else if (BTCLable.RADIO.equals(elementName)) {
			return new BTCRadio(context,element);
		}else if (BTCLable.DATEINPUT.equals(elementName)) {
			return new BTCDateinput(context,element);
		}	else if (BTCLable.LINK.equals(elementName)) {
			return new BTCLink(context,element);
		}else if (BTCLable.SPACE.equals(elementName)) {
			return new BTCSpace(context,element);
		}	else if (BTCLable.USE.equals(elementName)) {
			return new BTCUse(context,element);
		}else if (BTCLable.TRANSLATE.equals(elementName)) {
			return new BTCTranslate(context,element);
		}else if (BTCLable.OPENWINDOW.equals(elementName)) {
			return new BTCOpenwindow(context,element);
		}else if (BTCLable.LOOP.equals(elementName)) {
			return new BTCLoop(context,element);
		}	else if (BTCLable.IF.equals(elementName)) {
			return new BTCIf(context,element);
		}else if (BTCLable.CHECKBOX.equals(elementName)) {
			return new BTCCheckbox(context,element);
		}	else if (BTCLable.OPTVALUE.equals(elementName)) {
			return new BTCOptvalue(context,element);
		}else if (BTCLable.REGISTERDB.equals(elementName)) {
			return new BTCRegisterdb(context,element);
		}	else if (BTCLable.DYNAMICTEXTBOX.equals(elementName)) {
			return new BTCDynamictextbox(context,element);
		}else if (BTCLable.FROMADATE.equals(elementName)) {
			return new BTCFormatdate(context,element);
		}else if (BTCLable.FROMATNUMBER.equals(elementName)) {
			return new BTCFormatnumber(context,element);
		}else if(BTCLable.SSM.equals(elementName)){
			return new BTCSsm(context,element);
		}else if(BTCLable.STAMP.equals(elementName)){
			return new BTCStamp(context,element);
		}
	
		return new BTCDrawLable(context,element);
	}
}

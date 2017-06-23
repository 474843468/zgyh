package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.adapter;

import java.util.List;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanStatusListModel;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ElaonAccoutAdapter extends BaseAdapter{
	
	private Context context;


	
	private List<EloanStatusListModel> mEloanList;
	
	public ElaonAccoutAdapter(Context context, List<EloanStatusListModel> eloanList) {
		this.context = context;
		mEloanList = eloanList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mEloanList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mEloanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler viewhodler =null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.boc_loan_account, null);
			ViewFinder finder = new ViewFinder(convertView);
			viewhodler = new ViewHodler(finder);
			convertView.setTag(viewhodler);
		
		} else {
			viewhodler = (ViewHodler) convertView.getTag();
		}
		
		if (mEloanList  != null && mEloanList.size() > 0) {
			if (!TextUtils.isEmpty(mEloanList.get(position).getQuoteType())
					&& mEloanList.get(position).getQuoteType().equals("01")) {
				viewhodler.eloanWStatell.setChoiceTextName("线上申请");
				
			} else if (!TextUtils.isEmpty(mEloanList.get(position).getQuoteType())
					&& mEloanList.get(position).getQuoteType().equals("02")) {
				viewhodler.eloanWStatell.setChoiceTextName("柜台申请");
			}
		}
		
		return convertView;
	}

	
	class ViewHodler {
		/**中银E贷在线申请布局*/
	    public EditChoiceWidget eloanWStatell;
	    
	    public ViewHodler(ViewFinder finder) {
	    	eloanWStatell = finder.find(R.id.eloanWStatell);
	    }
	} 
	
}

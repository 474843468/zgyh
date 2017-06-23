package com.boc.bocsoft.mobile.bocmobile.base.model;

import android.os.Parcelable;

/**
 * 作者：XieDu
 * 创建时间：2016/9/18 14:09
 * 描述：
 */
public interface BaseFillInfoBean extends Parcelable {
    String getCombinName();

    void setCombinName(String combinName);

    String get_combinId();

    void set_combinId(String _combinId);

    String getConversationId();

    void setConversationId(String conversationId);
}

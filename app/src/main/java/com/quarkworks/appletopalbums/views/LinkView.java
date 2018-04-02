package com.quarkworks.appletopalbums.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class LinkView extends AppCompatTextView {

    private String url;

    LinkView(Context pContext) {
        super(pContext);
        url = null;
    }

    LinkView(Context pContext, AttributeSet pAttributes) {
        super(pContext, pAttributes);
        url = null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String pUrl) {
        url = pUrl;
    }
}

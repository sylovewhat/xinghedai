package com.tc.emms.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.StatusBarUtils;

public class Test1Activity  extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        StatusBarUtils.setCompat(Test1Activity.this);
        StatusBarUtils.setStatusBarMode(Test1Activity.this,true);
        StatusBarUtils.setColor(Test1Activity.this, Color.GREEN);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(BaseResponse response, String jsonStr, int act) {

    }

    @Override
    public void onError(BusinessException e, int act) {

    }
}

package com.tc.emms.utils;

import com.tc.emms.dialog.CommonDialogListener;

/**
 * Created by 小本 on 2016/9/8.
 *
 * @version ${VERSION}
 * @decpter
 */
public class ToastUtils {

    private static ToastUtils instances;
    private static CommonDialogListener mListener;

    private ToastUtils(){

    }

    public void setListener(CommonDialogListener listener){
        mListener = listener;
    }

    public static ToastUtils getInstances(){
        if (instances == null)
        {
            synchronized (ToastUtils.class)
            {
                if (instances == null)
                {
                    instances = new ToastUtils();
                }
            }
        }
        return instances;
    }


    public void showDialog(String _title, String _summary){
        if(mListener!=null){
            mListener.show(_title, _summary);
        }
    }
    
    public void showWaittingDialog(){
        if(mListener!=null){
            mListener.showWaittingDialog();
        }
    }

    public void cancel(){
        if(mListener!=null){
            mListener.cancel();
        }
    }
    
    public void dissWaittingDialog(){
        if(mListener!=null){
            mListener.dissWaittingDialog();
        }
    }
}

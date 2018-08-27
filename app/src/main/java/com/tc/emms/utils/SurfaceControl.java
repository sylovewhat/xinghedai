package com.tc.emms.utils;

import android.graphics.Bitmap;
import android.view.View;
  
public class SurfaceControl {  
  
    public static Bitmap screenshot(View view) {  
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap bmp = view.getDrawingCache();  
        return bmp;  
    }  
} 

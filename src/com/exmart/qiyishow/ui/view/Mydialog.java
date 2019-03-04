package com.exmart.qiyishow.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Mydialog extends Dialog {

	private static int default_width = 400; //默认宽度 
    private static int default_height = 180;//默认高度 

	public Mydialog(Context context,View layout,int style) {
		this(context,default_width,default_height,layout,style);
		
	}

	public Mydialog(Context context, int width, int height,
			View layout, int style) {
		super(context,style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams parms = window.getAttributes();
		parms.gravity = Gravity.CENTER;
		window.setAttributes(parms);
	}
	
}

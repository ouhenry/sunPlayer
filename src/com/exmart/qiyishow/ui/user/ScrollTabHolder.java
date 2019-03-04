package com.exmart.qiyishow.ui.user;

import android.widget.AbsListView;

public interface ScrollTabHolder {

	void adjustScroll(int scrollHeight);

	void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}

package jc.house.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

public abstract class JCBaseFragment extends Fragment {
	protected View view;
	
	public void refresh() {
		Toast.makeText(this.getActivity(), "刷新数据或者是滑到最上面", Toast.LENGTH_SHORT).show();
	}

}

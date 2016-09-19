package cn.itxy.ListView_CheckDelete_Lianxi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	ListView listView;
	Context context;
	List<String> dataSource = new ArrayList<String>();
	List<String> selectItem = new ArrayList<String>();
	boolean isGouXuan = false;
	MyAdapter adapter;
	RelativeLayout relativaLayout;
	Button bt_cancel, bt_delete;
	TextView tv_count;
	static final String TAG = "静静";
	boolean abc = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		context = this;
		listView = (ListView) findViewById(R.id.lv);
		relativaLayout = (RelativeLayout) findViewById(R.id.relativaLayout);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_delete = (Button) findViewById(R.id.bt_delete);
		tv_count = (TextView) findViewById(R.id.tv_count);
		initDataSource();
		adapter = new MyAdapter(context, tv_count);
		listView.setAdapter(adapter);

		bt_cancel.setOnClickListener(this);
		bt_delete.setOnClickListener(this);

	}

	private void initDataSource() {
		for (int i = 0; i < 30; i++) {
			dataSource.add("小日本" + (i + 1));
		}
	}

	class MyAdapter extends BaseAdapter {
		LayoutInflater mInflater;
		Context context;
		TextView tv_count;
		HashMap<Integer, View> mViewMap;
		HashMap<Integer, Integer> visibleCheckMap;
		HashMap<Integer, Boolean> isCheckMap;

		public MyAdapter(Context context, TextView tv_count) {
			this.context = context;
			this.tv_count = tv_count;

			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.i(TAG, mInflater + "");

			mViewMap = new HashMap<Integer, View>();
			visibleCheckMap = new HashMap<Integer, Integer>();
			isCheckMap = new HashMap<Integer, Boolean>();

			if (isGouXuan) {

				if (abc) {
					for (int i = 0; i < dataSource.size(); i++) {
						isCheckMap.put(i, true);
						visibleCheckMap.put(i, CheckBox.VISIBLE);
					}
				} else {

					for (int i = 0; i < dataSource.size(); i++) {
						isCheckMap.put(i, false);
						visibleCheckMap.put(i, CheckBox.VISIBLE);
					}

				}
			} else {
				for (int i = 0; i < dataSource.size(); i++) {
					isCheckMap.put(i, false);

					visibleCheckMap.put(i, CheckBox.INVISIBLE);
				}
			}

			Log.i(TAG, isCheckMap + "");
			Log.i(TAG, visibleCheckMap + "================");
			Log.i(TAG, mViewMap + "");

		}

		@Override
		public int getCount() {
			return dataSource.size();
		}

		@Override
		public Object getItem(int position) {
			return dataSource.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = mViewMap.get(position);
			Log.i(TAG, position + "………………");
			Log.i(TAG, view + "………………");
			if (view == null) {
				view = mInflater.inflate(R.layout.activity_item, null);

				TextView textView = (TextView) view.findViewById(R.id.tv_name);

				final CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);

				textView.setText(dataSource.get(position));
				Log.i(TAG, dataSource.get(position) + "！！！");

				checkBox.setChecked(isCheckMap.get(position));
				Log.i(TAG, view + "——————");

				checkBox.setVisibility(visibleCheckMap.get(position));
				Log.i(TAG, visibleCheckMap.get(position) + "***");

				view.setOnLongClickListener(new MyOnclickListence());

				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (isGouXuan) {
							if (position == 0) {
								if (checkBox.isChecked()) {
									
								} else {
									abc = true;
									adapter.notifyDataSetChanged();

									for (int i = 0; i < dataSource.size(); i++) {

										isCheckMap.put(i, true);

										selectItem.add(dataSource.get(i));

									}

								}
							} else {

								if (checkBox.isChecked()) {
									Log.i(TAG, checkBox.isChecked() + "");
									checkBox.setChecked(false);
									selectItem.remove(dataSource.get(position));
									Log.i(TAG, selectItem + "");
								} else {
									checkBox.setChecked(true);
									selectItem.add(dataSource.get(position));

								}
								Log.i(TAG + "#", selectItem.size() + "");
								tv_count.setText("共选择" + selectItem.size());
								Log.i(TAG + "#", "");
							}
						} else {
							Toast.makeText(context, "不能勾选", 1).show();
						}

					}
				});
				mViewMap.put(position, view);

			}
			Log.i(TAG + "#", "");
			return view;
		}
	}

	class MyOnclickListence implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			isGouXuan = true;
			selectItem.clear();
			relativaLayout.setVisibility(View.VISIBLE);
			for (int i = 0; i < dataSource.size(); i++) {
				adapter.visibleCheckMap.put(i, CheckBox.VISIBLE);
			}
			adapter = new MyAdapter(context, tv_count);
			listView.setAdapter(adapter);

			return true;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_cancel:
			isGouXuan = false;
			selectItem.clear();
			adapter = new MyAdapter(context, tv_count);
			listView.setAdapter(adapter);
			relativaLayout.setVisibility(View.INVISIBLE);
			break;
		case R.id.bt_delete:
			isGouXuan = false;
			for (int i = 0; i < selectItem.size(); i++) {
				for (int j = 0; j < dataSource.size(); j++) {
					if (selectItem.get(i).equals(dataSource.get(j))) {
						dataSource.remove(j);
					}
				}
			}
			selectItem.clear();
			adapter = new MyAdapter(context, tv_count);
			listView.setAdapter(adapter);
			relativaLayout.setVisibility(View.INVISIBLE);
		default:
			break;
		}

	}

}

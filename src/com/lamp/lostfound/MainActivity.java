package com.lamp.lostfound;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.lamp.lostfound.base.EditPopupWindow;
import com.lamp.lostfound.bean.Found;
import com.lamp.lostfound.bean.Lost;
import com.lamp.lostfound.config.Constants;
import com.lamp.lostfound.i.IPopupItemClick;



public class MainActivity extends BaseActivity implements OnItemClickListener,OnClickListener,IPopupItemClick {
	private RelativeLayout layout_action;// 标题栏
	private LinearLayout layout_all; // 标题栏中间下拉视图
	private TextView tv_lost;
	private ListView listview;
	private Button btn_add;

	private Button layout_found;
	private Button layout_lost;
	private PopupWindow morePop;

	private RelativeLayout progress;
	private LinearLayout layout_no;
	private TextView tv_no;

	private List<Lost> losts;
	private LostAdapter lostAdapter;
	
	
	private List<Found> founds;
	private FoundAdapter foundAdapter;
	EditPopupWindow mPopupWindow;
	int position;
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void initViews() {
		progress = (RelativeLayout) findViewById(R.id.progress);
		layout_no = (LinearLayout) findViewById(R.id.layout_no);
		tv_no = (TextView) findViewById(R.id.tv_no);

		layout_action = (RelativeLayout) findViewById(R.id.layout_action);
		layout_all = (LinearLayout) findViewById(R.id.layout_all);
		// 默认是失物界面
		tv_lost = (TextView) findViewById(R.id.tv_lost);
		tv_lost.setTag("lost");
		listview = (ListView) findViewById(R.id.list_lost);
		btn_add = (Button) findViewById(R.id.btn_add);
		initEditPop();
	}
	
	private void initEditPop() {
		mPopupWindow = new EditPopupWindow(this, 200, 48);
		mPopupWindow.setOnPopupItemClickListner(this);
	}

	@Override
	public void initListeners() {
		layout_all.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		btn_add.setOnClickListener(this);
	}

	@Override
	public void initData() {
		findLostAll();
	}

	/**
	 * 查询LOST列表
	 */
	private void findLostAll() {
		BmobQuery<Lost> query = new BmobQuery<Lost>();
		query.order("-createdAt");//按时间降序排列
		query.findObjects(this, new FindListener<Lost>() {
			
			@Override
			public void onSuccess(List<Lost> arg0) {
				MainActivity.this.losts = arg0;
				lostAdapter = new LostAdapter(losts);
				listview.setAdapter(lostAdapter);
				showView();
			}

			@Override
			public void onError(int arg0, String arg1) {
				showErrorView(0);
			}
		});
	}
	/**
	 * 请求出错的时候显示
	 * @param i
	 */
	protected void showErrorView(int i) {
		progress.setVisibility(View.GONE);
		listview.setVisibility(View.GONE);
		layout_no.setVisibility(View.VISIBLE);
		if(i==0){
			tv_no.setText(R.string.list_no_data_lost);
		}else{
			tv_no.setText(R.string.list_no_data_found);
		}
	}

	/**
	 * 请求正确的显示
	 */
	private void showView() {
		listview.setVisibility(View.VISIBLE);
		layout_no.setVisibility(View.GONE);
		progress.setVisibility(View.GONE);
	}
	@Override
	public void onEdit(View v) {
		String tags = tv_lost.getTag().toString();
		if("lost".equals(tags)){
			Lost lost = (Lost) lostAdapter.getItem(position);
			Intent intent = new Intent(this,AddActivity.class);
			intent.putExtra("lost", lost);
			intent.putExtra("from", "edit_lost");
			startActivityForResult(intent, Constants.REQUESTCODE_ADD);
		}else if("found".equals(tv_lost.getTag().toString())){
			Found f = (Found)foundAdapter.getItem(position);
			Intent intent = new Intent(this,AddActivity.class);
			intent.putExtra("found", f);
			intent.putExtra("from", "edit_found");
			startActivityForResult(intent, Constants.REQUESTCODE_ADD);
		}
		
	}

	@Override
	public void onDelete(View v) {
		String tags = tv_lost.getTag().toString();
		if("lost".equals(tags)){
			Lost l = (Lost) lostAdapter.getItem(position);
			l.delete(this, l.getObjectId(), new DeleteListener() {
				
				@Override
				public void onSuccess() {
					ShowToast("删除成功");
					losts.remove(position);
					lostAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}else if("found".equals(tags)){
			Found f = (Found) foundAdapter.getItem(position);
			f.delete(this, f.getObjectId(), new DeleteListener() {
				
				@Override
				public void onSuccess() {
					ShowToast("删除成功");
					founds.remove(position);
					foundAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

	/**
	 * 点击监听事件
	 */
	@Override
	public void onClick(View v) {
		if(v == btn_add){//添加按钮
			Intent intent = new Intent(this, AddActivity.class);
			intent.putExtra("from", tv_lost.getTag().toString());
			startActivityForResult(intent, Constants.REQUESTCODE_ADD);
		}else if(v == layout_all){
			showListPop();
		}else if(v == layout_found){
			tv_lost.setText("招领");
			tv_lost.setTag("found");
			morePop.dismiss();
			findFoundAll();
		}else if(layout_lost==v){
			//表示点击了失物
			tv_lost.setText("失物");
			tv_lost.setTag("lost");
			morePop.dismiss();
			findLostAll();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==Constants.REQUESTCODE_ADD&&resultCode==RESULT_OK){
			if("lost".equals(tv_lost.getTag())){
				Lost lost = data.getParcelableExtra("lost");
				String lost_op = data.getStringExtra("lost_op");
				if("add".equals(lost_op)){
					losts.add(lost);
				}else if("update".equals(lost_op)){
					Lost old = (Lost) lostAdapter.getItem(position);
					old.setTitle(lost.getTitle());
					old.setPhone(lost.getPhone());
					old.setDescribe(lost.getDescribe());
				}
				lostAdapter.notifyDataSetChanged();
			}else if("found".equals(tv_lost.getTag())){
				Found f = data.getParcelableExtra("found");
				String found_op = data.getStringExtra("found_op");
				if("add".equals(found_op)){
					founds.add(0, f);
				}else if("update".equals(found_op)){
					Found old = (Found) foundAdapter.getItem(position);
					old.setTitle(f.getTitle());
					old.setDescribe(f.getDescribe());
					old.setPhone(f.getPhone());
				}
				foundAdapter.notifyDataSetChanged();
			}
		}
	}

	private void findFoundAll() {
		BmobQuery<Found> query = new BmobQuery<Found>();
		query.order("-createdAt");
		query.findObjects(this, new FindListener<Found>() {
			
			@Override
			public void onSuccess(List<Found> arg0) {
				MainActivity.this.founds = arg0;
				foundAdapter = new FoundAdapter();
				listview.setAdapter(foundAdapter);
				showView();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				showErrorView(0);
			}
		});
	}

	/**
	 * 下拉列表
	 */
	private void showListPop() {
		View v = getLayoutInflater().inflate(R.layout.pop_lost, null);
		layout_found = (Button) v.findViewById(R.id.layout_found);
		layout_lost = (Button) v.findViewById(R.id.layout_lost);
		//注册监听事件
		layout_found.setOnClickListener(this);
		layout_lost.setOnClickListener(this);
		morePop = new PopupWindow(v, mScreenWidth, 600);
		
		//触屏监听事件
		morePop.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
					morePop.dismiss();
					return true;
				}
				return false;
			}
		});
		
		morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		morePop.setTouchable(true);
		morePop.setFocusable(true);
		morePop.setBackgroundDrawable(new BitmapDrawable());
		//设置动画效果
		morePop.setAnimationStyle(R.style.MenuPop);
		morePop.showAsDropDown(layout_action, 0, -dip2px(this, 2.0f));
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		this.position = arg2;//得到点击位置
		int[] location = new int[2];
		arg1.getLocationOnScreen(location);//得到坐标
		//设置弹窗的位置
		mPopupWindow.showAtLocation(arg1, Gravity.RIGHT | Gravity.TOP, location[0], getStateBar() + location[1]);
	}
	
	//招领适配器
		class FoundAdapter extends BaseAdapter{

			@Override
			public int getCount() {
				return founds.size();
			}

			@Override
			public Object getItem(int position) {
				return founds.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder vh;
				if (convertView == null) {
					convertView = getLayoutInflater().inflate(R.layout.item_list,
							null);
					vh = new ViewHolder();
					vh.tv_title = (TextView) convertView
							.findViewById(R.id.tv_title);
					vh.tv_describe = (TextView) convertView
							.findViewById(R.id.tv_describe);
					vh.tv_phone = (TextView) convertView
							.findViewById(R.id.tv_phone);
					vh.tv_createdAt = (TextView) convertView
							.findViewById(R.id.tv_time);
					convertView.setTag(vh);
				}
				vh = (ViewHolder) convertView.getTag();
				Found found = founds.get(position);
				vh.tv_title.setText(found.getTitle());
				vh.tv_describe.setText(found.getDescribe());
				vh.tv_phone.setText(found.getPhone());
				vh.tv_createdAt.setText(found.getCreatedAt());
				return convertView;
			}
			
		}

		// 失物适配器
		class LostAdapter extends BaseAdapter {
			private List<Lost> losts;

			public LostAdapter(List<Lost> losts) {
				this.losts = losts;
			}

			@Override
			public int getCount() {
				return losts.size();
			}

			@Override
			public Object getItem(int position) {
				return losts.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder vh;
				if (convertView == null) {
					convertView = getLayoutInflater().inflate(R.layout.item_list,
							null);
					vh = new ViewHolder();
					vh.tv_title = (TextView) convertView
							.findViewById(R.id.tv_title);
					vh.tv_describe = (TextView) convertView
							.findViewById(R.id.tv_describe);
					vh.tv_phone = (TextView) convertView
							.findViewById(R.id.tv_phone);
					vh.tv_createdAt = (TextView) convertView
							.findViewById(R.id.tv_time);
					convertView.setTag(vh);
				}
				vh = (ViewHolder) convertView.getTag();
				Lost lost = losts.get(position);
				vh.tv_title.setText(lost.getTitle());
				vh.tv_describe.setText(lost.getDescribe());
				vh.tv_phone.setText(lost.getPhone());
				vh.tv_createdAt.setText(lost.getCreatedAt());
				return convertView;
			}


		}
		class ViewHolder {
			TextView tv_title;
			TextView tv_describe;
			TextView tv_phone;
			TextView tv_createdAt;
		}
		
	

}

package com.lamp.lostfound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
/**
 * ����ҳ
 * @author xyzj9_000
 *
 */
@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity {

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_splash);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		handler.sendEmptyMessageDelayed(GO_HOME, 3000);//����һ���ӳٷ��͵Ŀ���Ϣ
	}
	private static final int GO_HOME = 0x1;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GO_HOME:
				gohome();
				break;

			default:
				break;
			}
		};
	};
	/**
	 * ���������溯��
	 */
	public void gohome(){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
		
	}

}

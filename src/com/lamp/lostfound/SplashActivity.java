package com.lamp.lostfound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
/**
 * 闪屏页
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
		handler.sendEmptyMessageDelayed(GO_HOME, 3000);//设置一个延迟发送的空消息
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
	 * 跳到主界面函数
	 */
	public void gohome(){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
		
	}

}

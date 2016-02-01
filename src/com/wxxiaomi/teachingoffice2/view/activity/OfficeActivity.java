package com.wxxiaomi.teachingoffice2.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wxxiaomi.mingdialog.dialog.MingEditDialog;
import com.wxxiaomi.mingdialog.dialog.MingEditDialog.OnSweetClickListener;
import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_lib_Login;
import com.wxxiaomi.teachingoffice2.engine.LibraryEngineImpl;
import com.wxxiaomi.teachingoffice2.engine.OfficeEngineImpl;
import com.wxxiaomi.teachingoffice2.utils.SharePrefUtil;
import com.wxxiaomi.teachingoffice2.view.fragment.ClassFormFragment;
import com.wxxiaomi.teachingoffice2.view.fragment.LeftFragment;
import com.wxxiaomi.teachingoffice2.view.fragment.LibUserInfoFragment;
import com.wxxiaomi.teachingoffice2.view.fragment.OfficeWithoutLoginFragment;
import com.wxxiaomi.teachingoffice2.view.fragment.base.BaseFragment;
import com.wxxiaomi.teachingoffice2.view.fragment.base.FragmentCallback;

public class OfficeActivity extends SlidingFragmentActivity implements
		FragmentCallback, OnClickListener {

	private SlidingMenu sm;
	private Fragment contentFragment;
	private Fragment leftFragment;
	private Html_Main main;
	private Html_Lib_Main html_lib_main;
	private MainChangeListener lis;
	private LibMainChangeListener libLis;
//	private ImageView iv_navigation;
	
	private Html_lib_Login temp_lib_login_page;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
//		iv_navigation = (ImageView) findViewById(R.id.iv_navigation);
//		iv_navigation.setOnClickListener(this);
		Intent intent = getIntent();
		boolean isLoginSuccess = intent.getBooleanExtra("isLoginSuccess", false);
		if(isLoginSuccess){
			main = (Html_Main) intent.getBundleExtra("value").get("info");
		}else{
			if(ConstantValue.isRemOfficeLogin){
				//记住的那个密码登录失败
			}
		}

		// 设置SlidingMenu的layout
		setBehindContentView(R.layout.menu_frame);

		if (savedInstanceState == null) {
			if(isLoginSuccess){
				contentFragment = new ClassFormFragment();
			}else{
			contentFragment = new OfficeWithoutLoginFragment();
			}
		} else {
			// 取出之前保存的contentFragment
			contentFragment = this.getSupportFragmentManager().getFragment(
					savedInstanceState, "contentFragment");
		}
		// 设置当前的fragment
		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, contentFragment).commit();

		// 设置SlidingMenu属性和layout
		sm = getSlidingMenu();// 得到SlidingMenu的对象
		sm.setMode(SlidingMenu.LEFT);// 设置slidingmenu滑动的方式
		sm.setShadowDrawable(R.drawable.shadow);// 设置slidingmenu边界的阴影图片
		sm.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置slidingmenu宽度
		// sm.setBehindWidth(400);//设置slidingmenu宽度
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置滑出slidingmenu范围
		sm.setMenu(R.layout.menu_frame);
		sm.setBehindCanvasTransformer(null);// 设置slidingmenu动画

		// 设置menu的fragment
		leftFragment = new LeftFragment();
		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, leftFragment).commit();
		// 设置在slidingmenu页显示ActionBar
		// setSlidingActionBarEnabled(false);
		// 是ActionBar的图标可以被点击
		getSupportActionBar().setHomeButtonEnabled(true);
		// 启用向左的图标
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setCustomView(LayoutInflater.from(this).inflate(R.layout.layout_title_bar, null));
//		getSupportActionBar().setDisplayShowCustomEnabled(true);
//		getSupportActionBar().setDisplayShowHomeEnabled(false);
//		getSupportActionBar().setDisplayShowTitleEnabled(false);
		

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			sm.toggle();// 动态打开或关闭slidingmenu
			break;

		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	public Html_Lib_Main getLibMain() {
		return html_lib_main;
	}



	/**
	 * 切换fragment
	 * 
	 * @param f
	 * @param state
	 */
	public void switchFragment(Fragment f, int state) {
		if (state == 0) {
			contentFragment = f;
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, contentFragment).commit();
			sm.toggle();
		} else if (state == 1) {
				
		} else if (state == 2) {

		}else if(state == 3){
			
		}else if(state == ConstantValue.CLICKTHESAMEFRAGMENT){
			sm.toggle();
		}

	}

	public Html_Main getMain() {
		return main;
	}

	@Override
	public void onFragmentCallback(BaseFragment fragment, int id, Bundle args) {
		switch (id) {
		// 处理登录过期
		case 0:
			getMainByCache();
			break;
		case ConstantValue.STATE_LIBOUTTIME:
			//处理图书馆cookie过期
			getLibMainBecauseCookie(html_lib_main.getUsername(),html_lib_main.getPassword());
			break;
		case ConstantValue.LIBFIRSTNOLOGIN:
			//图书馆登录记住密码的情况下第一次点击
			//取出缓存中的账号密码并要求用户输入验证码然后连接服务器获取libmain
			String libUserName = SharePrefUtil.getString(this, "libUserName", "");
			String libPassword = SharePrefUtil.getString(this, "libPassword", "");
			getLibMainBecauseCookie(libUserName,libPassword);
			break;
			

		default:
			break;
		}
	}

	private void getLibMainBecauseCookie(final String username,final String password) {
		/**
		 * 1.弹出dialog,dialog里面有验证码
		 * 
		 */
		
		final MingEditDialog mDialog = new MingEditDialog(OfficeActivity.this, MingEditDialog.CODEPIC);
		mDialog.setTitleText("");
		mDialog.setConfirmText("确定");
		mDialog.setConfirmClickListener(new OnSweetClickListener() {
			@Override
			public void onClick(final MingEditDialog sweetAlertDialog) {
				//第一个dialog的确定按钮事件，就要联网验证
				final MingEditDialog checkingDialog = new MingEditDialog(OfficeActivity.this, MingEditDialog.PROGRESS);
				checkingDialog.setTitleText("验证中").changeAlertType(MingEditDialog.PROGRESS);
				checkingDialog.show();
				final String code = sweetAlertDialog.getEditText();
				new AsyncTask<String, Void, ResponseData<Html_Lib_Main>>() {
					@Override
					protected ResponseData<Html_Lib_Main> doInBackground(String... params) {
						LibraryEngineImpl impl = new LibraryEngineImpl();
						return impl.Login(temp_lib_login_page,username, password, code);
					}

					@Override
					protected void onPostExecute(ResponseData<Html_Lib_Main> result) {
						checkingDialog.dismiss();
						if (result.isSuccess()) {
							Log.i("wang", "officeactivity-Login_success");
							html_lib_main = result.getObj();
							libLis.change(html_lib_main);
							mDialog.dismiss();
						} else {
							new MingEditDialog(OfficeActivity.this, MingEditDialog.ERROR)
							.setTitleText(result.getError())
							.show();
						}
						super.onPostExecute(result);
					}
				}.execute();
				
			}
		});
		mDialog.show();
		/**
		 * 联网获取验证码
		 * 
		 */
		new AsyncTask<String, Void, ResponseData<Html_lib_Login>>() {
			@Override
			protected ResponseData<Html_lib_Login> doInBackground(String... params) {
				LibraryEngineImpl impl = new LibraryEngineImpl();
				return impl.getLibLoginPageAndCodePic();
			}

			@Override
			protected void onPostExecute(ResponseData<Html_lib_Login> result) {
				if (result.isSuccess()) {
					temp_lib_login_page = result.getObj();
					mDialog.setCustomImage(result.getObj().getPicCode());
				} else {

				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}


	private void getMainByCache() {
		new AsyncTask<String, Void, ResponseData<Html_Main>>() {
			@Override
			protected ResponseData<Html_Main> doInBackground(String... params) {
				OfficeEngineImpl impl = new OfficeEngineImpl();
//				return impl.getOfficeMainHtml2BeanByCache(main.getUsername(),main.getPassword());
				return impl.getOfficeMainHtml2BeanByOne(main.getUsername(),main.getPassword());
			}

			@Override
			protected void onPostExecute(ResponseData<Html_Main> result) {
				if (result.isSuccess()) {
					main = result.getObj();
					lis.change(main);
				} else {

				}
				super.onPostExecute(result);
			}
		}.execute();

	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.add:
//			// main.setElectiveCourseUrl("http://210.38.162.116/(bpgrh52tyeiaan5524nsvn55)/xsxkqk.aspx?xh=131110199&xm=%CD%F5%BA%C6%C3%F7&gnmkdm=N121615");
////			html_lib_main.setCookie("asd");
//			break;
//		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		//图书馆登录之后返回回来的
		case 1:
			Bundle bundleExtra = data.getBundleExtra("value");
			html_lib_main = (Html_Lib_Main) bundleExtra.get("html_lib_main");
			switchFragment(new LibUserInfoFragment(), 0);
			libLis.change(html_lib_main);
//			Log.i("wang", "cookie="+html_lib_main.getCookie());
			break;
		case 4:
			Bundle bundleExtra2 = data.getBundleExtra("value");
			main = (Html_Main) bundleExtra2.get("result");
			// 刷新所有布局
			switchFragment(new ClassFormFragment(), 0);
			// sm.toggle();
			lis.change(main);
		default:
			break;
		}
	}

	public interface MainChangeListener {
		void change(Html_Main main);
	}

	public void setMainChangeListener(MainChangeListener lis) {
		this.lis = lis;
	}
	
	public interface LibMainChangeListener{
		void change(Html_Lib_Main html_lib_main);
	}
	public void setLibMainChangeListener(LibMainChangeListener libLis) {
		this.libLis = libLis;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 1.bundle
		// 2.存放的ID
		// 3.当前要保存的fragment的实例
		this.getSupportFragmentManager().putFragment(outState,
				"contentFragment", contentFragment);
	}
	
//	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_navigation:
			sm.toggle();
			break;

		default:
			break;
		}
	}
}

package com.example.jinanuniversity.data.bo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class BoBaseActivity extends Activity{
	private static final String TAG = BoBaseActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	//��Ļ��ʾ������
	public static final int FEATURE_NO_TITLE 	= 0x0001;
	public static final int FLAG_FULLSCREEN 	= 0x0002;
	public static final int SCREEN_VERTICAL 	= 0x0004;
	public static final int SCREEN_HORIZONTAL	= 0x0008;

	//����Ԥ��������
	public static final int NO_TITLE_AND_FULLSCREEN = FEATURE_NO_TITLE|FLAG_FULLSCREEN;
	public static final int NO_TITLE_AND_VERTICAL = FEATURE_NO_TITLE|SCREEN_VERTICAL;
	public static final int NO_TITLE_AND_HORIZONTAL = FEATURE_NO_TITLE|SCREEN_HORIZONTAL;
	public static final int NO_TITLE_AND_FULLSCREEN_AND_VERTICAL = FEATURE_NO_TITLE|FLAG_FULLSCREEN|SCREEN_VERTICAL;
	public static final int NO_TITLE_AND_FULLSCREEN_AND_HORIZONTAL = FEATURE_NO_TITLE+FLAG_FULLSCREEN|SCREEN_HORIZONTAL;

	public static final int FULLSCREEN_AND_VERTICAL = FLAG_FULLSCREEN|SCREEN_VERTICAL;
	public static final int FULLSCREEN_AND_HORIZONTAL = FLAG_FULLSCREEN|SCREEN_HORIZONTAL;
	
	/**
	 * ����ȫ��<br>
	 *  setContentView(layoutResId)ǰ����
	 * */
	protected void setScreenFull() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * ��������ϵͳĬ�ϱ��� <br>
	 * setContentView(layoutResId)ǰ����
	 * */
	protected void setNoTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * ���ü���<br>
	 * setContentView(layoutResId)ǰ����
	 * */
	protected void setScreenVertical() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * ���ú��� <br>
	 * setContentView(layoutResId)ǰ����
	 * */
	protected void setScreenHorizontal() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * ������Ļ
	 * @param ��־
	 * */
	protected void setScreen(int flag) {
		switch (flag) {
		case FEATURE_NO_TITLE:
			setNoTitle();
			break;
		case FLAG_FULLSCREEN:
			setScreenFull();
			break;
		case SCREEN_VERTICAL:
			setScreenVertical();
			break;
		case SCREEN_HORIZONTAL:
			setScreenHorizontal();
			break;
		case NO_TITLE_AND_FULLSCREEN:
			setNoTitle();
			setScreenFull();
			break;
		case NO_TITLE_AND_VERTICAL:
			setNoTitle();
			setScreenVertical();
			break;
		case NO_TITLE_AND_HORIZONTAL:
			setNoTitle();
			setScreenHorizontal();
			break;
		case NO_TITLE_AND_FULLSCREEN_AND_VERTICAL:
			setNoTitle();
			setScreenFull();
			setScreenVertical();
			break;
		case NO_TITLE_AND_FULLSCREEN_AND_HORIZONTAL:
			setNoTitle();
			setScreenFull();
			setScreenHorizontal();
			break;
		case FULLSCREEN_AND_VERTICAL:
			setScreenFull();
			setScreenVertical();
			break;
		case FULLSCREEN_AND_HORIZONTAL:
			setScreenFull();
			setScreenHorizontal();
			break;
		}
	}
	
	
	/**�������������<br>
	 * setContentView(layoutResId)ǰ����*/
	protected void setSoftInputMode(){
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	public final static String INTENT_KEY_OVERRIDE 	= "key_override";
	public final static int OVERRIDE_DEFAULT 		= 0;
	public final static int OVERRIDE_LEFT_TO_RIGHT 	= 10;
	public final static int OVERRIDE_RIGHT_TO_TLEFT	= ~OVERRIDE_LEFT_TO_RIGHT;
	protected int override = OVERRIDE_DEFAULT;
	
	
	/**���������Ƴ�*/
	protected void transitionLeftToRight(){
		transitionLeftToRight(this);
	}
	
	/**���������Ƴ�*/
	protected void transitionLeftToRight(Activity activity){
		if(Integer.parseInt(Build.VERSION.SDK)>=5){
			activity.overridePendingTransition(idAnim("boframework_slide_in_left"), idAnim("boframework_slide_out_right"));
		}
	}
	
	/**���������Ƴ�*/
	protected void transitionRightToLeft(){
		transitionRightToLeft(this);
	}
	
	/**���������Ƴ�*/
	protected void transitionRightToLeft(Activity activity){
		if(Integer.parseInt(Build.VERSION.SDK)>=5){
			activity.overridePendingTransition(idAnim("boframework_slide_in_right"), idAnim("boframework_slide_out_left"));
		}
	}
	
	/**��С�Ƴ�*/
	protected void transitionZoomin(){
		transitionRightToLeft(this);
	}
	
	/**��С�Ƴ�*/
	protected void transitionZoomin(Activity activity){
		if(Integer.parseInt(Build.VERSION.SDK)>5){
			activity.overridePendingTransition(idAnim("boframework_action_zoomin"), idAnim("boframework_action_zoomout"));    
		}
	}
	
	protected void doStartActivity(Class<?> cls,int override){
		doStartActivity(this,cls,override);
	}
	
	protected void doStartActivity(Activity activity,Class<?> cls,int override){
		doStartActivity(activity,cls,false,override);
	}
	
	protected void doStartActivity(Class<?> cls,boolean finish,int override){
		doStartActivity(this,cls,finish,override);
	}
	
	protected void doStartActivity(Activity activity,Class<?> cls,boolean finish,int override){
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		doStartActivity(activity,intent,finish,override);
	}
	
	protected void doStartActivity(Intent intent,int override){
		doStartActivity(this,intent,override);
	}
	
	public void doStartActivity(Activity activity,Intent intent,int override){
		doStartActivity(activity,intent,false,override);
	}
	
	protected void doStartActivity(Intent intent,boolean finish,int override){
		doStartActivity(this,intent,finish,override);
	}
	
	protected void doStartActivity(Activity activity,Intent intent,boolean finish,int override){
		intent.putExtra(INTENT_KEY_OVERRIDE, override);
		startActivity(intent);
		doFinish(activity,finish,override);
	}
	
	protected void doFinish(){
		doFinish(this);
	}
	
	protected void doFinish(Activity activity){
		doFinish(activity,OVERRIDE_DEFAULT);
	}
	
	protected void doFinish(int override){
		doFinish(this,true,override);
	}
	
	protected void doFinish(Activity activvity,int override){
		if(override == OVERRIDE_DEFAULT)
			override = getIntent().getIntExtra(INTENT_KEY_OVERRIDE, OVERRIDE_DEFAULT);
		doFinish(activvity,true,~override);
	}
	
	private void doFinish(Activity activity,boolean finish, int override){
		if(finish)
			activity.finish();
		if(override == OVERRIDE_LEFT_TO_RIGHT)
			transitionLeftToRight(activity);
		else if(override == OVERRIDE_RIGHT_TO_TLEFT)
			transitionRightToLeft(activity);
	}
	
	protected void doStartActivityForResult(Class<?> cls,int requestCode,int override){
		doStartActivityForResult(this,cls,requestCode,override);
	}
	
	protected void doStartActivityForResult(Activity activity,Class<?> cls,int requestCode,int override){
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		doStartActivityForResult(activity,intent,requestCode,override);
	}
	
	protected void doStartActivityForResult(Intent intent,int requestCode,int override){
		doStartActivityForResult(this,intent,requestCode,override);
	}
	
	protected void doStartActivityForResult(Activity activity,Intent intent,int requestCode,int override){
		intent.putExtra(INTENT_KEY_OVERRIDE, override);
		startActivityForResult(intent,requestCode);
		doFinish(activity,false,override);
	}
	
	
	protected void doSetResult(int resultCode){
		doSetResult(this,resultCode);
	}
	
	protected void doSetResult(Activity activity,int resultCode){
		doSetResult(activity,resultCode,null,OVERRIDE_DEFAULT);
	}
	
	protected void doSetResult(int resultCode,Intent data){
		doSetResult(this,resultCode,data);
	}
	
	protected void doSetResult(Activity activity,int resultCode,Intent data){
		doSetResult(this,resultCode,data,OVERRIDE_DEFAULT);
	}
	
	protected void doSetResult(int resultCode,int override){
		doSetResult(this,resultCode,override);
	}
	
	
	
	protected void doSetResult(Activity activity,int resultCode,int override){
		doSetResult(activity,resultCode,null,override);
	}
	
	protected void doSetResult(int resultCode,Intent data,int override){
		doSetResult(this,resultCode,data,override);
	}
	
	protected void doSetResult(Activity activity,int resultCode,Intent data,int override){
		if(data != null)
			setResult(resultCode, data);
		else
			setResult(resultCode);
		doFinish(activity,override);
	}
	
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			doFinish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public final static String ANIM = "anim";
	public final static String ARRAY = "array";
	public final static String ATTR = "attr";
	public final static String COLOR = "color";
	public final static String DRAWABLE = "drawable";
	public final static String ID = "id";
	public final static String LAYOUT = "layout";
	public final static String STRING = "string";
	public final static String STYLE = "style";
	public final static String XML = "xml";
	public final static String RAW = "raw";
	
	public int idXml(String name){
		return idXml(this,name);
	}
	public int idXml(Context mContext,String name){
		return idXml(mContext,name,mContext.getPackageName());
	}
	public int idXml(String name,String defPackage){
		return idXml(this,name,defPackage);
	}
	public int idXml(Context mContext,String name, String defPackage){
		return idRes(mContext,name,XML,defPackage);
	}
	
	public int idStyle(String name){
		return idStyle(this,name);
	}
	public int idStyle(Context mContext,String name){
		return idStyle(mContext,name,mContext.getPackageName());
	}
	public int idStyle(String name,String defPackage){
		return idStyle(this,name,defPackage);
	}
	public int idStyle(Context mContext,String name, String defPackage){
		return idRes(mContext,name,STYLE,defPackage);
	}
	
	public int idString(String name){
		return idString(this,name);
	}
	public int idString(Context mContext,String name){
		return idString(mContext,name,mContext.getPackageName());
	}
	public int idString(String name,String defPackage){
		return idString(this,name,defPackage);
	}
	public int idString(Context mContext,String name, String defPackage){
		return idRes(mContext,name,STRING,defPackage);
	}
	
	public int idLayout(String name){
		return idLayout(this,name);
	}
	public int idLayout(Context mContext,String name){
		return idLayout(mContext,name,mContext.getPackageName());
	}
	public int idLayout(String name,String defPackage){
		return idLayout(this,name,defPackage);
	}
	public int idLayout(Context mContext,String name, String defPackage){
		return idRes(mContext,name,LAYOUT,defPackage);
	}
	
	public int id(String name){
		return id(this,name);
	}
	public int id(Context mContext,String name){
		return id(mContext,name,mContext.getPackageName());
	}
	public int id(String name,String defPackage){
		return id(this,name,defPackage);
	}
	public int id(Context mContext,String name, String defPackage){
		return idRes(mContext,name,ID,defPackage);
	}
	
	public int idDrawable(String name){
		return idDrawable(this,name);
	}
	public int idDrawable(Context mContext,String name){
		return idDrawable(mContext,name,mContext.getPackageName());
	}
	public int idDrawable(String name,String defPackage){
		return idDrawable(this,name,defPackage);
	}
	public int idDrawable(Context mContext,String name, String defPackage){
		return idRes(mContext,name,DRAWABLE,defPackage);
	}
	
	
	
	public int idColor(String name){
		return idColor(this,name);
	}
	public int idColor(Context mContext,String name){
		return idColor(mContext,name,mContext.getPackageName());
	}
	public int idColor(String name,String defPackage){
		return idColor(this,name,defPackage);
	}
	public int idColor(Context mContext,String name, String defPackage){
		return idRes(mContext,name,COLOR,defPackage);
	}
	
	public int idAttr(String name){
		return idAttr(this,name);
	}
	public int idAttr(Context mContext,String name){
		return idAttr(mContext,name,mContext.getPackageName());
	}
	public int idAttr(String name,String defPackage){
		return idAttr(this,name,defPackage);
	}
	public int idAttr(Context mContext,String name, String defPackage){
		return idRes(mContext,name,ATTR,defPackage);
	}
	
	public int idArray(String name){
		return idArray(this,name);
	}
	public int idArray(Context mContext,String name){
		return idArray(mContext,name,mContext.getPackageName());
	}
	public int idArray(String name,String defPackage){
		return idArray(this,name,defPackage);
	}
	public int idArray(Context mContext,String name, String defPackage){
		return idRes(mContext,name,ARRAY,defPackage);
	}
	
	public int idAnim(String name){
		return idAnim(this,name);
	}
	public int idAnim(Context mContext,String name){
		return idAnim(mContext,name,mContext.getPackageName());
	}
	public int idAnim(String name,String defPackage){
		return idAnim(this,name,defPackage);
	}
	public int idAnim(Context mContext,String name, String defPackage){
		return idRes(mContext,name,ANIM,defPackage);
	}
	
	public int idRaw(String name){
		return idRaw(this,name);
	}
	public int idRaw(Context mContext,String name){
		return idRaw(mContext,name,mContext.getPackageName());
	}
	public int idRaw(String name,String defPackage){
		return idRaw(this,name,defPackage);
	}
	public int idRaw(Context mContext,String name, String defPackage){
		return idRes(mContext,name,RAW,defPackage);
	}
	
	
	public int idRes(String name, String defType){
		return idRes(this,name, defType);
	}
	public int idRes(Context mContext,String name, String defType){
		return idRes(mContext,name, defType,mContext.getPackageName());
	}
	public int idRes(String name, String defType, String defPackage){
		return idRes(this,name, defType,defPackage);
	}
	public int idRes(Context mContext,String name, String defType, String defPackage){
		int idRes = mContext.getResources().getIdentifier(name, defType,defPackage);
		return idRes;
	}

	
	/**
	 * @return Ƥ��Ӧ��Contxt
	 */
	public Context createPackageContexts(){
		return createPackageContexts("net.b");
	}
	
	/**
	 * @param packageName
	 * @return Ƥ��Ӧ��Contxt
	 */
	public Context createPackageContexts(String packageName){
		return createPackageContexts(packageName,Context.CONTEXT_IGNORE_SECURITY);
	}
	
	/**
	 * @param packageName
	 * @param flags 
	 * @return Ƥ��Ӧ��Contxt
	 */
	public Context createPackageContexts(String packageName, int flags){
		Context mContext;
		try {
			mContext = createPackageContext(packageName,flags);
		} catch (NameNotFoundException e) {
			mContext = this;
		}
		return mContext;
	}
	
//	public static void isQuitOut(final Context context) {
//		BoAlert quitAlert = new BoAlert(context);
//		quitAlert.setTitle("�˳���ʾ");
//		quitAlert.setStrBtLeftText("ȷ��");
//		quitAlert.setStrBtRightText("ȡ��");
//		quitAlert.setText("ȷ���˳�������");
//		quitAlert.showText(new BoOnAlertListener() {
//			public void setOnListenetr(int keyCode) {
//				if (keyCode == BoAlert.KEY_CODE_LEFT) {
//					// ((Activity) context).finish();
//					if (Integer.parseInt(Build.VERSION.SDK) < 8) {
//						Intent startMain = new Intent(Intent.ACTION_MAIN);
//						startMain.addCategory(Intent.CATEGORY_HOME);
//						startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						context.startActivity(startMain);
//						android.os.Process.killProcess(android.os.Process
//								.myPid());
//						System.exit(0);// �˳�����
//					} else {
//						Intent startMain = new Intent(Intent.ACTION_MAIN);
//						startMain.addCategory(Intent.CATEGORY_HOME);
//						startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						context.startActivity(startMain);
//						android.os.Process.killProcess(android.os.Process
//								.myPid());
//						System.exit(0);// �˳�����
//					}
//				}
//			}
//		});
//	}
}

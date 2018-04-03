package com.evguard.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.version.AppAsyncInstallingPackage;
import com.evguard.version.AppAsyncVersion;
import com.evguard.version.AppCheckingHandler;
import com.evguard.version.AppVersion;
import com.evguard.version.DownloadingHandler;
import com.xinghaicom.evguard.R;

public class AC_About extends AC_CheckUpdate {

	private Button btCheckVersion;
	private Button btAboutWeb;
//	private Dg_Waiting mDownDialog;
//	/* 版本更新 */
//	protected AppCheckingHandler mCheckingHandler = null;
//	protected AppVersion mNewVersion = null;
//	protected AppAsyncVersion mAsyncVersion = null;
//	protected ProgressDialog mDlgProgress = null;
//	protected DownloadingHandler mDownloadingHandler = null;
//	protected AppAsyncInstallingPackage mAsyncInstallingPack = null;
//	private App_Settings mSettings = null;
//	private final int RQ_UPDATING = 1;
	private AppTitleBar mTitleBar;
	private TextView tvVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_about);
		initialView();
		mTitleBar.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_back),
				"关于EV卫士",false,
				null,null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					@Override
					public void onLeftOperateClick() {
						AC_About.this.finish();
					}

					@Override
					public void onRightOperateClick() {

					}

					@Override
					public void onTitleClick() {
						// TODO Auto-generated method stub

					}

				});
		initListener();
	}

	private void initListener() {
		btCheckVersion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkVersion();
			}
		});

		btAboutWeb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AC_About.this, AC_About_Web.class);
				startActivity(intent);
			}
		});
	}

	protected void initialView() {
		btCheckVersion = (Button) findViewById(R.id.bt_check_version);
		btAboutWeb = (Button) findViewById(R.id.bt_about_web);
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
		tvVersion = (TextView) findViewById(R.id.tv_version);
//		tvVersion.setText("EV卫士  V" + getVersion(mContext));
	}

	@Override
	protected void onResume() {
		super.onResume();
//		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		StatService.onPause(this);
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		LogEx.i("==BACKK==", "BACKK");
//		switch (requestCode) {
//		case RQ_UPDATING:// 升级
//			if (mNewVersion.getIsMustUpdate()) {
//				finish();
//			}
//			break;
//		}
//	}
//
//	@Override
//	public void handleMsg(Message msg) {
//		// TODO Auto-generated method stub
//
//	}

//	// =============================================
//	// ****************版本升级开始*****************
//	// =============================================
//
//	private void checkVersion() {
//
//		if (mSettings == null)
//			mSettings = new App_Settings(this);
//		try {
//			mCheckingHandler = new AppCheckingHandler() {
//				
//
//				@Override
//				public void onHasUnNecessaryNew(AppVersion newVersion) {
//					if (mDownDialog != null){
//						mDownDialog.dismiss();
//						mDownDialog = null;
//					}
//					mSettings.setUpdatingMode(UpdatingMode.HAS_UNNECESSARY_NEW);
//					mNewVersion = newVersion;
//					confirmUnNecessaryUpdating();
//				}
//
//				@Override
//				public void onHasNotNew() {
//					if (mDownDialog != null){
//						mDownDialog.dismiss();
//						mDownDialog = null;
//					}
//					final Dg_Alert mDg_Alert = Dg_Alert.newInstance(
//							"提示", "当前版本已是最新版本，无需升级", "确认");
//					mDg_Alert.setCancelable(true);
//					mDg_Alert.setPositiveButton("确认", new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							mDg_Alert.dismiss();
//						}
//					});
//					mDg_Alert.show(getSupportFragmentManager(), null);
//					mSettings.setUpdatingMode(UpdatingMode.HAS_NOT_NEW);
//				}
//
//				@Override
//				public void onHasNecessaryNew(AppVersion newVersion) {
//					if (mDownDialog != null){
//						mDownDialog.dismiss();
//						mDownDialog = null;
//					}
//					mSettings.setUpdatingMode(UpdatingMode.HAS_NECESSARY_NEW);
//					mNewVersion = newVersion;
//					confirmNecessaryUpdating();
//				}
//
//				@Override
//				public void onException(Exception e) {
//					if (mDownDialog != null){
//						mDownDialog.dismiss();
//						mDownDialog = null;
//					}
//					Log.e(TAG, "检查更新失败：" + e.getMessage());
//					showToast("检测更新失败！");
//					confirmExceptionOnUpdating();
//				}
//
//				@Override
//				public void onFalied() {
//					if (mDownDialog != null){
//						mDownDialog.dismiss();
//						mDownDialog = null;
//					}
//					Log.e(TAG, "检查更新失败：");
//					showToast("检测更新失败！");
//					confirmExceptionOnUpdating();
//				}
//
//				@Override
//				public void onGetting() {
//					if (mDownDialog != null){
//						mDownDialog.dismiss();
//						mDownDialog = null;
//					}
//					if (mDownDialog == null) {
//						mDownDialog = Dg_Waiting.newInstance("检测版本", "正在检测是否有新版本，请稍候.");
//						mDownDialog.setCancelable(false);
//					}
//					mDownDialog.show(getSupportFragmentManager(), "");
//				}
//			};
//			mAsyncVersion = new AppAsyncVersion(mCheckingHandler);
//			mAsyncVersion.check();
//		} catch (Exception e) {
//			Log.e(TAG, "检查更新失败：" + e.getMessage());
//			confirmExceptionOnUpdating();
//		}
//	}
//
//	protected void confirmNecessaryUpdating() {
//
//		AlertDialog.Builder dlgNecessaryUpdating = new AlertDialog.Builder(this);
//		dlgNecessaryUpdating.setTitle("检测到新版本");
//		dlgNecessaryUpdating.setMessage(String.format(
//				"重要更新，请升级至最新版本%s，否则将无法继续使用",
//				mNewVersion != null ? mNewVersion.getNewVersion() : ""));
//		dlgNecessaryUpdating.setPositiveButton("升级",
//				new DialogInterface.OnClickListener() {
//
//					public void onClick(DialogInterface dialog, int which) {
//						try {
//							update();
//						} catch (Exception e) {
//							showToast("升级失败：" + e.getMessage());
//							confirmNecessaryUpdating();
//						}
//
//					}
//				});
//		dlgNecessaryUpdating.setNegativeButton("退出",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						finish();
//					}
//				});
//		dlgNecessaryUpdating.setOnCancelListener(new OnCancelListener() {
//			public void onCancel(DialogInterface dialog) {
//				finish();
//			}
//		});
//		dlgNecessaryUpdating.show();
//	}
//
//	protected void confirmUnNecessaryUpdating() {
//
//		if (this.isFinishing())
//			return;
//		AlertDialog.Builder dlgNecessaryUpdating = new AlertDialog.Builder(this);
//		dlgNecessaryUpdating.setTitle("检测到新版本");
//		dlgNecessaryUpdating.setMessage(String.format("版本更新，欢迎升级至最新版本%s",
//				mNewVersion != null ? mNewVersion.getNewVersion() : ""));
//		dlgNecessaryUpdating.setPositiveButton("升级",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						try {
//							update();
//						} catch (Exception e) {
//							showToast("升级失败：" + e.getMessage());
//						}
//					}
//				});
//		dlgNecessaryUpdating.setNegativeButton("以后再说", null);
//		dlgNecessaryUpdating.setOnCancelListener(null);
//		dlgNecessaryUpdating.show();
//	}
//
//	protected void confirmExceptionOnUpdating() {
//		Log.i("update...", "confirmExceptionOnUpdating");
//		if (mNewVersion != null && mNewVersion.getIsMustUpdate())
//			confirmNecessaryUpdating();
//	}
//
//	protected void update() throws Exception {
//		if (this.isFinishing())
//			return;
//		if (mDlgProgress == null) {
//			mDlgProgress = new ProgressDialog(this);
//			mDlgProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			mDlgProgress.setTitle("正在下载");
//			mDlgProgress.setMessage(String.format("正在下载%s版安装包，请稍候...",
//					mNewVersion.getNewVersion()));
//			mDlgProgress.setCancelable(true);
//			mDlgProgress.setCanceledOnTouchOutside(false);
//			mDlgProgress.setOnCancelListener(new OnCancelListener() {
//				public void onCancel(DialogInterface dialog) {
//					unUpdated();
//				}
//			});
//		}
//		mDlgProgress.show();
//		mDownloadingHandler = new DownloadingHandler() {
//			@Override
//			public void onException(Exception e) {
//				mDlgProgress.dismiss();
//				showToast("升级失败：" + e.getMessage());
//				confirmExceptionOnUpdating();
//			}
//
//			@Override
//			public void onDownloading(int percent) {
//				mDlgProgress.setProgress(percent);
//			}
//
//			@Override
//			public void onDownloaded(File installingFile) {
//				mDlgProgress.setProgress(mDlgProgress.getMax());
//				mDlgProgress.dismiss();
//				showInstalling(installingFile);
//			}
//		};
//		mAsyncInstallingPack = new AppAsyncInstallingPackage(mNewVersion,
//				mDownloadingHandler);
//		mAsyncInstallingPack.download();
//	}
//
//	protected void unUpdated() {
//		mAsyncInstallingPack.close();
//		if (mNewVersion.getIsMustUpdate())
//			finish();
//	}
//
//	protected void stopUpdatingChecking() {
//		if (mAsyncVersion != null) {
//			mAsyncVersion.close();
//		}
//	}
//
//	protected void showInstalling(File packFile) {
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(packFile),
//				"application/vnd.android.package-archive");
//		startActivityForResult(intent, RQ_UPDATING);
//	}
//
//	// =============================================
//	// ****************版本升级结束*****************
//	// =============================================
	

}

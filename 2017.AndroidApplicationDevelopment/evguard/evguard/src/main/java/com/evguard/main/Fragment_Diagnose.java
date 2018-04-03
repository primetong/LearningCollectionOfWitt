package com.evguard.main;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evguard.data.AppDataCache;
import com.evguard.main.Dg_Base.OnCancelListener;
import com.evguard.main.Thread_GetDiagnose.CallBack_GetDiagnose;
import com.evguard.model.WebRes_GetCarMedical;
import com.evguard.version.AppCheckingHandler;
import com.xinghaicom.asynchrony.LoopHandler;
import com.xinghaicom.evguard.R;

public class Fragment_Diagnose extends Fragment_Base implements
		View.OnClickListener {

	private String TAG = this.getClass().getSimpleName();

	private static final int MESSAGE_GETTING = 0;
	private static final int MESSAGE_GETTING_OK = 1;
	private static final int MESSAGE_GETTING_FAILED = 2;
	private static final int SHOWRESULT = 3;
	private Handler mHandler;
	private Dg_Waiting mDownDialog = null;
	private static Fragment_Diagnose mInstance;
	private static Object lockobj = new Object();
	private ImageButton btBeginDiagnose;
	private TextView tvDiagnose;
	private TextView tvDiagnoseText;
	private TextView tvDiagnoseScore;
	private TextView tvLastDiagnoseScore;
	private TextView tvLastDiagnoseTime;
	private TextView tvResultScore;
	private RelativeLayout rlLastDiagnose;
	private LinearLayout llDiagnoseResult;
	private boolean mIsUserCancle = false;
	Thread_GetDiagnose mThread_GetDiagnose;
	private ImageView ivWholeCar;
	private ImageView ivEleControl;
	private ImageView ivBattery;
	private ImageView ivMachine;
	private int flag = 0;
	public Animation operatingAnim;
	String wholeCarContent;
	String eleControlContent;
	String batteryContent;
	String machineContent;
	private Animation translate_animation;
	private boolean isExitDiagnose = false;
	
	boolean isDiagnoseDone = false;
	boolean isDiagnose = false;
	private TextView tvCheckItems;

//	private FrameLayout flCar;

	private ImageView ivCarTran;

	private ImageView ivCar;

	private Button btCancle;

	public static Fragment_Diagnose getInstance() {
		if (mInstance == null)
			synchronized (lockobj) {
				mInstance = new Fragment_Diagnose();
			}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layoutView != null)
			return layoutView;
		layoutView = inflater.inflate(R.layout.fagment_diagnose, container,
				false);
		findView();
		initHandler();
		initListener();
		return layoutView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onResume() {
		if(!AppDataCache.getInstance().isDiagnose()){
			if(AppDataCache.getInstance().isDiagnoseDone()){
				showResult();
			}else {
				initView();
			}
			
		} else {
			operatingAnim = AnimationUtils.loadAnimation(mContext,
					R.anim.tip);
			LinearInterpolator lin = new LinearInterpolator();
			operatingAnim.setInterpolator(lin);
			btBeginDiagnose.setImageResource(R.drawable.img_diagnose_ing);
			if (operatingAnim != null) {
				btBeginDiagnose.startAnimation(operatingAnim);
			}
			translate_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.diagnose_car);
			translate_animation.setRepeatMode(Animation.REVERSE);
			if (translate_animation != null) {
				ivCarTran.startAnimation(translate_animation);
			}
		}
		
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	
	
	private void initView() {
		initThread();
		btBeginDiagnose.setImageResource(R.drawable.img_diagnose_ready);
		tvDiagnose.setVisibility(View.VISIBLE);
		tvDiagnose.setText("点击左侧按钮进行检测");
		tvDiagnose.setTextSize(16);
		tvDiagnoseText.setVisibility(View.GONE);
		tvDiagnoseScore.setVisibility(View.GONE);
		rlLastDiagnose.setVisibility(View.VISIBLE);
		llDiagnoseResult.setVisibility(View.GONE);
		int score = AppDataCache.getInstance().getLastScore();
		if (score == 0) {
			tvLastDiagnoseScore.setText("无");
		} else {
			tvLastDiagnoseScore.setText(score + "");
		}
		String time = AppDataCache.getInstance().getLastTime();
		if (time.equals("")) {
			tvLastDiagnoseTime.setText("无历史记录");
		} else {
			tvLastDiagnoseTime.setText(time);
		}
		tvCheckItems.setVisibility(View.GONE);
		ivWholeCar.setImageResource(R.drawable.img_whole_car_n);
		ivEleControl.setImageResource(R.drawable.img_ele_control_n);
		ivBattery.setImageResource(R.drawable.img_battery_n);
		ivMachine.setImageResource(R.drawable.img_mechine_n);
		ivWholeCar.setClickable(false);
		ivEleControl.setClickable(false);
		ivBattery.setClickable(false);
		ivMachine.setClickable(false);
//		flCar.setBackgroundResource(R.drawable.img_car);
		ivCar.setImageResource(R.drawable.car1);
		ivCarTran.setVisibility(View.GONE);
		btCancle.setVisibility(View.GONE);
		if (operatingAnim != null) {
			btBeginDiagnose.clearAnimation();
		}
		if (translate_animation != null) {
			ivCarTran.clearAnimation();
		}
		btBeginDiagnose.setClickable(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mInstance = null;
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private void findView() {
		btBeginDiagnose = (ImageButton) layoutView
				.findViewById(R.id.iv_diagnose_status);
		tvDiagnose = (TextView) layoutView.findViewById(R.id.tv_diagnose);
		tvDiagnoseText = (TextView) layoutView
				.findViewById(R.id.tv_diagnose_text);
		tvDiagnoseScore = (TextView) layoutView
				.findViewById(R.id.tv_diagnose_scroe);
		tvLastDiagnoseScore = (TextView) layoutView
				.findViewById(R.id.tv_last_diagnose_score);
		tvLastDiagnoseTime = (TextView) layoutView
				.findViewById(R.id.tv_last_diagnose_time);
		tvCheckItems = (TextView) layoutView.findViewById(R.id.tv_check_items);
		tvResultScore = (TextView) layoutView
				.findViewById(R.id.tv_result_score);

		rlLastDiagnose = (RelativeLayout) layoutView
				.findViewById(R.id.rl_last_diagnose);
		llDiagnoseResult = (LinearLayout) layoutView
				.findViewById(R.id.ll_result);
		ivWholeCar = (ImageView) layoutView.findViewById(R.id.iv_whole_car);
		ivEleControl = (ImageView) layoutView.findViewById(R.id.iv_ele_control);
		ivBattery = (ImageView) layoutView.findViewById(R.id.iv_battery);
		ivMachine = (ImageView) layoutView.findViewById(R.id.iv_machine);
		ivCarTran = (ImageView) layoutView.findViewById(R.id.iv_car_tran);
//		flCar = (FrameLayout) layoutView.findViewById(R.id.fl_car);
		ivCar = (ImageView) layoutView.findViewById(R.id.car1);
		btCancle = (Button) layoutView.findViewById(R.id.bt_cancle);
		if (operatingAnim != null) {
			btBeginDiagnose.clearAnimation();
		}
		if (translate_animation != null) {
			ivCarTran.clearAnimation();
		}
	}

	
	
	private void initListener() {
		btBeginDiagnose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mThread_GetDiagnose.getWholeCar();
				btBeginDiagnose.setClickable(false);
				AppDataCache.getInstance().setScore(0);
				AppDataCache.getInstance().setCheckItemNum(0);
				llDiagnoseResult.setVisibility(View.GONE);
				rlLastDiagnose.setVisibility(View.GONE);
				tvDiagnose.setVisibility(View.GONE);
				tvCheckItems.setVisibility(View.GONE);
				ivWholeCar.setImageResource(R.drawable.img_whole_car_n);
				ivEleControl.setImageResource(R.drawable.img_ele_control_n);
				ivBattery.setImageResource(R.drawable.img_battery_n);
				ivMachine.setImageResource(R.drawable.img_mechine_n);
				ivWholeCar.setClickable(false);
				ivEleControl.setClickable(false);
				ivBattery.setClickable(false);
				ivMachine.setClickable(false);
				tvDiagnoseText.setVisibility(View.VISIBLE);
				tvDiagnoseScore.setVisibility(View.VISIBLE);
				operatingAnim = AnimationUtils.loadAnimation(mContext,
						R.anim.tip);
				LinearInterpolator lin = new LinearInterpolator();
				operatingAnim.setInterpolator(lin);
				btBeginDiagnose.setImageResource(R.drawable.img_diagnose_ing);
				if (operatingAnim != null) {
					btBeginDiagnose.startAnimation(operatingAnim);
				}
				tvDiagnoseScore.setText("0");
				tvDiagnoseText.setText("正在扫描整车系统");
				ivCarTran.setVisibility(View.VISIBLE);
				translate_animation = AnimationUtils.loadAnimation(mContext,
						R.anim.diagnose_car);
				translate_animation.setRepeatMode(Animation.REVERSE);
				if (translate_animation != null) {
					ivCarTran.startAnimation(translate_animation);
				}
//				flCar.setBackgroundResource(R.drawable.img_car_ok);
				ivCar.setImageResource(R.drawable.img_car_ok);
				btCancle.setVisibility(View.VISIBLE);
			}

		});
		
		ivWholeCar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String charSequence = Html.fromHtml(wholeCarContent).toString();  
				final Dg_Alert mDg_Alert = Dg_Alert.newInstance(
						"整车检测结果", charSequence, "确认");
				mDg_Alert.setCancelable(true);
				mDg_Alert.setPositiveButton("确认", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mDg_Alert.dismiss();
					}
				});
				
				mDg_Alert.show(getChildFragmentManager(),"");
			}
		});
		ivEleControl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String charSequence = Html.fromHtml(eleControlContent).toString(); 
				final Dg_Alert mDg_Alert = Dg_Alert.newInstance(
						"电控检测结果", charSequence, "确认");
				mDg_Alert.setCancelable(true);
				mDg_Alert.setPositiveButton("确认", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mDg_Alert.dismiss();
					}
				});
				
				mDg_Alert.show(getChildFragmentManager(),"");
			}
		});
		ivBattery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String charSequence = Html.fromHtml(batteryContent).toString(); 
				final Dg_Alert mDg_Alert = Dg_Alert.newInstance(
						"电源检测结果", charSequence, "确认");
				mDg_Alert.setCancelable(true);
				mDg_Alert.setPositiveButton("确认", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mDg_Alert.dismiss();
					}
				});
				
				mDg_Alert.show(getFragmentManager(),"");
			}
		});
		ivMachine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String charSequence = Html.fromHtml(machineContent).toString(); 
				final Dg_Alert mDg_Alert = Dg_Alert.newInstance(
						"电机检测结果", charSequence, "确认");
				mDg_Alert.setCancelable(true);
				mDg_Alert.setPositiveButton("确认", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mDg_Alert.dismiss();
					}
				});
				
				mDg_Alert.show(getFragmentManager(),"");
			}
		});
		btCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isDiagnose){
					final Dg_OptionSelect mDg_OptionSelect = Dg_OptionSelect.newInstance("退出诊断", "是否要退出当前车辆诊断", "确定", "取消");
					mDg_OptionSelect.setPositiveButton("确定", new OnClickListener() {
						

						@Override
						public void onClick(View v) {
							
							if (mThread_GetDiagnose != null) {
								mThread_GetDiagnose.interrupt();
								mThread_GetDiagnose = null;
							}
							mDg_OptionSelect.dismiss();
							isDiagnose = false;
							AppDataCache.getInstance().setDiagnose(isDiagnose);
							isExitDiagnose = true;
							AppDataCache.getInstance().setDiagnoseDone(false);
							initView();
						}
					});
					mDg_OptionSelect.setCancleButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mDg_OptionSelect.dismiss();
							isExitDiagnose = false;
							isDiagnose = true;
							AppDataCache.getInstance().setDiagnose(isDiagnose);
							AppDataCache.getInstance().setDiagnoseDone(true);
						}
					});
					mDg_OptionSelect.show(getChildFragmentManager(), null);
					}
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}
	}


	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MESSAGE_GETTING:
					isDiagnose = true;
					AppDataCache.getInstance().setDiagnose(isDiagnose);
					AppDataCache.getInstance().setDiagnoseDone(false);
					break;
				case MESSAGE_GETTING_OK:
					if (mIsUserCancle)
						break;
					if (mDownDialog != null) {
						mDownDialog.dismiss();
					}
					setData(msg.getData());
					break;
				case MESSAGE_GETTING_FAILED:
					if (mDownDialog != null)
						mDownDialog.dismiss();
					String errorMsg = "已退出车辆检测！";
					if(msg.obj != null) 
						errorMsg += "出错信息：" + msg.obj;
					showToast(errorMsg);
					AppDataCache.getInstance().setDiagnose(false);
					AppDataCache.getInstance().setDiagnoseDone(false);
					initView();
					break;
				case SHOWRESULT:
					showResult();
					
					break;
				}
				
				
			}

		};
	}

	private void setData(Bundle data) {
		switch (flag) {
		case 1:
			if (data.getString("Result").equals("0")) {
				ivWholeCar.setImageResource(R.drawable.img_whole_car_ok);
			}
			if (data.getString("Result").equals("1")) {
				ivWholeCar.setImageResource(R.drawable.img_whole_car_error);
			}
			wholeCarContent = data.getString("AnalysisResult");
			tvDiagnoseScore.setText(AppDataCache.getInstance().getScore() + "");
			tvDiagnoseText.setText("正在扫描电控系统");
			break;
		case 2:
			if (data.getString("Result").equals("0")) {
				ivEleControl.setImageResource(R.drawable.img_ele_control_ok);
			}
			if (data.getString("Result").equals("1")) {
				ivEleControl.setImageResource(R.drawable.img_ele_control_error);
			}
			eleControlContent = data.getString("AnalysisResult");
			tvDiagnoseScore.setText(AppDataCache.getInstance().getScore() + "");
			tvDiagnoseText.setText("正在扫描电池系统");
			break;
		case 3:
			if (data.getString("Result").equals("0")) {
				ivBattery.setImageResource(R.drawable.img_battery_ok);
			}
			if (data.getString("Result").equals("1")) {
				ivBattery.setImageResource(R.drawable.img_battery_error);
			}
			batteryContent = data.getString("AnalysisResult");
			tvDiagnoseScore.setText(AppDataCache.getInstance().getScore() + "");
			tvDiagnoseText.setText("正在扫描电机系统");
			break;
		case 4:
			if (data.getString("Result").equals("0")) {
				ivMachine.setImageResource(R.drawable.img_mechine_ok);
			}
			if (data.getString("Result").equals("1")) {
				ivMachine.setImageResource(R.drawable.img_mechine_error);
			}
			machineContent = data.getString("AnalysisResult");
			tvDiagnoseScore.setText(AppDataCache.getInstance().getScore() + "");
			Timer timer = new Timer(true);
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					Message endMsg = mHandler
							.obtainMessage(SHOWRESULT);
					mHandler.sendMessage(endMsg);
				}
			}, 1*1000);
			
			break;
		default:
			break;
		}

	}
	
	

	private void initThread() {
		if(mThread_GetDiagnose == null){
		mThread_GetDiagnose = new Thread_GetDiagnose(mContext,
				new LoopHandler() {
				}, new CallBack_GetDiagnose() {

					@Override
					public void getDiagnoseOK(
							WebRes_GetCarMedical aWebRes_GetCarMedical,
							int aFlag) {
						Message endMsg = mHandler
								.obtainMessage(MESSAGE_GETTING_OK);

						AppDataCache.getInstance().setScore(
								AppDataCache.getInstance().getScore()
										+ Integer
												.parseInt(aWebRes_GetCarMedical
														.getScore()));
						AppDataCache.getInstance().setCheckItemNum(
								AppDataCache.getInstance().getCheckItemNum()
										+ Integer
												.parseInt(aWebRes_GetCarMedical
														.getCheckItemNum()));
						AppDataCache.getInstance().setTime(
								aWebRes_GetCarMedical.getTime());
						flag = aFlag;
						Bundle bundle = new Bundle();
						bundle.putString("AnalysisResult",
								aWebRes_GetCarMedical.getAnalysisResult());
						bundle.putString("Result",
								aWebRes_GetCarMedical.getDiagnoseResult());
						endMsg.setData(bundle);
						mHandler.sendMessage(endMsg);
					}

					@Override
					public void getDiagnoseFailed(String sError) {
						Message endMsg = mHandler
								.obtainMessage(MESSAGE_GETTING_FAILED);
						mHandler.sendMessage(endMsg);
					}

					@Override
					public void getDiagnoseing() {
						Message endMsg = mHandler
								.obtainMessage(MESSAGE_GETTING);
						mHandler.sendMessage(endMsg);
					}
				});
		
		mThread_GetDiagnose.start();
		}
	}
	
	private void showResult() {
		if (operatingAnim != null) {
			btBeginDiagnose.clearAnimation();
		}
		btBeginDiagnose.setImageResource(R.drawable.img_diagnose_ready);
		tvDiagnoseScore.setVisibility(View.GONE);
		tvDiagnoseText.setVisibility(View.GONE);
		tvCheckItems.setVisibility(View.VISIBLE);
		AppDataCache.getInstance().setLastCheckItemNum(AppDataCache.getInstance().getCheckItemNum());
		AppDataCache.getInstance().setLastScore(AppDataCache.getInstance().getScore());
		AppDataCache.getInstance().setLastTime(AppDataCache.getInstance().getTime());
		tvCheckItems
				.setText("共检测"
						+ AppDataCache.getInstance().getCheckItemNum()
						+ "项，没有发现故障");
		tvDiagnose.setVisibility(View.VISIBLE);
		tvDiagnose.setText("点击左侧按钮再次进行诊断");
		tvDiagnose.setTextSize(12);
		llDiagnoseResult.setVisibility(View.VISIBLE);
		tvResultScore.setText(AppDataCache.getInstance().getScore() + "");
		rlLastDiagnose.setVisibility(View.GONE);
//		flCar.setBackgroundResource(R.drawable.img_car_ok);
		ivCar.setImageResource(R.drawable.img_car_ok);
		ivWholeCar.setClickable(true);
		ivEleControl.setClickable(true);
		ivBattery.setClickable(true);
		ivMachine.setClickable(true);
		if (translate_animation != null) {
			ivCarTran.clearAnimation();
		}
		
		ivCarTran.setVisibility(View.GONE);
		btBeginDiagnose.setClickable(true);
		btCancle.setVisibility(View.GONE);
		isDiagnose = false;
		AppDataCache.getInstance().setDiagnose(isDiagnose);
		AppDataCache.getInstance().setDiagnoseDone(true);
	}

	@Override
	protected void restoreState(Bundle saveState) {
		
	}

	@Override
	protected Bundle saveState(Bundle saveState) {
		return saveState;
	}
	
	
}

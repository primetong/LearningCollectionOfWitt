package com.evguard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.audrey.mode.TableShowViewData;
import com.audrey.view.ArcBar;
import com.audrey.view.CircleBar;
import com.audrey.view.PorterDuffXfermodeView;
import com.audrey.view.TableShowView;
import com.xinghaicom.evguard.R;

public class MainActivity extends Activity {

	private ArcBar mArcBar=null;
	private CircleBar mCircleBar1=null;
	private CircleBar mCircleBar2=null;
	int iArcBar=0;
	int iCircleBar=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doArcBar();
        doCircleBar();
        dowave();
        doTable();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void doArcBar(){
    	mArcBar=(ArcBar) findViewById(R.id.cbar);
    	Timer t=new Timer();
        
        t.schedule(new TimerTask(){

			@Override
			public void run() {
				if(iArcBar>100){
					iArcBar=0;
				}
				mArcBar.setBarValue(iArcBar);
				iArcBar+=11;
			}
        	
        },1000,1000);
    }
    private void doCircleBar(){
    	mCircleBar1=(CircleBar) findViewById(R.id.circlebar1);
    	Timer t=new Timer();
        
        t.schedule(new TimerTask(){

			@Override
			public void run() {
				if(iCircleBar>100){
					iCircleBar=0;
				}
				mCircleBar1.setBarValue(iCircleBar);
				iCircleBar+=5;
			}
        	
        },1000,1000);
        mCircleBar2=(CircleBar) findViewById(R.id.circlebar2);
    	Timer t2=new Timer();
        
        t2.schedule(new TimerTask(){

			@Override
			public void run() {
				if(iCircleBar>100){
					iCircleBar=0;
				}
				mCircleBar2.setBarValue(iCircleBar);
				iCircleBar+=5;
			}
        	
        },1000,1000);
    }
    int icount=0;
    private void dowave(){
    	final PorterDuffXfermodeView mWaterWaveView=(PorterDuffXfermodeView) findViewById(R.id.wavebar);
//    	mWaterWaveView.startWave();
//    	final Wave_SinBar aWave_SinBar=(Wave_SinBar) findViewById(R.id.wavebar1);
    	Timer t2=new Timer();
        
        t2.schedule(new TimerTask(){
			@Override
			public void run() {
				if(icount>100){
					icount=0;
				}
				mWaterWaveView.setBarValue(icount);
				icount+=1;
			}
        	
        },1000,1000);
    	
    }
    private void doTable(){

    	final TableShowView aTableShowview=(TableShowView) findViewById(R.id.tableview);
//    	aTableShowview.setIncrease(7, 50);//���ò���
//    	aTableShowview.setYCheckValue(50);
//    	aTableShowview.setData(getVirData(31));
//    	
    	Button btn_month=(Button)findViewById(R.id.btn_year);
    	Button btn_week=(Button)findViewById(R.id.btn_week);
    	Button btn_day=(Button)findViewById(R.id.btn_day);
    	
    	btn_month.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				aTableShowview.setIncrease(30*24*60*60, 50);//���ò���
		    	aTableShowview.setYCheckValue(200);
		    	aTableShowview.setData(getVirData(100));
			}
    		
    	});
    	btn_week.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				aTableShowview.setIncrease(7*24*60*60, 50);//���ò���
		    	aTableShowview.setYCheckValue(100);
		    	aTableShowview.setData(getVirData(50));
			}
    		
    	});
    	btn_day.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				aTableShowview.setIncrease(24*60*60, 50);//���ò���
		    	aTableShowview.setYCheckValue(150);
		    	aTableShowview.setData(getVirData(24));
			}
    	});
    }
    
	private List<TableShowViewData> getVirData(int count){
		List<TableShowViewData> alist=new ArrayList<TableShowViewData>();
		for(int i=0;i<=count;i++){
			Random r=new Random();
			float y=r.nextInt(500);
			long time=System.currentTimeMillis()+1000*10*i;
			
			Log.i("122", (new Date(time)).toString());
			TableShowViewData p=new TableShowViewData(time,y);
			alist.add(p);
			Log.i("121", "***x:"+time+"::y:"+y);
		}
		return alist;
	}
}

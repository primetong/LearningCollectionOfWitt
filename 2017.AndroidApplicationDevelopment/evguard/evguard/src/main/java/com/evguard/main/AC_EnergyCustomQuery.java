package com.evguard.main;

import com.xinghaicom.evguard.R;

import android.os.Bundle;
import android.os.Message;

public class AC_EnergyCustomQuery extends AC_BaseLogined {

	@Override
	public void onCreate(Bundle savedInstancestate) {
		try {
			super.onCreate(savedInstancestate);
			setContentView(R.layout.ac_energycustomquery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

}

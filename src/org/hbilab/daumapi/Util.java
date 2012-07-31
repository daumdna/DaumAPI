package org.hbilab.daumapi;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class Util {
	//토스트 메세지 보여주기
	public static void toastMessage(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	//키보드 숨기기
	public static void hideKeyboard(Context context , EditText editText){
		InputMethodManager iManager = 
    			(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
    	iManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	//SensorManager 객체생성과 등록하기
	public static SensorManager getSensor(Context context, SensorEventListener listener){
		SensorManager sensorM = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorM.registerListener(listener, sensorM.getDefaultSensor(Sensor.TYPE_ORIENTATION),
        		SensorManager.SENSOR_DELAY_GAME);
        return sensorM;
	}
	//Screen 폭가져오기
	public static int getScreenWidth(Context context){
		WindowManager managerW = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = managerW.getDefaultDisplay();
		return display.getWidth();
	}
	//Screen 높이가져오기
	public static int getScreenHeight(Context context){
		WindowManager managerW = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = managerW.getDefaultDisplay();
		return display.getHeight();
	}
	//콤마 찍어주는 메서드
	public static String getComma(int str){
		return String.format("%,d", str);
	}
	
}







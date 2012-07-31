package org.hbilab.daumapi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import android.os.Handler;
import android.os.Message;
/*
 * 서버에서 출력하는 JSON 데이터 받아오기
 * */
public class JSONManager extends Thread{
	//메인 스레드 핸들러
	private Handler handler;
	//전송할 url 주소
	private String urlAddr;
	//전송할 데이터가 담겨있는 HashMap 객체
	private HashMap<String, String> map;
	
	public static final int DOWN_SUCCESS = 3;
	public static final int DOWN_FAIL = 999;
	public JSONManager(Handler handler, String urlAddr, HashMap<String ,String> map){
		this.handler = handler;
		this.urlAddr = urlAddr;
		this.map = map;
	}
	public void run(){
		//서버에 출력할 JSON 데이터를 저장하기 위한 객체
		StringBuilder builder = new StringBuilder();
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlAddr);
			conn = (HttpURLConnection)url.openConnection();
			if(conn != null){//연결이 성공되었다면
				conn.setConnectTimeout(10000);//대기시간을 10초동안 설정
				conn.setUseCaches(false);//캐시하지 않음
				if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){//서버페이지 존재하는 경우
					//JSON 데이터를 Input 스트림으로 읽어온다.
					InputStreamReader isr = new InputStreamReader(conn.getInputStream());
					BufferedReader br = new BufferedReader(isr,1024);
					String readLine = null;
					while((readLine = br.readLine()) != null){//한줄씩 읽어서
						//StringBuilder에 저장한다.
						builder.append(readLine);
					}
					br.close();
					//메인 스레드의 핸들러 전송 성공이라고 알린다.
					Message msg = new Message();
					msg.what = DOWN_SUCCESS;//성공시 3을 보내기로 하자.
					msg.obj = builder.toString();//JSON 데이터를 Message의 obj에 저장한다.
					handler.sendMessage(msg);//핸들러에 메세지 보내기
				}
				
			}
		} catch (Exception e) {
			//접속이 실패한 경우 999라는 메세지를 핸들러에게 보낸다.
			handler.sendEmptyMessage(DOWN_FAIL);
		} finally {
			conn.disconnect();
		}
	}
	
}

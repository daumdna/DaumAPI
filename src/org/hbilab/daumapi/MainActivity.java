package org.hbilab.daumapi;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	private EditText editTextSearch;
	private JSONManager jManager;
	private ListView listView;
	private ArrayList<BookItem> list;
	private DaumAdapter adapter;
	
	int firstVisibleItem;
	int visibleItemCount;
	int totalItemCount;
	int page = 1;
	int total = 0;
	
	private ProgressDialog loagindDialog; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);
        editTextSearch = (EditText)findViewById(R.id.editTextSearch);
        
        
        list = new ArrayList<BookItem>();
        adapter = new DaumAdapter(this,R.layout.daum,list);
        listView.setAdapter(adapter);
        
        listView.setDivider(new ColorDrawable(Color.GRAY));
        listView.setDividerHeight(2);
        listView.setOnScrollListener(new OnScrollListener() {
			//scrollState => 0 : 정지상태 , 1:터치 상태 2 : 터치 후에 이동되는 상태 
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL &&
					(firstVisibleItem+visibleItemCount)==totalItemCount){
					update(++page);
				}
			}
			//firstVisibleItem : ListView에 보이는 상단셀의 인덱스
			//visibleItemCount : ListView에 보이는 셀의 수
			//totalItemCount :  ListView에 있는 총 셀의 수
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				MainActivity.this.firstVisibleItem = firstVisibleItem;
				MainActivity.this.visibleItemCount = visibleItemCount;
				MainActivity.this.totalItemCount = totalItemCount;
			}
		});
    }

    public void search(View v){
    	loagindDialog = ProgressDialog.show(this, "","로딩 중입니다. 잠시 기다려주세요", true);
    	list.clear();
    	
    	String keyWord = editTextSearch.getText().toString();
    	if(keyWord== null || keyWord.length()==0) return;
    	String encodedK = "";
    	
    	try {
			encodedK = URLEncoder.encode(keyWord,"utf-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
    	//http://apis.daum.net/search/book?
    	//apikey=DAUM_SEARCH_DEMO_APIKEY&output=json&q=daum%20openapi
    	StringBuilder  builder = new StringBuilder();
    	builder.append("http://apis.daum.net/search/book?");
    	builder.append("apikey=f146dfc358007a8b3531a652ec7baded66d89e1a&output=json&");
    	builder.append("q="+encodedK);
    	
    	jManager = new JSONManager(handler,builder.toString(),null);
    	jManager.start();
    }
    public void update(int page){
    	//show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable)
    	
    	String keyWord = editTextSearch.getText().toString();
    	if(keyWord== null || keyWord.length()==0) return;
    	String encodedK = "";
    	
    	try {
			encodedK = URLEncoder.encode(keyWord,"utf-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
    	int totalPage = (total%10==0)?(total/10):(total/10+1);
    	if(page>totalPage){
    		Util.toastMessage(this, "마지막 페이지 입니다.");
    	}else{
    	
	    	StringBuilder  builder = new StringBuilder();
	    	builder.append("http://apis.daum.net/search/book?");
	    	builder.append("apikey=f146dfc358007a8b3531a652ec7baded66d89e1a&output=json&");
	    	builder.append("q="+encodedK+"&pageno="+page);
	    	
	    	jManager = new JSONManager(handler,builder.toString(),null);
	    	jManager.start();
	    	Util.toastMessage(this, "10개리스트가 추가되었습니다.");
    	}
    }
    Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch(msg.what){
    		case JSONManager.DOWN_SUCCESS : 
    			String jsonData = (String)msg.obj;
    			parseForJson(jsonData);
    		}
    	}
    };
    
    private void parseForJson(String jsonData){
    	final String JSONDATA = jsonData;
    	
	    	try {
				JSONObject jsonObj = new JSONObject(JSONDATA);
				JSONObject channel = jsonObj.getJSONObject("channel");
				
				total = Integer.parseInt(channel.getString("totalCount"));
				
				JSONArray items = channel.getJSONArray("item");
				for(int i=0 ; i<items.length() ; i++){
					JSONObject temp = items.getJSONObject(i);
					String title = temp.getString("title");
					String link = temp.getString("link");
					String image = temp.getString("cover_s_url");
					String price = temp.getString("sale_price");
					list.add(new BookItem(title,link,image,
	    			Util.getComma(Integer.parseInt(price))));
	                    
					
				}
				
				adapter.notifyDataSetChanged();
				 loagindDialog.dismiss(); // 다이얼로그 삭제
				Util.hideKeyboard(MainActivity.this, editTextSearch);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    }
}









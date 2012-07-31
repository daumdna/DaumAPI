package org.hbilab.daumapi;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class DaumAdapter extends BaseAdapter implements OnItemClickListener{
	private Context context;
	private int layoutRes;
	private ArrayList<BookItem> list;
	private LayoutInflater inflater;
	public DaumAdapter(Context context, int layoutRes, ArrayList<BookItem> list){
		this.context = context;
		this.layoutRes = layoutRes;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(layoutRes, parent,false);
		}
		final int INDEX = position;
		ImageView image = (ImageView)convertView.findViewById(R.id.image);
		String itemImage = list.get(position).getImage();
		if(itemImage != null && itemImage.length()>0){
			DoImageLoad task = new DoImageLoad(image);
			task.execute(itemImage); 
		}else{
			image.setImageResource(R.drawable.ic_launcher);
		}
		image.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse(list.get(INDEX).getLink());
		    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		    	context.startActivity(intent);
			}
		});
		
		TextView title = (TextView)convertView.findViewById(R.id.title);
		title.setText(Html.fromHtml(Html.fromHtml(list.get(position).getTitle()).toString()));
		
		title.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Uri uri = Uri.parse(list.get(INDEX).getLink());
		    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		    	context.startActivity(intent);
				
			}
		});
		
		TextView price = (TextView)convertView.findViewById(R.id.price);
		price.setText(list.get(position).getPrice());
				
		return convertView;
	}
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Log.e("onItemClick",list.get(position).getLink());
	}
	
	 private class DoImageLoad extends AsyncTask<String, Void, Bitmap> {  
			ImageView imageView;
			String image_url;
			public DoImageLoad(ImageView imageView) {  
			        this.imageView = imageView;  
		    }  
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
			@Override
	    	protected Bitmap doInBackground(String... strData) {
				image_url = strData[0];
				Bitmap image = ImageCache.getImage(image_url);  

		        if (image == null) {  
		        	image = ImageDownLoad(image_url);  
		            ImageCache.setImage(image_url, image);  
		        }  
		        return image;  
	    	}
			
			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
			}
			
			@Override
	    	protected void onPostExecute(Bitmap bm) {
	    		imageView.setImageBitmap(bm);
	    	}

	    	@Override
			protected void onCancelled() {
				super.onCancelled();
			}    	

		}
		public Bitmap ImageDownLoad(String image_url){
			try {
				URL url = new URL(image_url); 
				URLConnection conn = url.openConnection(); 
				conn.connect(); 
				BufferedInputStream  bis = new BufferedInputStream(conn.getInputStream());
				Bitmap bm = BitmapFactory.decodeStream(bis); bis.close();
				return bm;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

}









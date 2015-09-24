package com.cardcamera;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = null;
	protected static final int ITEM1 = Menu.FIRST;
	protected static final int ITEM2 = Menu.FIRST+1;
	protected static final int ITEM3 = Menu.FIRST+2;
	int longClick_position;
	ListView item_list;
	ArrayList<String> data = new ArrayList<String>();
	ArrayList<String> content = new ArrayList<String>();
	ArrayList<Bitmap> image = new ArrayList<Bitmap>();
	ArrayList<String> type = new ArrayList<String>();
	MyAdapter adapter;
	private SharedPreferences settings;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			      
	        item_list = (ListView)findViewById(R.id.item_list);	      
	        adapter =new MyAdapter(this);
	        item_list.setAdapter(adapter);

	        processControllers();
	        // 加入範例資料
	        data.add("Example");
	        type.add("Choose a type");
	        content.add("Leave some comment here!!");
	        Resources res=getResources();
	        
	        Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.ic_launcher); 
	        image.add(0, bmp);
	        	        
	    
	 }
	
	
	
	private void deleteItem(){
		
		int size = data.size();
		if( size > 0 ){
			data.remove(longClick_position);
			//image.remove(longClick_position);
			adapter.notifyDataSetChanged();
		}
	}
	
	 
	    @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	      	
	    	// 如果被啟動的Activity元件傳回確定的結果
	      if (resultCode == Activity.RESULT_OK) {
	            // 讀取標題
	          String titleText = data.getStringExtra("titleText");
	          String contentText = data.getStringExtra("contentText");
	          byte[] theImage = data.getByteArrayExtra("photo"); 
	          String choose_type = data.getStringExtra("getData");
	          Bitmap bitmap = BitmapFactory.decodeByteArray(theImage, 0, theImage.length);
				
	         
	            // 加入標題項目
	          if (requestCode == 0) {
	                // 加入標題項目
	              this.data.add(titleText);
	              this.content.add(contentText);
	              this.image.add(bitmap);
	              this.type.add(choose_type);
	                // 通知資料已經改變，ListView元件才會重新顯示
	              adapter.notifyDataSetChanged();
	           }
	            // 如果是修改記事
	           else if (requestCode == 1) {
	                // 讀取記事編號
	               int position = data.getIntExtra("position", -1);
	               
	               if (position != -1) {
	                    // 設定標題項目
	            	   this.data.set(position, titleText);
	            	   this.content.set(position, contentText);
	            	   this.image.set(position, bitmap);
	            	   this.type.set(position,choose_type);
	                    // 通知資料已經改變，ListView元件才會重新顯示
	            	   adapter.notifyDataSetChanged();
	               }
	            }
	        }
	    }
	    
	 
	   
	   private void processControllers() {
	        OnItemClickListener itemListener = new OnItemClickListener() {
	 
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, 
	                    int position, long id) {
	                Intent intent =new Intent("com.cardcamera.EDIT_ITEM");
	                intent.putExtra("position", position);
	                intent.putExtra("titleText",data.get(position));
	                intent.putExtra("contentText",content.get(position));
	                intent.putExtra("choose_type",type.get(position));
	                
	                ByteArrayOutputStream baos = new ByteArrayOutputStream();
		            image.get(position).compress(Bitmap.CompressFormat.JPEG, 100, baos);		               
					byte[] photodata2 = baos.toByteArray();
		            intent.putExtra("theImage",photodata2);
	                
	                
	                startActivityForResult(intent,1);
	            }
	        };
	 
	        item_list.setOnItemClickListener(itemListener);
	 
	        OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {
	 
	            @Override
	            public boolean onItemLongClick(AdapterView<?> parent, View view, 
	                    int position, long id) {
	            	longClick_position = position;
	            	item_list.showContextMenu();
	            	
	                return true;
	            }
	        };
	        item_list.setOnItemLongClickListener(itemLongListener);
	        
	        OnCreateContextMenuListener context =new OnCreateContextMenuListener() {
				
				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					
					menu.setHeaderTitle("選項");
					menu.add(Menu.NONE,ITEM1,Menu.NONE, "刪除");
					menu.add(Menu.NONE,ITEM2,Menu.NONE, "編輯");
					menu.add(Menu.NONE,ITEM3,Menu.NONE, "取消");
				}
			};
			item_list.setOnCreateContextMenuListener(context);
			
		  	   
	   }  
	   
	    @Override
	public boolean onContextItemSelected(MenuItem item) {
	    	
	    	switch(item.getItemId()){
	    	
	    	case ITEM1:
	    		deleteItem();
	    		Toast.makeText(this, "已刪除", Toast.LENGTH_SHORT).show();
	    		break;
	    	case ITEM2:
	    		Intent intent =new Intent("com.cardcamera.EDIT_ITEM");
	    		intent.putExtra("position", longClick_position);
	            intent.putExtra("titleText",data.get(longClick_position));
	            intent.putExtra("contentText",content.get(longClick_position));
	            intent.putExtra("choose_type",type.get(longClick_position));
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            image.get(longClick_position).compress(Bitmap.CompressFormat.JPEG, 100, baos);		               
				byte[] photodata2 = baos.toByteArray();
	            intent.putExtra("theImage",photodata2);
	    		startActivityForResult(intent,1);
	    		break;
	    	case ITEM3:	
	    		break;
	    	
	
	    	
	    	}
		return super.onContextItemSelected(item);
	}


		public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater menuInflater = getMenuInflater();
	        menuInflater.inflate(R.menu.main, menu);
	        return true;
	    }
	 
	    // 使用者選擇所有的選單項目都會呼叫這個方法
	    public void clickMenuItem(MenuItem item) {
	        // 使用參數取得使用者選擇的選單項目元件編號
	        int itemId = item.getItemId();
	   

	        switch (itemId) {
	    
	        // 使用者選擇新增選單項目    
	        case R.id.add_item:
	            // 建立啟動另一個Activity元件需要的Intent物件
	            Intent intent = new Intent("com.cardcamera.ADD_ITEM");
	            startActivityForResult(intent, 0);
	            break;
	        case R.id.action_settings:
	        	Intent intent2 = new Intent(this, AboutActivity.class);
	        	startActivity(intent2);
	            break;
	        case R.id.en:
	        	 if(Locale.getDefault().getDisplayLanguage() == "en"){ 
	        		 Toast.makeText(this, "Is already in English", Toast.LENGTH_LONG).show(); 
	             
	        	 }
	        	 else{
	        		Locale locale = new Locale("en"); 
	        		Locale.setDefault(locale);
	        		Configuration config = new Configuration();
	        		config.locale = locale;
	        		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	        		Toast.makeText(this, "Locale in English", Toast.LENGTH_LONG).show();
	        	 }
	             break;    
	        case R.id.ch:
	        	 if(Locale.getDefault().getDisplayLanguage() == "zh_TW"){
	        		 Toast.makeText(this, "已在中文介面", Toast.LENGTH_LONG).show();
	        	 }
	        	 else{
	             Locale locale_ch = new Locale("ch"); 
	             Locale.setDefault(locale_ch);
	             Configuration config_ch = new Configuration();
	             config_ch.locale = locale_ch;
	             getBaseContext().getResources().updateConfiguration(config_ch, getBaseContext().getResources().getDisplayMetrics());
	             Toast.makeText(this, "語言轉換:中文", Toast.LENGTH_LONG).show();
	        	 }
	             break;     
	      
	      
	        }
			
	 
	    }
	 
	    
	    public class MyAdapter extends BaseAdapter{
	    	private LayoutInflater myInflater;
	    	public MyAdapter(Context c) {
				myInflater =LayoutInflater.from(c);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return data.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return data.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				convertView = myInflater.inflate(R.layout.listview, null);				
				ImageView imgLogo =(ImageView) convertView.findViewById(R.id.imageView1);
				TextView txtname =(TextView) convertView.findViewById(R.id.textView1);
				TextView txtcontent =(TextView) convertView.findViewById(R.id.content);
				TextView txttype =(TextView) convertView.findViewById(R.id.textView2);
			    imgLogo.setImageBitmap(image.get(position));
				//imgLogo.setImageResource(R.drawable.orange);
				txtname.setText(data.get(position));
				txtcontent.setText(content.get(position));
				txttype.setText(type.get(position));
				return convertView;
			}}
	    
	    
	   
	}


package com.cardcamera;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends Activity {
	SharedPreferences shared;	
	private static final String TAG = null;
	private EditText title_text ,content_text;
	private ImageView iv;
	private TextView tv;
	private File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());	    
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private boolean tell =false;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        title_text = (EditText) findViewById(R.id.title_text);
        content_text = (EditText) findViewById(R.id.content_text);
        this.getApplicationContext();
        spinner = (Spinner)findViewById(R.id.mySpinner);
        tv =(TextView) findViewById(R.id.tv);
        iv=(ImageView) findViewById(R.id.imageView1);
        ImageButton pict =(ImageButton) findViewById(R.id.take_picture);
        ImageButton album =(ImageButton) findViewById(R.id.album);
		pict.setOnClickListener(event);
		album.setOnClickListener(event2);
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		shared = getPreferences(MODE_WORLD_READABLE);
		// 取得Intent物件
        Intent intent = getIntent();
        // 讀取Action名稱
        String action = intent.getAction();
        
        
       // 如果是修改記事
       if (action.equals("com.cardcamera.EDIT_ITEM")) {
        	// 接收與設定記事標題
    	    //tell = true;
        	String titleText = intent.getStringExtra("titleText");
        	String contentText = intent.getStringExtra("contentText");
        	title_text.setText(titleText);
        	content_text.setText(contentText);
        	byte[] theImage2 = intent.getByteArrayExtra("theImage"); 
	          
	        Bitmap bitmap2 = BitmapFactory.decodeByteArray(theImage2, 0, theImage2.length);         	 
        	iv.setImageBitmap(bitmap2);
         }
       
      // mytypeList = new ArrayAdapter<String>(ItemActivity.this,android.R.layout.simple_spinner_item, mytype);
       ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.mytypes_array,
               android.R.layout.simple_spinner_item);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner.setAdapter(adapter);
       spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if(position > 0)
				Toast.makeText(ItemActivity.this, "你選的是"+ arg0.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
				
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				//Toast.makeText(ItemActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
			}
       });
       
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				
		switch (requestCode) {
        	case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
        		startPhotoZoom(Uri.fromFile(tempFile));
        		break;
        	case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
        		// 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
        		if (data != null)
        			startPhotoZoom(data.getData());
        			break;
        	case PHOTO_REQUEST_CUT:// 返回的结果
        		if (data != null)              
        			sentPicToNext(data);
        			break;
		}			
	}
		
	private ImageButton.OnClickListener event =new ImageButton.OnClickListener() {
		
		public void onClick(View v){
		tell =true;	
		Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	            // 指定调用相机拍照后照片的储存路径
	    cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
	    startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
		}
	};
	
	private ImageButton.OnClickListener  event2 =new ImageButton.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		tell =true;
		
		
		Intent albumintent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		albumintent.setType("image/*");
		albumintent.setType("image/*");
		albumintent.putExtra("crop", "true");
		albumintent.putExtra("aspectX", 1);
		albumintent.putExtra("aspectY", 1);
		albumintent.putExtra("outputX", 300);
	    albumintent.putExtra("outputY", 300);
	    albumintent.putExtra("return-data", true);
		startActivityForResult(albumintent, PHOTO_REQUEST_CUT);
		 
		}
	};
	
				
	private void startPhotoZoom(Uri uri) {
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		 // aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
		 
		// 将进行剪裁后的图片传递到下一个界面上
		
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		
		return dateFormat.format(date) + ".jpg";
	}
		    
	private void sentPicToNext(Intent picdata) {
		        
		Bundle bundle = picdata.getExtras();		        
		if (bundle != null) {		           
			Bitmap photo = bundle.getParcelable("data");		            
			if (photo==null) {		                
				iv.setImageResource(R.drawable.orange);		            
			}
			else {		               
				iv.setImageBitmap(photo);
//	      设置文本内容为    图片绝对路径和名字		                
				tv.setText(tempFile.getAbsolutePath());
		    }
		            
			ByteArrayOutputStream baos = null;		            
			try {	
				
				baos = new ByteArrayOutputStream();		               
				photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);		               
				byte[] photodata = baos.toByteArray();		                
				System.out.println(photodata.toString());
				
				Intent intent = this.getIntent();
				intent.setClass(ItemActivity.this, MainActivity.class);		                
			
				intent.putExtra("photo", photodata);		                
				setResult(Activity.RESULT_OK, intent);
	
		            
			} 
			catch (Exception e) {		                
				e.getStackTrace();		            
			} 
			finally {		                
				if (baos != null) {		                    
					try {	                        
						baos.close();		                    
					} 
					catch (Exception e) {		                        
						e.printStackTrace();
		             }
				}
			}	        
		}		    
	}

	
	    		        	
	// 點擊確定與取消按鈕都會呼叫這個方法
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void onSubmit(View view) {
		
		// 讀取使用者輸入的標題與內容
		String titleText = title_text.getText().toString();
		String contentText =content_text.getText().toString();
		
		// 取得回傳資料用的Intent物件
		Intent result = this.getIntent();		
		// 確定按鈕
		if (view.getId() == R.id.button1) {
		
			
			// 設定標題與內容
			result.putExtra("titleText", titleText);
			result.putExtra("contentText", contentText);
			if(spinner.getSelectedItemPosition() == 0){
				spinner.setSelection(1);
			}
			result.putExtra("getData",spinner.getSelectedItem().toString().toString());
			setResult(Activity.RESULT_OK, result);
		}		        
	    if (view.getId() ==R.id.button2){
	    	tell =true;
	    	ItemActivity.this.setResult(Activity.RESULT_CANCELED,result);
			
		}
		
		// 結束
		if(tell ==true){
	    
			finish();	
		}
		else{
			Toast.makeText(this, "請選擇照片", Toast.LENGTH_LONG).show();
		}
	}
	
	

	// 使用者選擇所有的選單項目都會呼叫這個方法
	public void clickMenuItem(MenuItem item) {
	    // 使用參數取得使用者選擇的選單項目元件編號
	    int itemId = item.getItemId();
	    switch (itemId) {

	    // 使用者選擇新增選單項目    
	   
	    case android.R.id.home:
	    	
	    }
	 }
}
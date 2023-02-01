package com.rabbi.gmailbomber;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.net.Uri;
import android.view.LayoutInflater;
import android.content.SharedPreferences;
import android.content.Context;

public class MainActivity extends Activity implements View.OnClickListener {      
    private Button startBombingBtn,termsBtn,howToUseBtn,aboutBtn,moreAppsBtn;
    AlertDialog.Builder adb;
    AlertDialog ShowAlert;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Show Warning Popup
       adb = new AlertDialog.Builder(this);
       LayoutInflater lf = LayoutInflater.from(this);
       View view = lf.inflate(R.layout.warning_popup,null);
       Button agreeBtn = view.findViewById(R.id.agreeBtn);;
       agreeBtn.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View p1) {
                ShowAlert.cancel();
                // write agree data 
                SharedPreferences sp = getSharedPreferences("appdata",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();                
                editor.putString("isAgree","true");
                editor.commit();
                
             }                       
       });
       adb.setView(view);
       adb.setCancelable(false);
       ShowAlert = adb.create();
       
       
       // Read agree data
       SharedPreferences sp = getSharedPreferences("appdata",Context.MODE_PRIVATE);
       String isAgree = sp.getString("isAgree","false");
       //Toast.makeText(getApplication(), isAgree, Toast.LENGTH_SHORT).show();
       if(isAgree == "true"){
          
       }else if(isAgree == "false"){
          ShowAlert.show();
       }
       
       
        startBombingBtn = findViewById(R.id.startBombingBtn);
        termsBtn = findViewById(R.id.termsBtn);
        howToUseBtn = findViewById(R.id.howToUseBtn);
        aboutBtn = findViewById(R.id.aboutBtn);
        moreAppsBtn = findViewById(R.id.moreAppsBtn);
        
        startBombingBtn.setOnClickListener(this);
        termsBtn.setOnClickListener(this);
        howToUseBtn.setOnClickListener(this);
        aboutBtn.setOnClickListener(this);
        moreAppsBtn.setOnClickListener(this);
        
        
       adb = new AlertDialog.Builder(this);
       adb.setTitle("Warning");
       adb.setMessage("Are you sure you want to delete this message.You can't retrive message once deleted.");
       

       
    }
    
   @Override
   public void onClick(View btn){
      switch(btn.getId()){
         case R.id.startBombingBtn:
            startActivity(new Intent(getApplicationContext(),StartBombing.class));
            overridePendingTransition(0,1);
            break;
         case R.id.termsBtn:
            startActivity(new Intent(getApplicationContext(),TermsAndConditions.class));          
            overridePendingTransition(0,1);
            break;
            
         case R.id.howToUseBtn:            
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://fh-rabbi.github.io/Ghost-Mailer/"));
            startActivity(intent);    
            overridePendingTransition(0,1);
            break;
            
         case R.id.aboutBtn:
            Intent intent2 = new Intent(Intent.ACTION_VIEW,Uri.parse("https://cutt.ly/rabbi"));
            startActivity(intent2);          
            overridePendingTransition(0,1);
            break;
            
         case R.id.moreAppsBtn:
            Intent intent3 = new Intent(Intent.ACTION_VIEW,Uri.parse("https://frappstore.up.railway.app/android-apps"));
            startActivity(intent3);          
            overridePendingTransition(0,1);
            break;
            
          default:
              
      }
   }
    
} 


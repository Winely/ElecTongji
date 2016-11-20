package me.donggu.electongji;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        balance=(TextView)findViewById(R.id.balance);
//        textView=(TextView)findViewById(R.id.textView);
        viewState=getIntent().getStringExtra("viewState");
        try{
            viewState= URLEncoder.encode(viewState, "gb2312");
        }catch(IOException e){
            e.printStackTrace();
        }
        postBody="__EVENTTARGET=&radio=usedR&ImageButton1.x=1&ImageButton1.y=1"
                + "&__VIEWSTATE="+viewState
                + "&drlouming="+getIntent().getStringExtra("xiaoqu")
                + "&drceng="+getIntent().getStringExtra("loudong")
                + "&dr_ceng="+getIntent().getStringExtra("louceng")
                + "&drfangjian="+getIntent().getStringExtra("fangjian");
        new Thread(getBalance).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==RESULT){
                balance.setText((String)msg.obj);
                Log.d("Result", (String)msg.obj);
            }
        }
    };

    private Runnable getBalance = new Runnable() {
        @Override
        public void run() {
            String result = "";
            try{
                Connection.Response response = Jsoup.connect(getIntent().getStringExtra("url"))
                        .requestBody(postBody)
                        .followRedirects(false)
                        .method(Connection.Method.POST)
                        .execute();
                Document doc = Jsoup.connect(getIntent().getStringExtra("url")+response.header("Location"))
                        .cookies(response.cookies())
                        .timeout(6000)
                        .get();
                result = doc.select("h6").first().text();
                Log.d("Result","result = "+result);
            }catch (IOException e){
                e.printStackTrace();
                Log.d("ResultActibity", "Error");
            }
            Message msg = new Message();
            msg.what=RESULT;
            msg.obj=result;
            handler.sendMessage(msg);
        }
    };

    private final int RESULT = 3;
    private TextView balance;
    private String postBody;
    private String viewState = "";
}

package me.donggu.electongji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import me.donggu.model.ListItem;
import me.donggu.model.ListIthemAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LoucengActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_louceng);
        loucengView = (ListView)findViewById(R.id.list_louceng);
        viewState = getIntent().getStringExtra("viewState");
        try {
            viewState= URLEncoder.encode(viewState, "gb2312");
        }catch (IOException e){
            e.printStackTrace();
        }
        postBody = "__EVENTTARGET="
                + "&__VIEWSTATE="+viewState
                + "&drlouming="+getIntent().getStringExtra("xiaoqu")
                + "&drceng="+getIntent().getStringExtra("loudong");

        new Thread(getLouceng).start();
        loucengView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FANGJIAN);
                intent.putExtra("viewState",viewState);
                intent.putExtra("louceng", loucengList.get(i).getValue());
                intent.putExtra("url", getIntent().getStringExtra("url"));
                intent.putExtra("xiaoqu", getIntent().getStringExtra("xiaoqu"));
                intent.putExtra("loudong",getIntent().getStringExtra("loudong"));
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==LOUDONG){
                loucengView.setAdapter(loucengAdapter);
            }
        }
    };

    private Runnable getLouceng = new Runnable() {
        @Override
        public void run() {
            try{
                Document doc = Jsoup.connect(getIntent().getStringExtra("url"))
                        .requestBody(postBody)
                        .post();
                Element element = doc.select("#dr_ceng").first();
                for(Element e:element.children()){
                    if(e.hasAttr("selected"))continue;
                    loucengList.add(new ListItem(e.val(),e.text()));
                }
                viewState = doc.select("#__VIEWSTATE").first().val();
            }catch (IOException e){
                e.printStackTrace();
            }
            if(loucengList.isEmpty()){
                Intent intent = new Intent(FANGJIAN);
                intent.putExtra("viewState",viewState);
                intent.putExtra("louceng", "");
                intent.putExtra("url", getIntent().getStringExtra("url"));
                intent.putExtra("xiaoqu", getIntent().getStringExtra("xiaoqu"));
                intent.putExtra("loudong",getIntent().getStringExtra("loudong"));
                startActivity(intent);
                Log.d("Louceng", "没有楼层");
            }
            else{
                loucengAdapter = new ListIthemAdapter(
                        LoucengActivity.this, R.layout.list_item_layout, loucengList);
                handler.sendEmptyMessage(LOUDONG);
            }
        }
    };

    private ListView loucengView;
    private ArrayList<ListItem> loucengList = new ArrayList<>();
    private ListIthemAdapter loucengAdapter;
    private final String FANGJIAN = "me.donggu.electongji.FANGJIAN";
    private final int LOUDONG = 2;
    private String postBody;
    private String viewState="";
    private String loucengValue;
}

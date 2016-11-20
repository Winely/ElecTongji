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
import java.util.ArrayList;

public class LoudongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loudong);
        loudongView = (ListView)findViewById(R.id.list_drloudong);
        postBody = "__EVENTTARGET="+
                "&drlouming="+getIntent().getStringExtra("xiaoqu");
        new Thread(getLoudong).start();
        loudongView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LOUCENG);
                intent.putExtra("viewState",viewState);
                intent.putExtra("loudong", loudongList.get(i).getValue());
                intent.putExtra("url", getIntent().getStringExtra("url"));
                intent.putExtra("xiaoqu", getIntent().getStringExtra("xiaoqu"));
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==LOUDONG){
                loudongView.setAdapter(loudongAdapter);
            }
        }
    };

    private Runnable getLoudong = new Runnable() {
        @Override
        public void run() {
            try{
                Document doc = Jsoup.connect(getIntent().getStringExtra("url"))
                        .requestBody(postBody)
                        .post();
                Element element = doc.select("#drceng").first();
                for(Element e:element.children()){
                    if(e.hasAttr("selected"))continue;
                    loudongList.add(new ListItem(e.val(),e.text()));
                }
                viewState = doc.select("#__VIEWSTATE").first().val();
            }catch (IOException e){
                e.printStackTrace();
            }
            loudongAdapter = new ListIthemAdapter(
                    LoudongActivity.this, R.layout.list_item_layout, loudongList);
            handler.sendEmptyMessage(LOUDONG);
        }
    };

    private ListView loudongView;
    private ArrayList<ListItem> loudongList = new ArrayList<>();
    private ListIthemAdapter loudongAdapter;
    private final String LOUCENG = "me.donggu.electongji.LOUCENG";
    private final int LOUDONG = 1;
    private String postBody;
    private String viewState="";
    private String loudongValue;
}

package me.donggu.electongji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

public class FangjianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangjian);
        fangjianView = (ListView)findViewById(R.id.list_fangjian);
        viewState = getIntent().getStringExtra("viewState");
        try {
            viewState= URLEncoder.encode(viewState, "gb2312");
        }catch (IOException e){
            e.printStackTrace();
        }
        postBody = "__EVENTTARGET="
                + "&__VIEWSTATE="+viewState
                + "&drlouming="+getIntent().getStringExtra("xiaoqu")
                + "&drceng="+getIntent().getStringExtra("loudong")
                + "&dr_ceng="+getIntent().getStringExtra("louceng");

        new Thread(getFangjian).start();
        fangjianView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RESULT);
                intent.putExtra("viewState",viewState);
                intent.putExtra("fangjian", fangjianList.get(i).getValue());
                intent.putExtra("url", getIntent().getStringExtra("url"));
                intent.putExtra("xiaoqu", getIntent().getStringExtra("xiaoqu"));
                intent.putExtra("loudong",getIntent().getStringExtra("loudong"));
                intent.putExtra("louceng", getIntent().getStringExtra("louceng"));
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==FANGJIAN){
                fangjianView.setAdapter(fangjianAdapter);
            }
        }
    };

    private Runnable getFangjian = new Runnable() {
        @Override
        public void run() {
            try{
                Document doc = Jsoup.connect(getIntent().getStringExtra("url"))
                        .requestBody(postBody)
                        .post();
                Element element = doc.select("#drfangjian").first();
                for(Element e:element.children()){
                    if(e.hasAttr("selected"))continue;
                    fangjianList.add(new ListItem(e.val(),e.text()));
                }
                viewState = doc.select("#__VIEWSTATE").first().val();
            }catch (IOException e){
                e.printStackTrace();
            }
            fangjianAdapter = new ListIthemAdapter(
                    FangjianActivity.this, R.layout.list_item_layout, fangjianList);
            handler.sendEmptyMessage(FANGJIAN);
        }
    };

    private ListView fangjianView;
    private ArrayList<ListItem> fangjianList = new ArrayList<>();
    private ListIthemAdapter fangjianAdapter;
    private final String RESULT = "me.donggu.electongji.RESULT";
    private final int FANGJIAN = 2;
    private String postBody;
    private String viewState="";
}

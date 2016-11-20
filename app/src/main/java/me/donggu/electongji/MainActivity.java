package me.donggu.electongji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xiaoquView =(ListView)findViewById(R.id.list_xiaoqu);
        new Thread(getXiaoqu).start();
        xiaoquView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String loumingIndex = xiaoquList.get(i).getValue();
                Intent intent = new Intent(LOUDONG);
                intent.putExtra("xiaoqu", loumingIndex);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== XIAOQU){
                xiaoquView.setAdapter(xiaoquAdapter);
                Log.d("Xiaoqu", "Here!");
            }
        }
    };

    Runnable getXiaoqu = new Runnable() {
        @Override
        public void run() {
            try{
                Document doc = Jsoup.connect(url).get();
                Element element = doc.select("#drlouming").first();
                for(Element e : element.children()){
                    if(e.hasAttr("selected"))continue;
                    xiaoquList.add(new ListItem(e.val(),e.text()));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            xiaoquAdapter =new ListIthemAdapter(
                    MainActivity.this, R.layout.list_item_layout, xiaoquList);
            handler.sendEmptyMessage(XIAOQU);
        }
    };

    private ListView xiaoquView;
    private final int XIAOQU = 0;
    private ArrayList<ListItem> xiaoquList = new ArrayList<>();
    private ListIthemAdapter xiaoquAdapter;
    private String url="http://202.120.163.129:88/";

    public final String LOUDONG = "me.donggu.electongji.LOUDONG";
}

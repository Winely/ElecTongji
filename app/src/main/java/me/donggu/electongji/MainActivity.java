package me.donggu.electongji;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
        loumingView=(ListView)findViewById(R.id.list_drlouming);
        new Thread(getLouming).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==LOUMING){
                loumingView.setAdapter(loumingAdapter);
                Log.d("Louming", "Here!");
            }
        }
    };

    Runnable getLouming = new Runnable() {
        @Override
        public void run() {
            try{
                Document doc = Jsoup.connect(url).get();
                Element element = doc.select("#drlouming").first();
                for(Element e : element.children()){
                    if(e.hasAttr("selected"))continue;
                    loumingList.add(new ListItem(e.val(),e.text()));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            loumingAdapter=new ListIthemAdapter(
                    MainActivity.this, R.layout.list_item_layout, loumingList);
            Message msg = new Message();
            msg.what=LOUMING;
            handler.sendMessage(msg);
        }
    };

    private ListView loumingView;
    private int LOUMING = 0;
    private ArrayList<ListItem> loumingList = new ArrayList<>();
    private ListIthemAdapter loumingAdapter;
    private String url="http://202.120.163.129:88/";
}

package com.example.ice.screw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.navi);

        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.list_item, new String[] {"title"}, new int[] {R.id.title});
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent target = new Intent();
                target.setClassName(getApplicationContext(), getApplicationContext().getPackageName() + getData().get(position).get("intent").toString());
                startActivity(target);
            }
        });
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "隐患上报");
        map.put("intent", ".FiberRouteActivity");
        list.add(map);

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("title", "资源查询");
        map1.put("intent", ".FiberRouteActivity");
        list.add(map1);

        Map<String, Object> hm2 = new HashMap<String, Object>();
        hm2.put("title", "光路查询");
        hm2.put("intent", ".FiberRouteActivity");
        list.add(hm2);

        Map<String, Object>  map3 = new HashMap<String, Object>();
        map3.put("title", "局点查询");
        map3.put("intent", ".StationActivity");
        list.add(map3);

        return list;
    }

    @Override
    public boolean onSearchRequested(){
        //打开浮动搜索框（第一个参数默认添加到搜索框的值）
        startSearch(null, false, null, false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

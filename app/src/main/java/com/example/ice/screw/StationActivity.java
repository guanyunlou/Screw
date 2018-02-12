package com.example.ice.screw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;


public class StationActivity extends Activity {

    private List<Station> mData = new ArrayList<Station>();  // 这个数据会改变
    private List<Station> mBackData;  // 这是原始的数据
    private MyAdapter mAdapter;
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setSubmitButtonEnabled(false);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setTextFilterEnabled(true);
        mListView.setOnItemClickListener(new ItemClick());
        // mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
        // mListView.setTextFilterEnabled(true);
        initData();

        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                ListAdapter adapter = mListView.getAdapter();
                if (adapter instanceof Filterable) {
                    Filter filter = ((Filterable) adapter).getFilter();
                    if (newText == null || newText.length() == 0) {
                        filter.filter("");
                    } else {
                        filter.filter(newText);
                    }
                }


                /* ListView自带的方法
                if (newText == null || newText.length() == 0) {
                    mListView.clearTextFilter();
                } else {
                    mListView.setFilterText(newText);
                }
                */
                return true;
            }
        });
    }

    private class MyAdapter extends BaseAdapter implements Filterable {
        private MyFilter mFilter;

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(StationActivity.this, R.layout.list_item,
                        null);
            }

            TextView show = (TextView) convertView.findViewById(R.id.title);

            show.setText(mData.get(position).getName());

            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (null == mFilter) {
                mFilter = new MyFilter();
            }
            return mFilter;
        }

        // 自定义Filter类
        class MyFilter extends Filter {
            @Override
            // 该方法在子线程中执行
            // 自定义过滤规则
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                List<Station> newValues = new ArrayList<Station>();
                String filterString = constraint.toString().trim()
                        .toLowerCase();

                // 如果搜索框内容为空，就恢复原始数据
                if (TextUtils.isEmpty(filterString)) {
                    newValues = mBackData;
                } else {
                    // 过滤出新数据
                    for (Station sta : mBackData) {
                        if (-1 != sta.getName().toLowerCase().indexOf(filterString)) {
                            newValues.add(sta);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mData = (List<Station>) results.values;

                if (results.count > 0) {
                    mAdapter.notifyDataSetChanged();  // 通知数据发生了改变
                } else {
                    mAdapter.notifyDataSetInvalidated(); // 通知数据失效
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_station, menu);
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

    private void initData() {
        /*
        mData.add(new Station("512SZ.XYJMJ", "西园局西园局", "桐泾北路514号", 120.595564,31.324735));
        mData.add(new Station("512SZ.CXJMJ", "城西局城西局", "三香路1036号", 120.590661,31.303527));
        */
        readExcel();
        mBackData = mData;
    }

    private void readExcel() {
        try {

            /**
             * 后续考虑问题,比如Excel里面的图片以及其他数据类型的读取
             **/
            // InputStream is = new FileInputStream(getResources().getAssets().open("station.xls"));

            Workbook book = Workbook
                    .getWorkbook(getResources().getAssets().open("station.xls"));
            book.getNumberOfSheets();
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int rows = sheet.getRows();
            int cols = sheet.getColumns();

            for (int i = 1; i < rows; ++i) {
                Station station = new Station();
                station.setName((sheet.getCell(0, i)).getContents());
                station.setNo((sheet.getCell(1, i)).getContents());
                station.setAddress((sheet.getCell(2, i)).getContents());
                station.setLongitude(Double.parseDouble((sheet.getCell(4, i)).getContents()));
                station.setLatitude(Double.parseDouble((sheet.getCell(5, i)).getContents()));
                mData.add(station);
            }
            book.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private class ItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // Toast.makeText(StationActivity.this, mData.get(position), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("station", mData.get(position));
            intent.putExtras(bundle);

            Log.i("getApplicationContext():", getApplicationContext().toString());
            Log.i("getApplicationContext().getPackageName():", getApplicationContext().getPackageName());
            intent.setClassName(getApplicationContext(), getApplicationContext().getPackageName() + ".MapActivity");
            startActivity(intent);
        }
    }
}

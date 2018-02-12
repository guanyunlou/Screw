package com.example.ice.screw;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;


public class MapActivity extends Activity {
    MapView mMapView = null;
    private LatLng hmPos;
    private LatLng baiduPoint;
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        Bundle bundle = this.getIntent().getExtras();
        Station station = (Station)bundle.getParcelable("station");

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();

        hmPos = new LatLng(station.getLatitude(), station.getLongitude());

        // GPS坐标转换为百度经纬度坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(hmPos);
        baiduPoint = converter.convert();

        // 设置地图中心点
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(baiduPoint));

        // 设置缩放级别
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));

        // 打开热力图
        baiduMap.setBaiduHeatMapEnabled(false);

        // 添加文字覆盖物
        drawMarker();

        //对 marker 添加点击相应事件
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Marker被点击了！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void drawCircle() {
        //定义一个圆 ： 圆心+半径

        //1.创建自己
        CircleOptions circleOptions = new CircleOptions();
        //2.给自己设置数据
        circleOptions.center(hmPos) //圆心
                .radius(1000)//半径 单位米
                .fillColor(0x60ff0000)//填充色
                .stroke(new Stroke(2,0x6000ff00));//边框宽度和颜色

        //3.把覆盖物添加到地图中
        baiduMap.addOverlay(circleOptions);
    }

    private void drawText() {
        TextOptions textOptions = new TextOptions();

        textOptions.fontColor(0x60ff0000)//设置字体颜色
                .text("城西局城西局")//文字内容
                .position(hmPos)//位置
                .fontSize(24)//字体大小
                .typeface(Typeface.SERIF)//字体
                .rotate(30);//旋转

        baiduMap.addOverlay(textOptions);
    }

    private  void drawMarker() {
        MarkerOptions markerOptions = new MarkerOptions();

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
        markerOptions.icon(bitmap)
                .position(baiduPoint);

        baiduMap.addOverlay(markerOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}

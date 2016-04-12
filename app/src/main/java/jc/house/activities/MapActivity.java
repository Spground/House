package jc.house.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jc.house.R;
import jc.house.models.HouseDetail;
import jc.house.views.TitleBar;

public class MapActivity extends com.tencent.tencentmap.mapsdk.map.MapActivity {

    private MapView mapView;
    private boolean isSingleMarker = true;
    public static final String FLAG_IsSingleMarker = "isSingleMarker";
    public static final String FLAG_HOUSE = "house";
    public static final String FLAG_HOUSES = "houses";
    private Map<String, HouseDetail> mapHouses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        TitleBar titleBar = (TitleBar) this.findViewById(R.id.titlebar);
        titleBar.setTitle("地图详情");
        this.mapView = (MapView) this.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        this.mapView.getMap().setZoom(16);
        Intent intent = this.getIntent();
        isSingleMarker = intent.getBooleanExtra(FLAG_IsSingleMarker, true);
        if (isSingleMarker) {
            HouseDetail house = intent.getParcelableExtra(FLAG_HOUSE);
            setMapViewData(house);
            this.mapView.getMap().setCenter(new LatLng(house.getLat(), house.getLng()));
            this.mapView.getMap().setZoom(15);
        } else {
            List<HouseDetail> houses = intent.getParcelableArrayListExtra(FLAG_HOUSES);
            setMapViewDatas(houses);
            LatLng center = getCenterPoint();
            if (null != center) {
                this.mapView.getMap().setCenter(center);
            }
            this.mapView.getMap().setZoom(12);
        }
    }

    private void setMapViewData(HouseDetail houseDetail) {
        Marker marker = mapView.getMap().addMarker(new MarkerOptions().position(new LatLng(houseDetail.getLat(), houseDetail.getLng())).title(houseDetail.getName()).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)));
        marker.setDraggable(true);
        marker.showInfoWindow();
        marker.setSnippet("项目位置:" + houseDetail.getAddress());
        if (this.isSingleMarker) {
            this.mapView.getMap().setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    finish();
                }
            });
        } else {
            mapHouses.put(marker.getSnippet(), houseDetail);
            this.mapView.getMap().setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(MapActivity.this, HouseDetailActivity.class);
                    HouseDetail item = mapHouses.get(marker.getSnippet());
                    if (null != item) {
                        intent.putExtra(HouseDetailActivity.FLAG_HOUSE_DETAIL, item);
                        if (null != item.getHelper()) {
                            intent.putExtra(HouseDetailActivity.FLAG_HELPER_NAME, item.getHelper().getName());
                            intent.putExtra(HouseDetailActivity.FLAG_HELPER_ID, item.getHelper().getHxID());
                        }
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void setMapViewDatas(List<HouseDetail> houses) {
        this.mapHouses = new HashMap<>();
        for (HouseDetail house : houses) {
            setMapViewData(house);
        }
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    private LatLng getCenterPoint() {
        if (isSingleMarker || mapHouses.isEmpty()) {
            return null;
        }
        double latAll = 0;
        double lngAll = 0;
        Iterator<Map.Entry<String, HouseDetail>> iterator = mapHouses.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, HouseDetail> entry = iterator.next();
            latAll += entry.getValue().getLat();
            lngAll += entry.getValue().getLng();
        }
        return new LatLng(latAll / mapHouses.size(), lngAll / mapHouses.size());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

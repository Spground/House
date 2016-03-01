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
import jc.house.models.House;
import jc.house.views.TitleBar;

public class MapActivity extends com.tencent.tencentmap.mapsdk.map.MapActivity {

    private MapView mapView;
    private boolean isSingleMarker = true;
    public static final String FLAG_IsSingleMarker = "isSingleMarker";
    public static final String FLAG_HOUSE = "house";
    public static final String FLAG_HOUSES = "houses";
    private Map<String, House> mapHouses;

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
            House house = intent.getParcelableExtra(FLAG_HOUSE);
            setMapViewData(house);
            this.mapView.getMap().setCenter(new LatLng(house.getLat(), house.getLng()));
            this.mapView.getMap().setZoom(15);
        } else {
            List<House> houses = intent.getParcelableArrayListExtra(FLAG_HOUSES);
            setMapViewDatas(houses);
            LatLng center = getCenterPoint();
            if (null != center) {
                this.mapView.getMap().setCenter(center);
            }
            this.mapView.getMap().setZoom(12);
        }
    }

    private void setMapViewData(House house) {
        Marker marker = mapView.getMap().addMarker(new MarkerOptions().position(new LatLng(house.getLat(), house.getLng())).title(house.getName()).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)));
        marker.setDraggable(true);
        marker.showInfoWindow();
        if (this.isSingleMarker) {
            this.mapView.getMap().setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    finish();
                }
            });
        } else {
            mapHouses.put(marker.getTitle(), house);
            this.mapView.getMap().setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(MapActivity.this, HouseDetailActivity.class);
                    House item = mapHouses.get(marker.getTitle());
                    if (null != item) {
                        intent.putExtra(HouseDetailActivity.FLAG_ID, item.id);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void setMapViewDatas(List<House> houses) {
        this.mapHouses = new HashMap<>();
        for (House house : houses) {
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
        if (!isSingleMarker && mapHouses.size() > 0) {
            double latAll = 0;
            double lngAll = 0;
            Iterator<Map.Entry<String, House>> iterator = mapHouses.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<String, House> entry = iterator.next();
                latAll += entry.getValue().getLat();
                lngAll += entry.getValue().getLng();
            }
            return new LatLng(latAll / mapHouses.size(), lngAll / mapHouses.size());
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

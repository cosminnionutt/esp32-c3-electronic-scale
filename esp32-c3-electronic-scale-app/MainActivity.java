package com.vaemc.iotelectronicscale;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String SERVICE_UUID = "367c26b7-7617-4cc1-8e59-a0dc74e5ab99";
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private BleDeviceAdapter bleDeviceAdapter;
    private FloatingActionButton btnRefresh;

    private void bleRefresh(){
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                bleDeviceAdapter.addData(bleDevice);
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        bleDeviceAdapter = new BleDeviceAdapter(R.layout.item_ble, new ArrayList<BleDevice>());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(bleDeviceAdapter);

        bleDeviceAdapter.setOnItemClickListener((adapter, view, position) -> Toast.makeText(MainActivity.this, "asd", Toast.LENGTH_SHORT).show());

        btnRefresh.setOnClickListener(v -> {
            bleDeviceAdapter.setNewInstance( new ArrayList<BleDevice>());
            bleRefresh();
        });


        String[] perms = {Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {

            BleManager.getInstance().init(getApplication());

            BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                    .setServiceUuids(new UUID[]{UUID.fromString(SERVICE_UUID)})
                    .setScanTimeOut(5000)
                    .build();

            BleManager.getInstance().initScanRule(scanRuleConfig);

            bleRefresh();

        } else {
            EasyPermissions.requestPermissions(this, "",
                    1, perms);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnRefresh = (FloatingActionButton) findViewById(R.id.btn_refresh);
    }
}
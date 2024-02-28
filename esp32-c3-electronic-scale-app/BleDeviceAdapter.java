package com.vaemc.iotelectronicscale;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.clj.fastble.data.BleDevice;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BleDeviceAdapter extends BaseQuickAdapter<BleDevice, BaseViewHolder> {


    public BleDeviceAdapter(int layoutResId,List<BleDevice> list) {
        super(layoutResId, list);
    }




    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, BleDevice bleDevice) {
        baseViewHolder.setText(R.id.tv_mac, bleDevice.getMac());
        baseViewHolder.setText(R.id.tv_rssi,bleDevice.getRssi()+" dBm");
    }
}
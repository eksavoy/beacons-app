/*
package com.isen.urba.beaconapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.isen.urba.beaconapp.adapter.AddBeacons.AddBeaconsAdapter;
import com.isen.urba.beaconapp.utils.BeaconsUtils;
import com.isen.urba.beaconapp.utils.ViewUtils;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.List;

*/
/**
 * Created by romain on 26/05/2016.
 *//*

public class AddPopup {
    boolean popupOpen = false;
    Activity activity;
    Context context;
    Collection<Beacon> beaconsFind;
    List<com.isen.urba.beaconapp.pojo.Beacon> beaconsAuthorized;
    PopupWindow popupWindow;

    public AddPopup(Activity activity, Context context, Collection<Beacon> beaconsFind, List<com.isen.urba.beaconapp.pojo.Beacon> beaconsAuthorized) {
        this.activity = activity;
        this.context = context;
        this.beaconsFind = beaconsFind;
        this.beaconsAuthorized = beaconsAuthorized;
        this.instantiatePopup();
    }

    private void instantiatePopup(){
        if(!popupOpen){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            final View popup = layoutInflater.inflate(R.layout.popup, null);
            AddBeaconsAdapter adapter = new AddBeaconsAdapter(popup.getContext(), R.layout.add_beacon_list_item);
            ListView popupListView = ViewUtils.instantiatePopupView(activity,adapter);
            popupWindow = new PopupWindow(
                    popupListView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            Button add = (Button) popup.findViewById(R.id.popup_add_item);
            add.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {

                    String id = ((TextView) popup.findViewById(R.id.popup_beaconBluetoothName)).getText().toString();
                    String name = ((EditText) popup.findViewById(R.id.popup_name)).getText().toString();
                    if(!id.isEmpty() && !name.isEmpty()){
                        Beacon finded = BeaconsUtils.searchById(beaconsFind, id);
                        com.isen.urba.beaconapp.pojo.Beacon alreadyAdded = BeaconsUtils.searchById(beaconsAuthorized, id);
                        if((finded != null) && (alreadyAdded == null)){
                            beaconsAuthorized.add(new com.isen.urba.beaconapp.pojo.Beacon(name, finded.getBluetoothName(), finded.getBluetoothAddress(), finded.getRssi()));
                            popupWindow.dismiss();
                            popupOpen = false;
                        }else{
                            Toast.makeText(context, "Beacon not found", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, "Id or Name empty", Toast.LENGTH_LONG).show();
                    }
                }
            });
           */
/* Button close = (Button) popup.findViewById(R.id.btn_popup_close);
            close.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    popupOpen = false;
                }
            });*//*

            popupWindow.setFocusable(true);
            popupWindow.update();
            popupOpen = true;
        }
    }
    public void PopupWindowsAction(){
        popupWindow.showAsDropDown(activity.findViewById(R.id.action_add));
    }
}
*/

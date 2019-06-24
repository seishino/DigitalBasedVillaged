package com.dbv.digitalbasedvillaged;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListAnggotaScanAdapter extends ArrayAdapter<String> {
    private List<String> listNama;
    private List<String> listNoKK;
    private Activity context;

    public ListAnggotaScanAdapter(Activity context, List<String> listNoKK, List<String> listNama) {
        super(context, R.layout.list_item_anggota_scan,listNoKK);
        this.context = context;
        this.listNama = listNama;
        this.listNoKK = listNoKK;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.list_item_anggota_scan, null, true);
        ImageView imgProfile = (ImageView) rowView.findViewById(R.id.profile_pic);
        TextView txtNoKK = (TextView) rowView.findViewById(R.id.txtNoKK);
        TextView txtNama = (TextView) rowView.findViewById(R.id.txtNama);
        txtNoKK.setText(listNoKK.get(position));
        txtNama.setText(listNama.get(position));
        return rowView;
    }
}

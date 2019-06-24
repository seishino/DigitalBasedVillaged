package com.dbv.digitalbasedvillaged;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAnggotaAdapter extends ArrayAdapter<String> {
    private List<String> listNama;
    private List<String> listNoKK;
    private Activity context;

    public ListAnggotaAdapter(Activity context, List<String> listNoKK, List<String> listNama) {
        super(context, R.layout.list_item_anggota,listNoKK);
        this.context = context;
        this.listNama = listNama;
        this.listNoKK = listNoKK;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.list_item_anggota, null, true);
        TextView txtNoKK = (TextView) rowView.findViewById(R.id.txtNoKK);
        TextView txtNama = (TextView) rowView.findViewById(R.id.txtNama);
        txtNoKK.setText(listNoKK.get(position));
        txtNama.setText(listNama.get(position));
        return rowView;
    }
}

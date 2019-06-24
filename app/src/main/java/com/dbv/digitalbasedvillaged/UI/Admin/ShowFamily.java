package com.dbv.digitalbasedvillaged.UI.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dbv.digitalbasedvillaged.Controller.AdminController;
import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerFamily;
import com.dbv.digitalbasedvillaged.R;
import com.dbv.digitalbasedvillaged.UI.Admin.Adapter.ShowFamilyAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowFamily  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;
    private AdminController adminController = new AdminController();
    private ShowFamily root;
    private List<Villager> villagerList = new ArrayList<>();
    private TextView txtNoKK;
    private TextView txtAddress;
    private TextView txtRT;
    private TextView txtRW;
    private ImageButton btnBack;
    private String noKK;
    private ProgressDialog progressDialog;
    final String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_show_family);
        recyclerView = (RecyclerView)findViewById(R.id.listFamily);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        root = this;
        txtNoKK = (TextView)findViewById(R.id.txtNoKK);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        noKK = getIntent().getStringExtra("noKK");
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        progressDialog = new ProgressDialog(root);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCancelable(false);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getFamily();
    }
    public void getFamily()
    {
        progressDialog.show();
        adminController.fetchDataVillageByNoKK(new OnGetDataListenerFamily() {
            @Override
            public void onComplete(Family family) {
                txtNoKK.setText(family.getNoKK());
                txtAddress.setText(family.getAddress() + " / " + family.getRt() + " / " + family.getRw());
                root.villagerList = family.getFamilyList();
                recyclerViewAdapter = new ShowFamilyAdapter(villagerList,root);
                recyclerView.setAdapter(recyclerViewAdapter);
                progressDialog.dismiss();
            }
        },noKK);
    }
}

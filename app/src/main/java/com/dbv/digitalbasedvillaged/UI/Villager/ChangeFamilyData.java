package com.dbv.digitalbasedvillaged.UI.Villager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dbv.digitalbasedvillaged.Controller.VillagerController;
import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.R;

public class ChangeFamilyData  extends AppCompatActivity {
    private ChangeFamilyData root;
    private ImageButton btnBack;
    private VillagerController villagerController = new VillagerController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.villager_change_data_family);
        root = this;

        final EditText txtRT = (EditText)findViewById(R.id.txtRt);
        final EditText txtRW = (EditText)findViewById(R.id.txtRw);
        final EditText txtAddress = (EditText)findViewById(R.id.txtAddress);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Family family = new Family();
                family.setAddress(txtAddress.getText().toString());
                family.setRt(txtRT.getText().toString());
                family.setRw(txtRW.getText().toString());
                villagerController.updateFamily(family);
                finish();
            }
        });

    }
}

package com.dbv.digitalbasedvillaged.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.dbv.digitalbasedvillaged.R;

public class Register2  extends AppCompatActivity {
    private Register2 root;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_family);
        root = this;

        final EditText txtNIK = (EditText)findViewById(R.id.txtNoKK);
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
                if(txtNIK.getText().toString().equals(""))
                {
                    Toast.makeText(root, "NIK tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtNIK.getText().length()!=17)
                {
                    Toast.makeText(root, "NIK harus 17 digit", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtAddress.getText().toString().equals(""))
                {
                    Toast.makeText(root, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtRT.getText().toString().equals(""))
                {
                    Toast.makeText(root, "RT tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtRW.getText().toString().equals(""))
                {
                    Toast.makeText(root, "RW tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(root, Register.class);
                intent.putExtra("NoKK",txtNIK.getText().toString());
                intent.putExtra("Address",txtAddress.getText().toString());
                intent.putExtra("RT", txtRW.getText().toString());
                intent.putExtra("RW", txtRT.getText().toString());
                startActivity(intent);
            }
        });

    }
}

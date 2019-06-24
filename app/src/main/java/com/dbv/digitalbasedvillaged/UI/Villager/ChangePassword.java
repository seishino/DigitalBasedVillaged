package com.dbv.digitalbasedvillaged.UI.Villager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dbv.digitalbasedvillaged.Controller.VillagerController;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerString;
import com.dbv.digitalbasedvillaged.R;

public class ChangePassword extends AppCompatActivity {
    private ChangePassword root;
    private ImageButton btnBack;
    private VillagerController villagerController = new VillagerController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_change_password);
        root = this;

        final EditText txtOldPassword = (EditText)findViewById(R.id.txtOld);
        final EditText txtConfirmationPassword = (EditText)findViewById(R.id.txtConfirm);
        final EditText txtNewPassword = (EditText)findViewById(R.id.txtNew);
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
                villagerController.getOldPassword(new OnGetDataListenerString() {
                    @Override
                    public void onComplete(String ans) {
                        if(!ans.equals(txtOldPassword.getText().toString()))
                        {
                            Toast.makeText(root, "Password lama anda salah", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!txtConfirmationPassword.getText().toString().equals(txtNewPassword.getText().toString()))
                        {
                            Toast.makeText(root, "Password baru anda harus sama dengan Konfirmasi", Toast.LENGTH_LONG).show();
                            return;
                        }
                        villagerController.changePassword(txtNewPassword.getText().toString());
                        Toast.makeText(root, "Berhasil", Toast.LENGTH_SHORT).show();
                        txtOldPassword.setText("");
                        txtConfirmationPassword.setText("");
                        txtNewPassword.setText("");
                    }
                });
                

            }
        });

    }
}

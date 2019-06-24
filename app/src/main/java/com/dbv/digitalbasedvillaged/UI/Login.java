package com.dbv.digitalbasedvillaged.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dbv.digitalbasedvillaged.Controller.LoginController;
import com.dbv.digitalbasedvillaged.DAO.AdminDataAccess;
import com.dbv.digitalbasedvillaged.DAO.VillagerDataAccess;
import com.dbv.digitalbasedvillaged.Entity.Users;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerBoolean;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerUser;
import com.dbv.digitalbasedvillaged.UI.Villager.DefaultLayout;

import com.dbv.digitalbasedvillaged.R;

public class Login extends AppCompatActivity {
    private LoginController loginController = new LoginController();
    private Login root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        final EditText txtUsername = (EditText)findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
        final TextView txtSignUp = (TextView)findViewById(R.id.txtSignUp);
        TextView txtForget = (TextView)findViewById(R.id.txtForgot);
        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(root, "Silahkan lapor Pak Dukuh untuk direset!", Toast.LENGTH_LONG).show();
            }
        });
        root = this;
        try
        {

            SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
            String username = sharedPreferences.getString("username","null");
            String role = sharedPreferences.getString("role","null");
            if(!username.equals("null") && !role.equals("null"))
            {
                if(role.equals("Admin"))
                {
                    finish();
                    new AdminDataAccess().setNik(username);
                    startActivity(new Intent(root, com.dbv.digitalbasedvillaged.UI.Admin.DefaultLayout.class));
                }
                else if(role.equals("Villager"))
                {
                    finish();
                    new VillagerDataAccess().setNik(username);
                    startActivity(new Intent(root,DefaultLayout.class));
                }

            }
        }
        catch (Exception e)
        {}
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginController.login(new OnGetDataListenerBoolean() {
                    @Override
                    public void onComplete(final boolean ans) {


                            loginController.getUserData(new OnGetDataListenerUser() {
                                @Override
                                public void onComplete(Users users) {
                                    if(ans) {
                                        if (users.getStatus().equals("Verification")) {
                                            SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("username", txtUsername.getText().toString());
                                            if (users.getRole().equals("Admin")) {
                                                editor.putString("role", "Admin");
                                                finish();
                                                new AdminDataAccess().setNik(txtUsername.getText().toString());
                                                startActivity(new Intent(root, com.dbv.digitalbasedvillaged.UI.Admin.DefaultLayout.class));
                                            } else {
                                                editor.putString("role", "Villager");
                                                finish();
                                                new VillagerDataAccess().setNik(txtUsername.getText().toString());
                                                startActivity(new Intent(root, DefaultLayout.class));
                                            }
                                            editor.commit();
                                        } else {
                                            Toast.makeText(root, "Akun anda belum diverifikasi", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(Login.this, "Login gagal", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            },txtUsername.getText().toString());
                            //new VillagerDataAccess().setNik(txtUsername.getText().toString());


                    }
                },txtUsername.getText().toString(), txtPassword.getText().toString());
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root, Register2.class));
            }
        });
    }

}

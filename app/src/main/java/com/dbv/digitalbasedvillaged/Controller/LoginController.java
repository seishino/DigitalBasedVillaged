package com.dbv.digitalbasedvillaged.Controller;

import com.dbv.digitalbasedvillaged.DAO.LoginDataAccess;
import com.dbv.digitalbasedvillaged.Entity.Users;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListener;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerBoolean;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerUser;
import com.google.firebase.database.DataSnapshot;

public class LoginController {
    private LoginDataAccess dataAccess = new LoginDataAccess();
    public void login(final OnGetDataListenerBoolean listenerBoolean, final String username, final String password)
    {
        dataAccess.login(new OnGetDataListener() {

            @Override
            public void onComplete(DataSnapshot ds) {
                for(DataSnapshot dataSnapshot : ds.getChildren())
                {
                    if(dataSnapshot.getKey().equals(username) && dataSnapshot.child("Password").getValue().toString().equals(password))
                    {
                        listenerBoolean.onComplete(true);
                        return;
                    }
                }
                listenerBoolean.onComplete(false);
            }
        });
    }
    public void getUserData(final OnGetDataListenerUser listenerUser, String noKK)
    {
        dataAccess.getUserData(new OnGetDataListener() {

            @Override
            public void onComplete(DataSnapshot ds) {
                Users users = new Users();
                users.setStatus(ds.child("Status").getValue().toString());
                users.setNoKK(ds.getKey());
                users.setRole(ds.child("Role").getValue().toString());
                listenerUser.onComplete(users);
            }
        },noKK);
    }
}

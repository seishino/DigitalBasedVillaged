package com.dbv.digitalbasedvillaged.UI.Admin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dbv.digitalbasedvillaged.Controller.AdminController;
import com.dbv.digitalbasedvillaged.Entity.Users;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerListVillager;
import com.dbv.digitalbasedvillaged.R;
import java.util.ArrayList;
import java.util.List;

public class VerificationAdapter  extends RecyclerView.Adapter<VerificationAdapter.MyViewHolder> {
    private List<Users> mDataset;
    private View v;
    private Villager head= new Villager();
    private String sign;
    private AdminController adminController = new AdminController();
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtNIK;
        public TextView txtName;
        public TextView txtRole;
        public LinearLayout btnInfo;
        public ImageView imgInfo;
        public Button btnVerifikasi;
        public MyViewHolder(View v) {
            super(v);
            txtNIK=(TextView)v.findViewById(R.id.txtNIK);
            txtName=(TextView)v.findViewById(R.id.txtName);
            txtRole=(TextView)v.findViewById(R.id.txtRole);
            btnInfo = (LinearLayout)v.findViewById(R.id.btnInfo);
            btnVerifikasi = (Button)v.findViewById(R.id.btnVerification);
            //imgInfo = (ImageView)v.findViewById(R.id.imgInfo);
        }
    }
    public VerificationAdapter(List<Users> myDataset, String sign) {
        mDataset = myDataset;
        this.sign = sign;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VerificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_verification_layout_item, parent, false);

        VerificationAdapter.MyViewHolder vh = new VerificationAdapter.MyViewHolder(v);
        return vh;
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2);
        view.startAnimation(anim);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final VerificationAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        setFadeAnimation(holder.itemView);
        holder.txtNIK.setText(mDataset.get(position).getNoKK());
        List<Villager> villagerList2 = new ArrayList<>();
        adminController.fetchMemberData(new OnGetDataListenerListVillager() {
            @Override
            public void onComplete(List<Villager> villagerList) {
                head = getFamilyHead(villagerList);
                holder.txtName.setText(head.getNik());
                holder.txtRole.setText(head.getName());
            }
        },mDataset.get(position).getNoKK());
        if(sign.equals("Verification"))
        {
            holder.btnVerifikasi.setText("Batal Verifikasi");
        }
        else if(sign.equals("NoVerification"))
        {
            holder.btnVerifikasi.setText("Verifikasi");
        }
        else if(sign.equals("Delete"))
        {
            holder.btnVerifikasi.setText("Hapus");
        }
        holder.btnVerifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sign.equals("Verification"))
                    adminController.cancelVerificationFamily(mDataset.get(position).getNoKK());
                else if(sign.equals("NoVerification"))
                    adminController.verificationFamily(mDataset.get(position).getNoKK());
                else if(sign.equals("Delete"))
                    adminController.deleteFamily(mDataset.get(position).getNoKK());
            }
        });
        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public Villager getFamilyHead(List<Villager> villagerList)
    {
        for(Villager v : villagerList)
        {
            if(v.getRole().equals("Kepala Keluarga"))
            {
                return  v;
            }
        }
        return null;
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}

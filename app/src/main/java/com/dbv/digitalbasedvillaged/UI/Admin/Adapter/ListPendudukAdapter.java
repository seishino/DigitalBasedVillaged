package com.dbv.digitalbasedvillaged.UI.Admin.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.dbv.digitalbasedvillaged.R;
import com.dbv.digitalbasedvillaged.Controller.AdminController;
import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.UI.Admin.ListPendudukLayout;
import com.dbv.digitalbasedvillaged.UI.Admin.ShowFamily;

import java.util.List;

public class ListPendudukAdapter  extends RecyclerView.Adapter<ListPendudukAdapter.MyViewHolder> {
    private List<Family> mDataset;
    private View view;
    private AdminController adminController = new AdminController();
    private ListPendudukLayout listPendudukLayout;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtNIK;
        public TextView txtName;
        public TextView txtRole;
        public LinearLayout btnInfo;
        public ImageView imgInfo;
        public MyViewHolder(View v) {
            super(v);
            txtNIK=(TextView)v.findViewById(R.id.txtNIK);
            txtName=(TextView)v.findViewById(R.id.txtName);
            txtRole=(TextView)v.findViewById(R.id.txtRole);
            btnInfo = (LinearLayout)v.findViewById(R.id.btnInfo);
            //imgInfo = (ImageView)v.findViewById(R.id.imgInfo);
        }
    }
    public ListPendudukAdapter(List<Family> myDataset, ListPendudukLayout listPendudukLayout) {
        mDataset = myDataset;
        this.listPendudukLayout = listPendudukLayout;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListPendudukAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_list_penduduk_layout_item, parent, false);

        ListPendudukAdapter.MyViewHolder vh = new ListPendudukAdapter.MyViewHolder(view);
        return vh;
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2);
        view.startAnimation(anim);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ListPendudukAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        setFadeAnimation(holder.itemView);
        holder.txtNIK.setText(mDataset.get(position).getNoKK());
        Villager head = getFamilyHead(mDataset.get(position).getFamilyList());
        holder.txtName.setText(head.getNik());
        holder.txtRole.setText(head.getName());

        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(v.getContext(),holder.btnInfo,Gravity.CENTER);
                popupMenu.getMenu().add("Reset Password");
                popupMenu.getMenu().add("Lihat Data Keluarga");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Reset Password"))
                        {
                            adminController.resetPassword(mDataset.get(position).getNoKK());
                            Toast.makeText(view.getContext(), "Reset Password Berhasil", Toast.LENGTH_SHORT).show();
                        }
                        else if(item.getTitle().equals("Lihat Data Keluarga"))
                        {
                            Intent i = new Intent(view.getContext(), ShowFamily.class);
                            i.putExtra("noKK",mDataset.get(position).getNoKK());
                            listPendudukLayout.startActivity(i);
                        }
                        return true;
                    }
                });
                popupMenu.show();
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

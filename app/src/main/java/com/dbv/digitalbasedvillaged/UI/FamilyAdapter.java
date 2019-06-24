package com.dbv.digitalbasedvillaged.UI;

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

import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.R;

import java.util.List;

public class FamilyAdapter  extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {
    private List<Villager> mDataset;
    private View v;
    private Register root;
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
    public FamilyAdapter(List<Villager> myDataset, Register root) {
        mDataset = myDataset;
        this.root = root;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FamilyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.villager_list_penduduk_layout_item, parent, false);

        FamilyAdapter.MyViewHolder vh = new FamilyAdapter.MyViewHolder(v);
        return vh;
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2);
        view.startAnimation(anim);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final FamilyAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        setFadeAnimation(holder.itemView);
        holder.txtNIK.setText(mDataset.get(position).getNik());
        holder.txtName.setText(mDataset.get(position).getName());
        holder.txtRole.setText(mDataset.get(position).getRole());

        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(v.getContext(),holder.btnInfo,Gravity.CENTER);
                popupMenu.getMenu().add("Hapus");
                popupMenu.getMenu().add("Ubah");
                popupMenu.getMenu().add("Lihat Data");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Hapus"))
                        {
                            root.remove(position,mDataset.get(position));
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


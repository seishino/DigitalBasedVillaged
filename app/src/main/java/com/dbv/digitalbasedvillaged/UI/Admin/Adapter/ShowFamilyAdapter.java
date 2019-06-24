package com.dbv.digitalbasedvillaged.UI.Admin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.dbv.digitalbasedvillaged.R;

import com.dbv.digitalbasedvillaged.Controller.VillagerController;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.UI.Admin.ShowFamily;

import java.util.List;

public class ShowFamilyAdapter  extends RecyclerView.Adapter<ShowFamilyAdapter.MyViewHolder> {
    private List<Villager> mDataset;
    private View v;
    private VillagerController villagerController = new VillagerController();
    private ShowFamily root;
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
    public ShowFamilyAdapter(List<Villager> myDataset, ShowFamily root) {
        mDataset = myDataset;
        this.root = root;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShowFamilyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.villager_list_penduduk_layout_item, parent, false);

        ShowFamilyAdapter.MyViewHolder vh = new ShowFamilyAdapter.MyViewHolder(v);
        return vh;
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2);
        view.startAnimation(anim);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ShowFamilyAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        setFadeAnimation(holder.itemView);
        holder.txtNIK.setText(mDataset.get(position).getNik());
        holder.txtName.setText(mDataset.get(position).getName());
        holder.txtRole.setText(mDataset.get(position).getRole());

        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData(mDataset.get(position));
            }
        });

    }
    public void showData(Villager villager)
    {
        View popupView = LayoutInflater.from(root).inflate(R.layout.show_villager_info,null);

        final PopupWindow pw = new PopupWindow(popupView, 1000, 1000, true);
        pw.setOutsideTouchable(false);
        final TextView txtNIK = (TextView)popupView.findViewById(R.id.txtNIK);
        final TextView txtName = (TextView)popupView.findViewById(R.id.txtName);
        final TextView txtRole = (TextView)popupView.findViewById(R.id.txtRole);
        final TextView txtJob = (TextView)popupView.findViewById(R.id.txtJob);
        final TextView txtBirth = (TextView)popupView.findViewById(R.id.txtBirth);
        final TextView txtGender = (TextView)popupView.findViewById(R.id.txtGender);

        txtNIK.setText(villager.getNik());
        txtName.setText(villager.getName());
        txtBirth.setText(villager.getPlaceOfBirth()+ ", "+ villager.getDateOfBirth());
        txtJob.setText(villager.getJob());
        txtRole.setText(villager.getRole());
        txtGender.setText(villager.getGender());
        pw.showAtLocation(popupView, Gravity.CENTER, 0, 0);
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

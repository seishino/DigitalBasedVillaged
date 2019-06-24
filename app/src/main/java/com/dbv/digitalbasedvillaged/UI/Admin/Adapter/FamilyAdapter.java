package com.dbv.digitalbasedvillaged.UI.Admin.Adapter;

import android.app.DatePickerDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerFamily;
import com.dbv.digitalbasedvillaged.R;
import com.dbv.digitalbasedvillaged.Controller.AdminController;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.UI.Admin.Profile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FamilyAdapter  extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {
    private List<Villager> mDataset;
    private View v;
    private AdminController adminController = new AdminController();
    private Profile root;
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
    public FamilyAdapter(List<Villager> myDataset, Profile root) {
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
                if(mDataset.size()>1)
                    popupMenu.getMenu().add("Hapus");
                popupMenu.getMenu().add("Ubah");
                popupMenu.getMenu().add("Lihat Data");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("Hapus"))
                        {
                            adminController.removeMember(mDataset.get(position).getNik());
                        }
                        else if(item.getTitle().equals("Ubah"))
                        {
                            updateData(mDataset.get(position));
                        }
                        else if(item.getTitle().equals("Lihat Data"))
                        {
                            showData(mDataset.get(position));
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
    public void updateData(final Villager villager)
    {
        View popupView = LayoutInflater.from(root.getContext()).inflate(R.layout.add_perorang,null);

        final PopupWindow pw = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        pw.setOutsideTouchable(false);
        final EditText txtNIK = (EditText)popupView.findViewById(R.id.txtNoKK);
        final EditText txtName = (EditText)popupView.findViewById(R.id.txtName);
        final EditText txtDate = (EditText)popupView.findViewById(R.id.txtDate);
        final EditText txtJob = (EditText)popupView.findViewById(R.id.txtJob);

        final Spinner spinnerPlace = (Spinner)popupView.findViewById(R.id.spinnerPlace);
        final Spinner spinnerGender = (Spinner)popupView.findViewById(R.id.spinnerGender);
        final Spinner txtRole = (Spinner)popupView.findViewById(R.id.spinnerRole);
        ImageButton btnBack = (ImageButton)popupView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        txtNIK.setText(villager.getNik());
        txtName.setText(villager.getName());
        txtDate.setText(villager.getDateOfBirth().replace("-","/"));
        txtJob.setText(villager.getJob());
        txtNIK.setEnabled(false);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mdiDialog =new DatePickerDialog(root.getContext(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear+1;
                        txtDate.setText(String.format("%02d",dayOfMonth)+"/"+String.format("%02d",month)+"/" +year);
                    }
                }, Integer.parseInt(villager.getDateOfBirth().split("-")[2]), Integer.parseInt(villager.getDateOfBirth().split("-")[1]), Integer.parseInt(villager.getDateOfBirth().split("-")[0]));
                mdiDialog.show();

            }
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(root.getContext(),
                R.array.spinner_gender, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter1);
        List<String> province = new ArrayList<>();
        province.add("Yogyakarta");
        province.add("Jawa Tengah");
        province.add("Jawa Timur");
        province.add("Jawa Barat");
        ArrayAdapter<String> adapterProvince = new ArrayAdapter<String>(root.getContext(),android.R.layout.simple_spinner_item,province);
        adapterProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlace.setAdapter(adapterProvince);
        List<String> role = new ArrayList<>();
        role.add("Kepala Keluarga");
        role.add("Ibu");
        role.add("Anak");
        ArrayAdapter<String> adapterRole = new ArrayAdapter<String>(root.getContext(),android.R.layout.simple_spinner_item,role);
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtRole.setAdapter(adapterRole);

        int roleId = adapterRole.getPosition(villager.getRole());
        txtRole.setSelection(roleId);
        int genderId = adapter1.getPosition(villager.getGender());
        spinnerGender.setSelection(genderId);
        int placeId = adapterProvince.getPosition(villager.getPlaceOfBirth());
        spinnerPlace.setSelection(placeId);
        Button btnTambah = (Button)popupView.findViewById(R.id.btnAdd);
        Button btnCancel = (Button)popupView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminController.checkVillager(new OnGetDataListenerFamily() {
                    @Override
                    public void onComplete(Family family) {
                        Pattern p = Pattern.compile("^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$");
                        Matcher m = p.matcher(txtDate.getText().toString());

                        if(!m.matches())
                        {
                            Toast.makeText(root.getContext(), "Tanggal tidak sesuai format", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(txtNIK.getText().toString().equals(""))
                        {
                            Toast.makeText(root.getContext(), "NIK tidak boleh kosong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(txtNIK.getText().length()!=17)
                        {
                            Toast.makeText(root.getContext(), "NIK harus 17 digit", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(txtName.getText().toString().equals(""))
                        {
                            Toast.makeText(root.getContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(txtDate.getText().toString().equals(""))
                        {
                            Toast.makeText(root.getContext(), "Tanggal tidak boleh kosong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(txtJob.getText().toString().equals(""))
                        {
                            Toast.makeText(root.getContext(), "Pekerjaan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Villager villager = new Villager();
                        villager.setNik(txtNIK.getText().toString());
                        villager.setName(txtName.getText().toString());
                        villager.setGender(spinnerGender.getSelectedItem().toString());
                        villager.setDateOfBirth(txtDate.getText().toString().replace("/","-"));
                        villager.setPlaceOfBirth(spinnerPlace.getSelectedItem().toString());
                        villager.setJob(txtJob.getText().toString());
                        villager.setRole(txtRole.getSelectedItem().toString());
                        for(Villager v : family.getFamilyList())
                        {
                            try
                            {
                                if(v.getRole().equals("Kepala Keluarga") && txtRole.getSelectedItem().toString().equals("Kepala Keluarga") && !v.getNik().equals(txtNIK.getText().toString()))
                                {
                                    Toast.makeText(root.getContext(), "Kepala keluarga sudah ada", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(v.getRole().equals("Ibu") && txtRole.getSelectedItem().toString().equals("Ibu")&& !v.getNik().equals(txtNIK.getText().toString()))
                                {
                                    Toast.makeText(root.getContext(), "Ibu sudah ada", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            catch (Exception e)
                            {}
                        }
                        adminController.updateData(villager);
                        pw.dismiss();
                    }
                    });

            }
        });
        pw.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
    public void showData(Villager villager)
    {
        View popupView = LayoutInflater.from(root.getContext()).inflate(R.layout.show_villager_info,null);

        final PopupWindow pw = new PopupWindow(popupView, 1000, LinearLayout.LayoutParams.WRAP_CONTENT, true);
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

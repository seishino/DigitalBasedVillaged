package com.dbv.digitalbasedvillaged.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.dbv.digitalbasedvillaged.Controller.VillagerController;
import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;
    private VillagerController villagerController = new VillagerController();
    private Register root;
    private List<Villager> villagerList = new ArrayList<>();
    private Button btnConfirmation;
    private Button btnAdd;
    private ImageButton btnBack;
    private  ArrayAdapter<String> adapterRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_family_member);
        recyclerView = (RecyclerView)findViewById(R.id.listFamily);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new FamilyAdapter(villagerList,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        root = this;
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnConfirmation = (Button)findViewById(R.id.btnConfirm);
        btnConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isThereHead(villagerList))
                {
                    Toast.makeText(root, "Harus ada kepala keluarga", Toast.LENGTH_SHORT).show();
                    return;
                }
                String noKK = getIntent().getStringExtra("NoKK");
                String RT = getIntent().getStringExtra("RT");
                String RW = getIntent().getStringExtra("RW");
                String address = getIntent().getStringExtra("Address");
                Family family = new Family();
                family.setNoKK(noKK);
                family.setAddress(address);
                family.setRw(RW);
                family.setRt(RT);
                family.setFamilyList(villagerList);
                villagerController.addData(family);
                Toast.makeText(root, "berhasil", Toast.LENGTH_SHORT).show();
                finishAffinity();
                startActivity(new Intent(root, Login.class));
            }
        });
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }
    public void updateList()
    {
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void addData()
    {
        View popupView = LayoutInflater.from(this).inflate(R.layout.add_perorang,null);

        final PopupWindow pw = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        pw.setOutsideTouchable(false);
        final EditText txtNIK = (EditText)popupView.findViewById(R.id.txtNoKK);
        final EditText txtName = (EditText)popupView.findViewById(R.id.txtName);
        final EditText txtDate = (EditText)popupView.findViewById(R.id.txtDate);
        final EditText txtJob =     (EditText)popupView.findViewById(R.id.txtJob);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mdiDialog =new DatePickerDialog(root,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear+1;
                        txtDate.setText(String.format("%02d",dayOfMonth)+"/"+String.format("%02d",month)+"/" +year);
                    }
                }, Calendar.getInstance().getTime().getYear(), Calendar.getInstance().getTime().getMonth(), Calendar.getInstance().getTime().getDate());
                mdiDialog.show();

            }
        });
        final Spinner spinnerPlace = (Spinner)popupView.findViewById(R.id.spinnerPlace);
        final Spinner spinnerGender = (Spinner)popupView.findViewById(R.id.spinnerGender);
        final Spinner txtRole = (Spinner)popupView.findViewById(R.id.spinnerRole);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(root,
                R.array.spinner_gender, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter1);
        List<String> province = new ArrayList<>();
        province.add("Yogyakarta");
        province.add("Jawa Tengah");
        province.add("Jawa Timur");
        province.add("Jawa Barat");
        ArrayAdapter<String> adapterProvince = new ArrayAdapter<String>(root,android.R.layout.simple_spinner_item,province);
        adapterProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlace.setAdapter(adapterProvince);
        final List<String> role = new ArrayList<>();
        role.add("Kepala Keluarga");
        role.add("Ibu");
        role.add("Anak");

        adapterRole = new ArrayAdapter<String>(root,android.R.layout.simple_spinner_item,role);
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtRole.setAdapter(adapterRole);
        txtRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adapterRole.getPosition("Ibu") == position)
                {
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(root,
                            R.array.spinner_gender_ibu, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerGender.setAdapter(adapter1);
                }
                else
                {
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(root,
                            R.array.spinner_gender, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerGender.setAdapter(adapter1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(isThereHead(villagerList))
        {
            adapterRole.remove("Kepala Keluarga");
        }
        if(isThereMother(villagerList))
        {
            adapterRole.remove("Ibu");
        }
        Button btnCancel = (Button)popupView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        ImageButton btnBack = (ImageButton)popupView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        Button btnTambah = (Button)popupView.findViewById(R.id.btnAdd);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern p = Pattern.compile("^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$");
                Matcher m = p.matcher(txtDate.getText().toString());

                if(!m.matches())
                {
                    Toast.makeText(root, "Tanggal tidak sesuai format", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                if(txtName.getText().toString().equals(""))
                {
                    Toast.makeText(root, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtDate.getText().toString().equals(""))
                {
                    Toast.makeText(root, "Tanggal tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtJob.getText().toString().equals(""))
                {
                    Toast.makeText(root, "Pekerjaan tidak boleh kosong", Toast.LENGTH_SHORT).show();
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
                root.villagerList.add(villager);
                updateList();
                pw.dismiss();
            }
        });
        pw.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
    public boolean isThereHead(List<Villager> villagerList)
    {
        for(Villager v : villagerList)
        {
            if(v.getRole().equals("Kepala Keluarga"))
            {
                return true;
            }
        }
        return false;
    }
    public boolean isThereMother(List<Villager> villagerList)
    {
        for(Villager v : villagerList)
        {
            if(v.getRole().equals("Ibu"))
            {
                return true;
            }
        }
        return false;
    }
    public void remove(int pos, Villager v)
    {
        villagerList.remove(pos);
        recyclerViewAdapter.notifyDataSetChanged();
        if(v.getRole().equals("Kepala Keluarga"))
        {
            adapterRole.add("Kepala Keluarga");
        }
        else if(v.getRole().equals("Ibu"))
        {
            adapterRole.add("Ibu");
        }
    }
}

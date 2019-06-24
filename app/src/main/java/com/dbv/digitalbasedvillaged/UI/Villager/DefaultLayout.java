package com.dbv.digitalbasedvillaged.UI.Villager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dbv.digitalbasedvillaged.Controller.VillagerController;
import com.dbv.digitalbasedvillaged.DAO.VillagerDataAccess;
import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerFamily;
import com.dbv.digitalbasedvillaged.R;
import com.dbv.digitalbasedvillaged.UI.Login;
import com.dbv.digitalbasedvillaged.UI.Villager.Adapter.FamilyAdapter;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class DefaultLayout extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;
    private VillagerController villagerController = new VillagerController();
    private DefaultLayout root;
    private List<Villager> villagerList = new ArrayList<>();
    private TextView txtNoKK;
    private TextView txtAddress;
    private TextView txtRT;
    private TextView txtRW;
    private ImageButton btnMore;
    private  ProgressDialog progressDialog;
    final String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.villager_default_layout);
        recyclerView = (RecyclerView)findViewById(R.id.listFamily);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        root = this;
        txtNoKK = (TextView)findViewById(R.id.txtNoKK);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        btnMore = (ImageButton)findViewById(R.id.btnInfo);
        progressDialog = new ProgressDialog(root);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCancelable(false);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(v.getContext(),btnMore,Gravity.CENTER);
                popupMenu.getMenu().add("Tambah Anggota");
                popupMenu.getMenu().add("Ubah Data Keluarga");
                popupMenu.getMenu().add("Buat QR Code");
                popupMenu.getMenu().add("Ganti Password");
                popupMenu.getMenu().add("Ganti Akun");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Tambah Anggota"))
                        {
                            addData();
                        }
                        else if(item.getTitle().equals("Ubah Data Keluarga"))
                        {
                            startActivity(new Intent(root,ChangeFamilyData.class));
                        }
                        else if(item.getTitle().equals("Buat QR Code"))
                        {
                            if (Build.VERSION.SDK_INT >= 23)
                            {
                                if (checkPermission())
                                {
                                    generateQRCode();

                                } else {
                                    requestPermission();
                                    generateQRCode();
                                }
                            }
                            else
                            {
                                generateQRCode();
                            }
                        }
                        else if(item.getTitle().equals("Ganti Akun"))
                        {
                            try
                            {
                                SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
                                sharedPreferences.edit().remove("username").commit();
                                sharedPreferences.edit().remove("role").commit();
                            }
                            catch (Exception e)
                            {}
                            finish();
                            startActivity(new Intent(root, Login.class));
                        }
                        else if(item.getTitle().equals("Ganti Password"))
                        {
                            startActivity(new Intent(root, ChangePassword.class));
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        getFamily();
    }
    public void getFamily()
    {
        progressDialog.show();
        villagerController.fetchDataVillager(new OnGetDataListenerFamily() {
            @Override
            public void onComplete(Family family) {
                txtNoKK.setText(family.getNoKK());
                txtAddress.setText(family.getAddress() + " / " + family.getRt() + " / " + family.getRw());
                root.villagerList = family.getFamilyList();
                recyclerViewAdapter = new FamilyAdapter(villagerList,root);
                recyclerView.setAdapter(recyclerViewAdapter);
                progressDialog.dismiss();
            }
        });
    }
    public void updateList()
    {
        recyclerViewAdapter.notifyDataSetChanged();
    }
    public void generateQRCode()
    {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        Bitmap bitmap;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        QRGEncoder qrgEncoder = new QRGEncoder(new VillagerDataAccess().getNik().toString(), null, QRGContents.Type.TEXT, smallerDimension);
        try {
            boolean save;
            String result;
            bitmap = qrgEncoder.encodeAsBitmap();
            save = QRGSaver.save(savePath, new VillagerDataAccess().getNik(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
            result = save ? "Image Saved" : "Image Not Saved";
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        } catch (WriterException e) {
            Toast.makeText(root, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(root, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(root, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(root, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(root, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

    }
    public void addData()
    {
        View popupView = LayoutInflater.from(this).inflate(R.layout.add_perorang,null);

        final PopupWindow pw = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        pw.setOutsideTouchable(false);
        final EditText txtNIK = (EditText)popupView.findViewById(R.id.txtNoKK);
        final EditText txtName = (EditText)popupView.findViewById(R.id.txtName);
        final EditText txtDate = (EditText)popupView.findViewById(R.id.txtDate);
        final EditText txtJob = (EditText)popupView.findViewById(R.id.txtJob);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mdiDialog =new DatePickerDialog(root,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear+1;
                        txtDate.setText(String.format("%02d",dayOfMonth)+"/"+String.format("%02d",month)+"/" +year);
                    }
                }, 2000, Calendar.getInstance().getTime().getMonth(), Calendar.getInstance().getTime().getDate());
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
        List<String> role = new ArrayList<>();
        role.add("Kepala Keluarga");
        role.add("Ibu");
        role.add("Anak");
        ArrayAdapter<String> adapterRole = new ArrayAdapter<String>(root,android.R.layout.simple_spinner_item,role);
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtRole.setAdapter(adapterRole);
        ImageButton btnBack = (ImageButton)popupView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
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
                villagerController.checkVillager(new OnGetDataListenerFamily() {
                    @Override
                    public void onComplete(Family family) {

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
                        final Villager villager = new Villager();
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
                                if(v.getRole().equals("Kepala Keluarga") && txtRole.getSelectedItem().toString().equals("Kepala Keluarga"))
                                {
                                    Toast.makeText(root, "Kepala keluarga sudah ada", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(v.getRole().equals("Ibu") && txtRole.getSelectedItem().toString().equals("Ibu"))
                                {
                                    Toast.makeText(root, "Ibu sudah ada", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(v.getNik().equals(txtNIK.getText().toString()))
                                {
                                    Toast.makeText(root, "NIK sudah ada", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            catch (Exception e)
                            {}
                        }
                        villagerController.addMember(villager);
                        pw.dismiss();

                    }
                });
            }
        });

        pw.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
}

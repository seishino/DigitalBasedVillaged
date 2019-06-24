package com.dbv.digitalbasedvillaged.UI.Admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.dbv.digitalbasedvillaged.Controller.AdminController;
import com.dbv.digitalbasedvillaged.DAO.AdminDataAccess;
import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerFamily;
import com.dbv.digitalbasedvillaged.UI.Admin.Adapter.TabAdapterAdmin;
import com.dbv.digitalbasedvillaged.UI.Login;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import com.dbv.digitalbasedvillaged.R;
public class DefaultLayout  extends AppCompatActivity implements Profile.OnFragmentInteractionListener, ListPendudukLayout.OnFragmentInteractionListener, VerificationLayout.OnFragmentInteractionListener, View.OnClickListener{
    public static TabAdapterAdmin pagerAdapter;
    public static ViewPager viewPager;
    public ImageButton btnMore;
    public DefaultLayout root;
    public View v;
    private AdminController adminController = new AdminController();
    final String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private IntentIntegrator intentIntegrator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_layout_admin);
        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Verifikasi"));
        tabLayout.addTab(tabLayout.newTab().setText("Keluarga"));
        tabLayout.addTab(tabLayout.newTab().setText("Daftar Penduduk"));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorWhite));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        root=this;
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        pagerAdapter = new TabAdapterAdmin(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        btnMore = (ImageButton)findViewById(R.id.btnInfo);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu(v.getContext(),btnMore,Gravity.CENTER);
                popupMenu.getMenu().add("Tambah Anggota");
                popupMenu.getMenu().add("Ubah Data Keluarga");
                popupMenu.getMenu().add("Buat QR Code");
                popupMenu.getMenu().add("Scan QR Code");

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
                        else if(item.getTitle().equals("Scan QR Code"))
                        {
                            intentIntegrator = new IntentIntegrator(root);
                            intentIntegrator.initiateScan();
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

    }
    public void scan()
    {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{

                try{
                    Intent i = new Intent(root,ShowFamily.class);
                    i.putExtra("noKK",result.getContents());
                    startActivity(i);


                }catch (Exception e){
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        // inisialisasi IntentIntegrator(scanQR)
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
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
        QRGEncoder qrgEncoder = new QRGEncoder(new AdminDataAccess().getNik().toString(), null, QRGContents.Type.TEXT, smallerDimension);
        try {
            boolean save;
            String result;
            bitmap = qrgEncoder.encodeAsBitmap();
            save = QRGSaver.save(savePath, new AdminDataAccess().getNik(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
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
        View popupView = LayoutInflater.from(root).inflate(R.layout.add_perorang,null);

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
                adminController.checkVillager(new OnGetDataListenerFamily() {
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
                        adminController.addMember(villager);
                        pw.dismiss();
                    }
                });
            }
        });

        pw.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

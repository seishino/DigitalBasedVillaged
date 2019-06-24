package com.dbv.digitalbasedvillaged;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class AddDataVillager extends AppCompatActivity{
    Map<String,Map<String,Map<String,String>>> dataKeluarga = new HashMap<>();
    Map<String,Map<String,String>> childDataKeluarga = new HashMap<>();

    List<String> listNoKK = new ArrayList<>();
    List<String> listNama = new ArrayList<>();
    final String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    EditText editTextKK;
    Button btnTambah;
    ListView listAnggota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_villager);
        editTextKK = (EditText)findViewById(R.id.editText2);
        btnTambah = (Button)findViewById(R.id.button2);
        listAnggota = (ListView)findViewById(R.id.listAnggota);
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
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) AddDataVillager.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.add_perorang,
                        (ViewGroup) findViewById(R.id.popup));
                final PopupWindow pw = new PopupWindow(layout, 800, 1300, true);
                final EditText editTextNama= (EditText)layout.findViewById(R.id.txtNama);
                final EditText editTextKK = (EditText)layout.findViewById(R.id.txtNoKK);
                final EditText editTextUmur = (EditText)layout.findViewById(R.id.txtUmur);
                final Spinner spinnerGender = (Spinner)layout.findViewById(R.id.spinnerGender);

                Button btnAdd = (Button)layout.findViewById(R.id.btnAdd);
                pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(layout.getContext(),
                        R.array.spinner_gender, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerGender.setAdapter(adapter1);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,String> dataPerorang = new HashMap<>();
                        dataPerorang.put("Nama",editTextNama.getText().toString());
                        dataPerorang.put("Gender",spinnerGender.getSelectedItem().toString());
                        dataPerorang.put("Umur",editTextUmur.getText().toString());
                        childDataKeluarga.put(editTextKK.getText().toString(),dataPerorang);
                        listNoKK.add(editTextKK.getText().toString());
                        listNama.add(editTextNama.getText().toString());
                        updateListView();
                        pw.dismiss();

                    }
                });


            }
        });


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AddDataVillager.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddDataVillager.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(AddDataVillager.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddDataVillager.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

    }
    public void generateQRCode()
    {
        Button btn = (Button)findViewById(R.id.button4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = db.getReference();
                Map<String,Map<String,Map<String,String>>> dataKeluarga = new HashMap<>();
                Map<String,Map<String,String>> childDataKeluarga = new HashMap<>();
                Map<String,String> dataPerOrang = new HashMap<>();
                myRef.child("Penduduk").child(editTextKK.getText().toString());
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                Bitmap bitmap;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;
                QRGEncoder qrgEncoder = new QRGEncoder(editTextKK.getText().toString(), null, QRGContents.Type.TEXT, smallerDimension);
                try {
                    boolean save;
                    String result;
                    bitmap = qrgEncoder.encodeAsBitmap();
                    save = QRGSaver.save(savePath, editTextKK.getText().toString(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    result = save ? "Image Saved" : "Image Not Saved";
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    saveData();
                } catch (WriterException e) {
                    Toast.makeText(AddDataVillager.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void saveData()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference();
        dataKeluarga.put(editTextKK.getText().toString(),childDataKeluarga);
        for(String noIndukKK : dataKeluarga.keySet())
        {
            for(String noKKAnggota : dataKeluarga.get(noIndukKK).keySet())
            {
                myRef.child("Penduduk").child(noIndukKK).child(noKKAnggota).setValue(dataKeluarga.get(noIndukKK).get(noKKAnggota));
            }
        }
        //myRef.child("Penduduk").setValue(dataKeluarga);
        listNama.clear();
        listNoKK.clear();
        updateListView();
        Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
    }
    public void updateListView()
    {
        ListAnggotaAdapter listAnggotaAdapter = new ListAnggotaAdapter(AddDataVillager.this, listNoKK,listNama);
        listAnggota.setAdapter(listAnggotaAdapter);
        Toast.makeText(this, String.format("%d",listNama.size()), Toast.LENGTH_SHORT).show();
        listAnggota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //final String value = (String)parent.getItemAtPosition(position);

            }
        });
    }
}

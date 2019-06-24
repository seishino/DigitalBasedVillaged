package com.dbv.digitalbasedvillaged.UI.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.dbv.digitalbasedvillaged.Controller.AdminController;
import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerFamily;
import com.dbv.digitalbasedvillaged.UI.Admin.Adapter.FamilyAdapter;
import com.dbv.digitalbasedvillaged.R;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;
    private View view;
    private Profile root;
    private List<Villager> villagerList = new ArrayList<>();
    private TextView txtNoKK;
    private TextView txtAddress;
    private TextView txtRT;
    private TextView txtRW;
    private ImageButton btnMore;
    private AdminController adminController = new AdminController();
    private ProgressDialog progressDialog;
    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.admin_profile_layout, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.listFamily);


        root = this;
        layoutManager = new LinearLayoutManager(root.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        txtNoKK = (TextView)view.findViewById(R.id.txtNoKK);
        txtAddress = (TextView)view.findViewById(R.id.txtAddress);
        progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCancelable(false);
        getFamily();
        return view;
    }
    public void getFamily()
    {
        progressDialog.show();
        adminController.fetchDataAdmin(new OnGetDataListenerFamily() {
            @Override
            public void onComplete(Family family) {
                txtNoKK.setText(family.getNoKK());
                txtAddress.setText(family.getAddress() + " / " + family.getRt() + " / " + family.getRw());

                villagerList = family.getFamilyList();
                recyclerViewAdapter = new FamilyAdapter(family.getFamilyList(),root);
                recyclerView.setAdapter(recyclerViewAdapter);
                progressDialog.dismiss();
            }
        });
    }
    public void updateList()
    {
        recyclerViewAdapter.notifyDataSetChanged();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

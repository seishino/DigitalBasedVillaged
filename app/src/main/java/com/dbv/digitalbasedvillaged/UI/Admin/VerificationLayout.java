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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.dbv.digitalbasedvillaged.R;
import com.dbv.digitalbasedvillaged.Controller.AdminController;
import com.dbv.digitalbasedvillaged.Entity.Users;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerListUser;
import com.dbv.digitalbasedvillaged.UI.Admin.Adapter.VerificationAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VerificationLayout.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VerificationLayout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerificationLayout extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;
    private View view;
    private Spinner btnFilter;
    private AdminController adminController = new AdminController();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;
    private OnFragmentInteractionListener mListener;

    public VerificationLayout() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerificationLayout.
     */
    // TODO: Rename and change types and number of parameters
    public static VerificationLayout newInstance(String param1, String param2) {
        VerificationLayout fragment = new VerificationLayout();
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
        view = inflater.inflate(R.layout.admin_verification_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.listFamily);
        layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        btnFilter = (Spinner) view.findViewById(R.id.btnFilter);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(view.getContext(),
                R.array.spinner_verification, R.layout.transparent_text);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btnFilter.setAdapter(adapter1);
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCancelable(false);
        btnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    getAllFamily();
                else if(position==1)
                    getCancelVerification();
                else
                    getDeleteFamily();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
    public void getAllFamily()
    {
        progressDialog.show();
        adminController.fetchVerificationDataAdmin(new OnGetDataListenerListUser() {
            @Override
            public void onComplete(List<Users> usersList) {
                recyclerViewAdapter = new VerificationAdapter(usersList,"NoVerification");
                recyclerView.setAdapter(recyclerViewAdapter);
                progressDialog.dismiss();
            }
        });
    }
    public void getCancelVerification()
    {
        progressDialog.show();
        adminController.fetchCancelVerification(new OnGetDataListenerListUser() {
            @Override
            public void onComplete(List<Users> usersList) {
                recyclerViewAdapter = new VerificationAdapter(usersList,"Verification");
                recyclerView.setAdapter(recyclerViewAdapter);
                progressDialog.dismiss();
            }
        });
    }
    public void getDeleteFamily()
    {
        progressDialog.show();
        adminController.fetchVerificationDataAdmin(new OnGetDataListenerListUser() {
            @Override
            public void onComplete(List<Users> usersList) {
                recyclerViewAdapter = new VerificationAdapter(usersList,"Delete");
                recyclerView.setAdapter(recyclerViewAdapter);
                progressDialog.dismiss();
            }
        });
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

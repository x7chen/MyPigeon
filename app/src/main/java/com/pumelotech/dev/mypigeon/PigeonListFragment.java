package com.pumelotech.dev.mypigeon;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PigeonListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PigeonListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PigeonListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PigeonListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PigeonListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PigeonListFragment newInstance(String param1, String param2) {
        PigeonListFragment fragment = new PigeonListFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fly, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pigeon_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView_pigeon);
        final PigeonListAdapter mPigeonListAdapter = new PigeonListAdapter(getActivity());
        listView.setAdapter(mPigeonListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PigeonInfo pigeon = mPigeonListAdapter.getPigeon(position);
                if (pigeon == null) return;
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                intent.putExtra("name", pigeon.getName());
                startActivity(intent);
            }
        });
        String dbName = "MyPigeonDB.db";
        PigeonDB pigeonDB = new PigeonDB(getActivity(),dbName,null,1);
        SQLiteDatabase db = pigeonDB.getWritableDatabase();
        db.beginTransaction();
        for (int i = 0; i < 1; i++) {
            String name = "信使" + i;

            db.execSQL("Insert into PigeonTable (name,shed_id,owner_id,) values('信使',1,5)");
        }
        db.setTransactionSuccessful();
        db.close();
        for (int i = 0; i < 10; i++) {
            PigeonInfo pigeon = new PigeonInfo();
            pigeon.setName("依人" + (i + 1) + "号");
            pigeon.setID("10121" + i);
            if (i > 6)
                pigeon.setStatus("FLY");
            mPigeonListAdapter.addPigeon(pigeon);
        }
        for (int i = 0; i < 10; i++) {
            PigeonInfo pigeon = new PigeonInfo();
            pigeon.setName("高飞" + (i + 1) + "号");
            pigeon.setID("13121" + i);
            mPigeonListAdapter.addPigeon(pigeon);
        }
        mPigeonListAdapter.notifyDataSetChanged();
        // Inflate the layout for this fragment
        return view;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package py.fpuna.tesis.qoetest.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.activity.PreTestActivty;
import py.fpuna.tesis.qoetest.activity.WebTestIntroActivity;
import py.fpuna.tesis.qoetest.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TestFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private int mParam1;
    public static final String EXISTE_SHARED = "EXISTE_SHARED";

    private OnFragmentInteractionListener mListener;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(int param1) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }

        // Se abre el Shaerd Preferences
        mPrefs = getActivity().getSharedPreferences(Constants.SAHRED_PREFERENCES,
                Context.MODE_PRIVATE);
        // Se Obtiene el editor del
        mEditor = mPrefs.edit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test,
                container, false);
        Button comenzarTest = (Button) view.findViewById(R.id
                .startBtn);
        comenzarTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPrefs.contains(EXISTE_SHARED)) {
                    Intent intent = new Intent(getActivity(), WebTestIntroActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), PreTestActivty.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int number) {
        if (mListener != null) {
            mListener.onFragmentInteraction(number);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mListener.onFragmentInteraction(getArguments().getInt(ARG_PARAM1));
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
        public void onFragmentInteraction(int number);
    }

}

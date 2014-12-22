package py.fpuna.tesis.qoetest.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import py.fpuna.tesis.qoetest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebIntroDosFragment extends Fragment {


    public WebIntroDosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_intro_dos, container, false);
    }

    public static WebIntroDosFragment newInstance() {
        WebIntroDosFragment f = new WebIntroDosFragment();
        return f;
    }


}

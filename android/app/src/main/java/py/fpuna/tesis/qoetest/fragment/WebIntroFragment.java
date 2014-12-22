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
public class WebIntroFragment extends Fragment {


    public WebIntroFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_web_intro, container, false);
    }

    public static WebIntroFragment newInstance() {
        WebIntroFragment f = new WebIntroFragment();
        return f;
    }

}

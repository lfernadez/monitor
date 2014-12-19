package py.fpuna.tesis.qoetest.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.PhoneInfo;
import py.fpuna.tesis.qoetest.utils.DeviceInfoUtils;
import py.fpuna.tesis.qoetest.utils.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private int mParam1;

    Context context;

    private TextView deviceInfoTextView;
    private TextView networkInfoTextView;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(int position) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
        context = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deviceInfoTextView = (TextView) view.findViewById(R.id.device_info_text);
        networkInfoTextView = (TextView) view.findViewById(R.id.operator_info_text);

        /* Datos del telefono */
        DeviceInfoUtils infoUtils = new DeviceInfoUtils(getActivity()
                .getApplicationContext());
        String model = infoUtils.getDeviceModel();           //Modelo
        String manufacturer = infoUtils.getManufacturer();   //Vendedor
        String osVersion = infoUtils.getOSVersion();         //OS Version

        /* Datos de la pantalla del telefono */
        String str_ScreenSize = "Tamaño de pantalla : " + infoUtils.getScreenSize();

        NetworkUtils nu = new NetworkUtils(context);

        /* Datos de red del operador */
        String networkOperator = nu.getOperatorName();   //Nombre del Operador
        String activeNetwork = nu.getActiveNetworkType();
        PhoneInfo info = infoUtils.getPhoneInfo();
        String allNetwork = "";
        List<Map<String, String>> allNetworks = nu.getAllNetworkState();
        for (Map<String, String> networkItem : allNetworks) {
            for (String key : networkItem.keySet()) {
                allNetwork += key + " ESTADO: " + (String) networkItem.get(key)
                        + "\n";
            }
        }

        deviceInfoTextView.setText("Modelo: " + model + '\n'
                + "Vendedor: " + manufacturer + '\n' + str_ScreenSize + '\n'
                + "Versión SO: " +  osVersion + '\n' + "RAM: " + info.getRam() + '\n'
                + "Procesador: " + info.getCpuModel()  + '\n' + "Num Cores: " + info
                .getCpuCores()
                + '\n' + "Frecuencia: " + info.getCpuFrec()) ;

        networkInfoTextView.setText("Operador: " + networkOperator + '\n' + "Redes " +
                "disponibles: "
                + '\n' + allNetwork + '\n' + "Red Activa: " + activeNetwork);

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(int number);
    }

}

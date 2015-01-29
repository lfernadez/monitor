package py.fpuna.tesis.qoetest.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.fragment.InfoFragment;
import py.fpuna.tesis.qoetest.fragment.NavigationDrawerFragment;
import py.fpuna.tesis.qoetest.fragment.TestFragment;
import py.fpuna.tesis.qoetest.services.CPUMonitoringService;
import py.fpuna.tesis.qoetest.services.MonitoringService;
import py.fpuna.tesis.qoetest.services.NetworkMonitoringService;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.PreferenceUtils;

public class PrincipalActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        InfoFragment.OnFragmentInteractionListener,
        TestFragment.OnClickStartButtonListener,
        TestFragment.OnFragmentInteractionListener {

    public static final String EXISTE_SHARED = "EXISTE_SHARED";
    public static final String TAG = "PrincipalActivity";

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PreferenceUtils preferenceUtils;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        preferenceUtils = new PreferenceUtils(this);

        // Se abre el Shaerd Preferences
        mPrefs = getSharedPreferences(Constants.SAHRED_PREFERENCES,
                Context.MODE_PRIVATE);

        /*try {
            new Database(getBaseContext()).createDataBase(getBaseContext());
		} catch (IOException e) {
				e.printStackTrace();
		}*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        /* Verificacion de conexion */
        /*ConnectivityManager manager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);

        Boolean isWifi = manager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        Boolean is3g = manager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(!isWifi && !is3g){
            ConnectionErrorDialogFragment cedf = new
                    ConnectionErrorDialogFragment();
            cedf.show(getSupportFragmentManager(), "TAG");

        }*/

        /* Se inician los servicios de monitoreo */
        Intent intent = new Intent(getApplicationContext(), MonitoringService.class);
        startService(intent);
        Intent intentNetworkService = new Intent(getApplicationContext(),
                NetworkMonitoringService.class);
        startService(intentNetworkService);

        Intent intentCPUService = new Intent(getApplicationContext(), CPUMonitoringService.class);
        startService(intentCPUService);

        initIperf();
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TestFragment.newInstance(position + 1))
                        .addToBackStack("Principal")
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, InfoFragment.newInstance(position + 1))
                        .addToBackStack("Principal")
                        .commit();
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[0];
                break;
            case 2:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[1];
                break;
            case 3:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[2];
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.principal, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(int number) {
        switch (number) {
            case 1:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[0];
                break;
            case 2:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[1];
                break;
            case 3:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[2];
                break;
        }
    }

    @Override
    public void onClickStartButton() {
        if (mPrefs.contains(EXISTE_SHARED)) {
            Intent intent = new Intent(getApplicationContext(),
                    EmocionTestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PreTestActivty.class);
            startActivity(intent);
        }
    }

    public void initIperf() {
        InputStream in;
        try {
            //The asset "iperf" (from assets folder) inside the activity is opened for reading.
            in = getResources().getAssets().open(Constants.IPERF_FILE_NAME);
        } catch (IOException e2) {
            return;
        }
        try {
            //Checks if the file already exists, if not copies it.
            new FileInputStream(Constants.IPERF_BINARY_DIC);
        } catch (FileNotFoundException e1) {
            try {
                //The file named "iperf" is created in a system designated folder for this application.
                OutputStream out = new FileOutputStream(Constants.IPERF_BINARY_DIC, false);
                // Transfer bytes from "in" to "out"
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                //After the copy operation is finished, we give execute permissions to the "iperf" executable using shell commands.
                Process processChmod = Runtime.getRuntime().exec
                        ("/system/bin/chmod 744 " + Constants.IPERF_BINARY_DIC);
                // Executes the command and waits untill it finishes.
                processChmod.waitFor();
            } catch (IOException e) {
                return;
            } catch (InterruptedException e) {
                return;
            }

            return;
        }
        return;
    }



    public class ConnectionErrorDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity
                    ());
            builder.setMessage("No posee conexión, actívela")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().finish();
                        }
                    });
            return builder.create();

        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()){
            mNavigationDrawerFragment.closeDrawer();
        }else{
            finish();
        }

    }
}

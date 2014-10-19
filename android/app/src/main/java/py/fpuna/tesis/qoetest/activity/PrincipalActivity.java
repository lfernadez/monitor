package py.fpuna.tesis.qoetest.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.fragment.InfoFragment;
import py.fpuna.tesis.qoetest.fragment.NavigationDrawerFragment;
import py.fpuna.tesis.qoetest.fragment.TestFragment;
import py.fpuna.tesis.qoetest.services.MonitoringService;
import py.fpuna.tesis.qoetest.services.NetworkMonitoringService;

public class PrincipalActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        InfoFragment.OnFragmentInteractionListener,
        TestFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

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


        Intent intent = new Intent(getApplicationContext(), MonitoringService.class);
        startService(intent);
        Intent intentNetworkService = new Intent(getApplicationContext(),
                NetworkMonitoringService.class);
        startService(intentNetworkService);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TestFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, InfoFragment.newInstance(position + 1))
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
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
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


}

package tw.com.vip_tjob;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class OperActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, BaseAppCompatActivity.LoginInputListener {

    private Button btn_Login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_open_register);
        //btn_Login.setVisibility(View.INVISIBLE);
        btn_Login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.oper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_open_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.btn_login) {
            FragmentManager manager = getSupportFragmentManager();
            LoginDialogFragment dialog = (LoginDialogFragment) manager
                    .findFragmentByTag("tag");
            if (dialog == null) {
                dialog = new LoginDialogFragment();
                Bundle args = new Bundle();
                args.putString("message", "Message");
                dialog.setArguments(args);
                dialog.show(manager, "tag");


            }
        }


        //FragmentManager manager = getSupportFragmentManager();
        //Fragment fragment = new RegisterFragment();

        //FragmentTransaction transaction = manager.beginTransaction();
        //transaction.add(R.id.operActivity, fragment);
        //transaction.commit();
        //Log.d("Hello!", "Run..");
    }

    @Override
    public void onLoginInputComplete(String username, String password) {
        Toast.makeText(getApplicationContext(), username + ":" + password, Toast.LENGTH_SHORT).show();
    }

    public static class RegisterFragment extends Fragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_register, container, false);
            return v;
        }
    }
}

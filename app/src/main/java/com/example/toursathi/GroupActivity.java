package com.example.toursathi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.toursathi.Fragment.AlbumFragment;
import com.example.toursathi.Fragment.ChatFragment;
import com.example.toursathi.Fragment.LocationFragment;
import com.example.toursathi.Fragment.SplitwiseFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GroupActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    FragmentManager fragmentManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        fragmentManager=getSupportFragmentManager();
        BottomNavigationView bottomNavigationView =findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadfragment(new LocationFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.locationmenu:
                loadfragment(new LocationFragment());
                break;
            case R.id.albummenu:
                loadfragment(new AlbumFragment());
                break;
            case R.id.chatmenu:
                loadfragment(new ChatFragment());
                break;
            case R.id.splitwisemenu:
                loadfragment(new SplitwiseFragment());
                break;
        }
        return false;
    }


    public  boolean loadfragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentcontainer,fragment)
                    //.commitAllowingStateLoss();
                    .commit();
            return true;
        }
        return false;
    }
}

package com.zac4j.zwallet.view;

import android.databinding.DataBindingUtil;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.zac4j.zwallet.R;
import com.zac4j.zwallet.databinding.ActivityMainBinding;
import com.zac4j.zwallet.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{

  private DrawerLayout mDrawerLayout;
  private NavigationView mNavigationView;
  private MainViewModel mViewModel;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    mViewModel = new MainViewModel(this);
    binding.setViewModel(mViewModel);
    setupActionBar(binding);
    setupDrawer(binding);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mViewModel.destroy();
  }

  private void setupActionBar(ActivityMainBinding binding) {
    setSupportActionBar(binding.toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle("");
    }
  }

  private void setupDrawer(ActivityMainBinding binding) {
    mDrawerLayout = binding.drawerMainLayout;
    mNavigationView = binding.drawerMainNavView;
    mNavigationView.setNavigationItemSelectedListener(this);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onNavigationItemSelected(MenuItem item) {
    mNavigationView.setCheckedItem(item.getItemId());
    mDrawerLayout.closeDrawers();
    return true;
  }

}
package github.tornaco.xposedmoduletest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.newstand.logger.Logger;

import java.util.List;

import github.tornaco.xposedmoduletest.R;
import github.tornaco.xposedmoduletest.bean.PackageInfo;
import github.tornaco.xposedmoduletest.loader.PackageLoader;
import github.tornaco.xposedmoduletest.service.AppService;
import github.tornaco.xposedmoduletest.ui.adapter.AppListAdapter;
import github.tornaco.xposedmoduletest.ui.widget.SwitchBar;
import github.tornaco.xposedmoduletest.x.XExecutor;
import github.tornaco.xposedmoduletest.x.XSettings;

public class GuardAppNavActivity extends AppCompatActivity {

    protected FloatingActionButton fab;

    private SwipeRefreshLayout swipeRefreshLayout;

    protected AppListAdapter appListAdapter;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        initView();
        startLoading();
    }

    protected int getLayoutRes() {
        return R.layout.app_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLoading();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    protected void initView() {
        MobileAds.initialize(this, "ca-app-pub-5643935235734454~8615411999");
        mAdView = (AdView) findViewById(R.id.adView);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.polluted_waves));
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuardAppNavActivity.this, GuardAppPickerActivity.class));
            }
        });

        appListAdapter = onCreateAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(appListAdapter);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading();
            }
        });

        final XSettings xSettings = XSettings.get();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SwitchBar switchBar = (SwitchBar) findViewById(R.id.switchbar);
                if (switchBar == null) return;
                switchBar.setChecked(xSettings.enabled(getApplicationContext()));
                switchBar.addOnSwitchChangeListener(new SwitchBar.OnSwitchChangeListener() {
                    @Override
                    public void onSwitchChanged(SwitchCompat switchView, boolean isChecked) {
                        Logger.d("onSwitchChanged:" + isChecked);
                        xSettings.setEnabled(getApplicationContext(), isChecked);
                    }
                });
                switchBar.show();
            }
        });
    }

    protected AppListAdapter onCreateAdapter() {
        return new AppListAdapter(this) {
            @Override
            protected void onPackageRemoved() {
                super.onPackageRemoved();
                startLoading();
                startService(new Intent(GuardAppNavActivity.this, AppService.class));
            }
        };
    }

    private void startLoading() {
        swipeRefreshLayout.setRefreshing(true);
        XExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<PackageInfo> res = performLoading();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        appListAdapter.update(res);
                    }
                });
            }
        });
    }

    protected List<PackageInfo> performLoading() {
        return PackageLoader.Impl.create(this).loadStoredGuarded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}

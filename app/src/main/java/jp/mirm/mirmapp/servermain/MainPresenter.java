package jp.mirm.mirmapp.servermain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.ads.reward.RewardItem;
import jp.mirm.mirmapp.backup.BackupFragment;
import jp.mirm.mirmapp.console.ConsoleFragment;
import jp.mirm.mirmapp.servercontroller.ControllerFragment;
import jp.mirm.mirmapp.data.task.GoServerTask;
import jp.mirm.mirmapp.data.task.RewardTask;
import jp.mirm.mirmapp.setting.SettingFragment;
import jp.mirm.mirmapp.utils.*;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.serverlist.ListActivity;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.data.source.remote.PublicAPI;
import jp.mirm.mirmapp.share.ShareDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    MainPresenter(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void initialize() {
        MainActivity activity = (MainActivity) view.getActivity();

        Toolbar toolbar = view.getActivity().findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        DrawerLayout drawer = view.getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = view.getActivity().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(activity);

        View headerView = navigationView.getHeaderView(0);

        TextView nameView = headerView.findViewById(R.id.serverName);
        nameView.setText(MiRmAPI.getServerId());

        TextView urlView = headerView.findViewById(R.id.urlView);
        urlView.setText(URLHolder.serverDomain + "/" + MiRmAPI.getServerId());

        navigationView.setCheckedItem(R.id.nav_control);
        changeFragmentWithoutStack(new ControllerFragment());

        if (Utils.getFileSize(new File(Environment.getExternalStorageDirectory() + "/servers/" + MiRmAPI.getServerId() + "/console.log")) > 300) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getActivity())
                    .setTitle(R.string.static_message_notice)
                    .setMessage(R.string.activity_main_dialog_message_filesize_description)
                    .setPositiveButton(R.string.static_button_back, null);
            builder.show();
        }
    }

    @Override
    public void selectOptionItem(int index) {
        final MainActivity activity = (MainActivity) view.getActivity();
        switch (index) {
            case R.id.action_share: {
                ShareDialog dialog = new ShareDialog(activity);
                dialog.showDialog();
                break;
            }

            case R.id.action_goserver: {
                GoServerTask task = new GoServerTask();
                task.execute();
                break;
            }

            case R.id.action_login: {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                        .setTitle(R.string.static_message_notice)
                        .setMessage(R.string.dialog_logout_description)
                        .setPositiveButton(R.string.static_button_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MiRmAPI.logout();
                                Intent intent = new Intent(activity, ListActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        })
                        .setNegativeButton(R.string.static_button_no, null);
                dialog.show();
                break;
            }

            case R.id.action_browser: {
                PublicAPI.openBrowser(view.getActivity(), URLHolder.serverDomain, "");
                break;
            }
        }
    }

    @Override
    public void selectItem(int index) {
        MainActivity activity = (MainActivity) view.getActivity();

        switch (index) {
            case R.id.nav_console:
                changeFragment(new ConsoleFragment());
                break;

            case R.id.nav_control:
                changeFragment(new ControllerFragment());
                break;

            case R.id.nav_backup:
                changeFragment(new BackupFragment());
                break;

            /*case R.id.nav_rewards:
                openRewardDialog();
                break;*/

            case R.id.nav_settings:
                changeFragment(new SettingFragment());
                break;

            case R.id.nav_h2play:
                PublicAPI.openBrowser(activity, URLHolder.URL_HOWTOUSE, null);
                break;

            case R.id.nav_site:
                PublicAPI.openBrowser(activity, URLHolder.URL_MAINPAGE, null);
                break;

            case R.id.nav_twitter:
                PublicAPI.openBrowser(activity, "https://twitter.com/MiRm_mcpe", null);
                break;

            case R.id.nav_ftp:
                PublicAPI.openBrowser(activity, "https://ftp.mirm.jp/elfinder.hani.html#elf_l1_Lw", null);
                break;
        }

        try {
            DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = view.getActivity().findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            view.onSuperOnBackPressed();
        }
    }

    @Override
    public void changeFragment(Fragment fragment) {
        MainActivity activity = (MainActivity) view.getActivity();
        FragmentTransaction transaction = activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void changeFragmentWithoutStack(Fragment fragment) {
        MainActivity activity = (MainActivity) view.getActivity();
        FragmentTransaction transaction = activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void openRewardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getActivity())
                .setTitle(R.string.activity_main_dialog_message_reward_title)
                .setNegativeButton(R.string.static_button_back, null)
                .setNeutralButton(R.string.activity_main_dialog_button_reward_about, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PublicAPI.openBrowser(view.getActivity(), URLHolder.URL_REWARD_DESCRIPTION, "");
                    }
                });

        Property property = new Property(MiRmAPI.getServerId());
        long time = property.getNextReward();

        if (time <= System.currentTimeMillis()) {
            builder.setMessage(R.string.activity_main_dialog_message_description);
            builder.setPositiveButton(R.string.static_button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AdPlayer player = new AdPlayer(AdHolder.expireRewardId, true) {
                        @Override
                        public void onAdRewarded(RewardItem reward) {
                            RewardTask task = new RewardTask(view.getActivity());
                            task.execute();
                        }
                    }.initialize(view.getActivity());
                    player.load();
                }
            });

        } else {
            Date date = new Date(time);
            builder.setMessage(view.getActivity().getResources().getString(R.string.toast_reward_failure_bytime, new SimpleDateFormat("yyyy MM/dd HH:mm").format(date)));
        }

        builder.show();
    }
}

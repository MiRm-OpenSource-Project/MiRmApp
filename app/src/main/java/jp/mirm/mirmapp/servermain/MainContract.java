package jp.mirm.mirmapp.servermain;

import android.app.Activity;
import android.support.v4.app.Fragment;

public interface MainContract {

    interface View {
        Activity getActivity();
        void onSuperOnBackPressed();
    }

    interface Presenter {
        void initialize();
        void selectItem(int index);
        void selectOptionItem(int index);
        void onBackPressed();
        void changeFragment(Fragment fragment);
        void changeFragmentWithoutStack(Fragment fragment);
        void openRewardDialog();
    }
}
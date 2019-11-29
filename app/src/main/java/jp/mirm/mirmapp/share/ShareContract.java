package jp.mirm.mirmapp.share;

import android.app.Activity;

public interface ShareContract {

    interface View {
        Activity getActivity();
    }

    interface Presenter {
        void openTwitter();
        void createLink();
        void createQRCode();
    }

}

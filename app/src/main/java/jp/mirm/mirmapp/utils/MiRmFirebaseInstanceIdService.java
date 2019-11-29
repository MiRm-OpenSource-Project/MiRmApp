package jp.mirm.mirmapp.utils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import jp.mirm.mirmapp.R;

public class MiRmFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String token = Utils.encrypt(refreshedToken, getResources().getString(R.string.data_salt));
        Utils.writeFile("FCMToken.dat", token);
    }

}

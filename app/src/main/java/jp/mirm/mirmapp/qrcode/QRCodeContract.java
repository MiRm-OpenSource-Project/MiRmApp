package jp.mirm.mirmapp.qrcode;

import android.app.Activity;
import android.graphics.Bitmap;

public interface QRCodeContract {

    interface View {
        Activity getActivity();
        void setQRCodeImage(Bitmap bitmap);
        void setServerId(String serverId);
        void setPort(String port);
        void setLoading(boolean bool);
    }

    interface Presenter {
        void initialize();
        void setQRCodeImage(Bitmap bitmap);
        void setServerId(String serverId);
        void setPort(String port);
        void saveQRCodeImage(Bitmap bitmap);
        void setLoading(boolean bool);
    }
}

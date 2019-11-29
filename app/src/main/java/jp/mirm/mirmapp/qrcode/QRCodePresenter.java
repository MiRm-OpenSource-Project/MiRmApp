package jp.mirm.mirmapp.qrcode;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.data.task.CreateQRCodeTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QRCodePresenter implements QRCodeContract.Presenter {

    private final QRCodeContract.View view;

    QRCodePresenter(QRCodeContract.View view) {
        this.view = view;
    }

    @Override
    public void initialize() {
        CreateQRCodeTask task = new CreateQRCodeTask(this);
        task.execute();
    }

    @Override
    public void saveQRCodeImage(Bitmap bitmap) {
        try {
            if (bitmap == null) {
                Toast.makeText(view.getActivity(), R.string.toast_qrcode_failure_save, Toast.LENGTH_LONG).show();
                return;
            }

            String fileName = MiRmAPI.getServerId() + new SimpleDateFormat("_yyyyMMddHHmmss").format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_DCIM, fileName);
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            Toast.makeText(view.getActivity(), view.getActivity().getResources().getString(R.string.toast_qrcode_success_save, fileName), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(view.getActivity(), R.string.toast_qrcode_failure_save, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setQRCodeImage(Bitmap bitmap) {
        view.setQRCodeImage(bitmap);
    }

    @Override
    public void setServerId(String serverId) {
        view.setServerId(serverId);
    }

    @Override
    public void setPort(String port) {
        view.setPort(port);
    }

    @Override
    public void setLoading(boolean bool) {
        view.setLoading(bool);
    }
}

package jp.mirm.mirmapp.data.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.qrcode.QRCodePresenter;
import jp.mirm.mirmapp.utils.URLHolder;

public class CreateQRCodeTask extends AsyncTask<Void, Void, Integer> {

    private final QRCodePresenter presenter;

    public CreateQRCodeTask(QRCodePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPreExecute() {
        presenter.setLoading(true);
    }

    @Override
    protected Integer doInBackground(Void... args) {
        return MiRmAPI.getPort();
    }

    @Override
    protected void onPostExecute(Integer port) {
        String shareURL = "minecraft://?addExternalServer=" + URLHolder.serverPublicURL + ":" + port;

        try {
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.encodeBitmap(shareURL, BarcodeFormat.QR_CODE, 400, 400);
            presenter.setQRCodeImage(bitmap);
            presenter.setPort(String.valueOf(port));
            presenter.setServerId(MiRmAPI.getServerId());
            presenter.setLoading(false);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}

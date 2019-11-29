package jp.mirm.mirmapp.qrcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import jp.mirm.mirmapp.R;

public class QRCodeActivity extends AppCompatActivity implements QRCodeContract.View {

    private QRCodePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        this.presenter = new QRCodePresenter(this);

        presenter.initialize();

        findViewById(R.id.qrCodeSaveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView = findViewById(R.id.qrCodeImage);
                presenter.saveQRCodeImage(((BitmapDrawable) imageView.getDrawable()).getBitmap());
            }
        });

        findViewById(R.id.qrCodeCloseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRCodeActivity.this.finish();
            }
        });
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setQRCodeImage(Bitmap bitmap) {
        ImageView view = findViewById(R.id.qrCodeImage);
        view.setImageBitmap(bitmap);
    }

    @Override
    public void setServerId(String serverId) {
        TextView view = findViewById(R.id.qrCodeServerId);
        view.setText(getResources().getString(R.string.activity_qrcode_message_serverid, serverId));
    }

    @Override
    public void setPort(String port) {
        TextView view = findViewById(R.id.qrCodePort);
        view.setText(getResources().getString(R.string.activity_qrcode_message_port, port));
    }

    @Override
    public void setLoading(boolean bool) {
        ProgressBar bar = findViewById(R.id.qrCodeLoading);
        bar.setIndeterminate(bool);
        if (bool) {
            bar.setVisibility(View.VISIBLE);
        } else {
            bar.setVisibility(View.GONE);
        }
    }
}

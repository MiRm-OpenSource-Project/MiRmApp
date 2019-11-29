package jp.mirm.mirmapp.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.share.SharePresenter;
import jp.mirm.mirmapp.share.ShareContract;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ShareDialog implements ShareContract.View {

    private final Activity activity;
    private AlertDialog.Builder dialog;

    private final SharePresenter presenter;

    public ShareDialog(Activity activity) {
        this.activity = activity;
        this.presenter = new SharePresenter(this);
    }

    public void showDialog() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_share, (ViewGroup) activity.findViewById(R.id.shareLayout));

        dialog = new AlertDialog.Builder(activity);

        layout.findViewById(R.id.share_twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openTwitter();
            }
        });

        layout.findViewById(R.id.share_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.createLink();
            }
        });

        layout.findViewById(R.id.share_qrcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.createQRCode();
            }
        });

        dialog.setNegativeButton(R.string.static_button_back, null);
        dialog.setView(layout);
        dialog.setTitle(R.string.dialog_share_message_title);

        AlertDialog dialogToShow = dialog.create();

        WindowManager.LayoutParams wmlp = dialogToShow.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;

        dialogToShow.getWindow().setAttributes(wmlp);
        dialogToShow.show();
    }

    @Override
    public Activity getActivity() {
        return this.activity;
    }
}

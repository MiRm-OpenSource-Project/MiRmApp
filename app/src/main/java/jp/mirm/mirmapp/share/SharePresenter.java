package jp.mirm.mirmapp.share;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.data.task.CreateLinkTask;
import jp.mirm.mirmapp.qrcode.QRCodeActivity;

import static jp.mirm.mirmapp.utils.URLHolder.serverPublicURL;

public class SharePresenter implements ShareContract.Presenter {

    private ShareContract.View view;

    SharePresenter(ShareContract.View view) {
        this.view = view;
    }

    @Override
    public void openTwitter() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            String shareURL = "minecraft://?addExternalServer=" + MiRmAPI.getServerId() + "|" + serverPublicURL;

            String message = Uri.encode(view.getActivity().getResources().getString(R.string.dialog_share_message_twitter_share, MiRmAPI.getServerId(), shareURL, "https://play.google.com/store/apps/details?id=jp.mirm.mirmapp"));

            intent.setData(Uri.parse("twitter://post?message=" + message));
            view.getActivity().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(view.getActivity(), R.string.toast_twitter_notfound, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void createLink() {
        CreateLinkTask task = new CreateLinkTask();
        task.execute();
    }

    @Override
    public void createQRCode() {
        Intent intent = new Intent(view.getActivity(), QRCodeActivity.class);
        view.getActivity().startActivity(intent);
    }
}

package jp.mirm.mirmapp.data.source.remote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import jp.mirm.mirmapp.web.WebActivity;

public class PublicAPI {

    public static void openBrowser(Context context, String url, String endPoint) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("endPoint", endPoint);

        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}

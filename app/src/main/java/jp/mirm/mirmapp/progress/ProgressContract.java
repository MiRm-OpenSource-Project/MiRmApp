package jp.mirm.mirmapp.progress;

public interface ProgressContract {

    interface View {
        ProgressDialog create();
        ProgressDialog show();
        void dismiss();
        ProgressDialog setText(String text);
        ProgressDialog setText(int redId);
    }

    interface Presenter {

    }

}

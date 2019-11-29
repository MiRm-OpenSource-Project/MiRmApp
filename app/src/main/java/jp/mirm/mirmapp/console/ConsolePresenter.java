package jp.mirm.mirmapp.console;

import android.app.Activity;
import android.os.Environment;
import android.view.inputmethod.InputMethodManager;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.data.task.ConsoleTask;
import jp.mirm.mirmapp.data.task.SendCommandTask;
import jp.mirm.mirmapp.utils.ConsoleTextFormatter;
import jp.mirm.mirmapp.utils.Utils;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsolePresenter implements ConsoleContract.Presenter {

    private final ConsoleContract.View view;
    private final ConsoleTask task;
    private final SendCommandTask commandTask;
    private final ConcurrentLinkedQueue<String> commandQueue;
    private boolean inited = false;

    ConsolePresenter(ConsoleContract.View view) {
        this.view = view;
        this.task = new ConsoleTask(view);
        this.commandTask = new SendCommandTask(this);
        this.commandQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean sendCommand(String command) {
        view.append("\n> " + command);

        if (!command.isEmpty()) {
            addCommandQueue(command);
            view.clearCommandArea();
        } else {
            view.scrollToEnd();
        }
        return true;
    }

    @Override
    public void initialize() {
        if (!inited) {
            inited = true;

            String text = Utils.readFile(Environment.getExternalStorageDirectory().getPath() + "/servers/" + MiRmAPI.getServerId() + "/console.log");
            if (text != null) {
                view.append(ConsoleTextFormatter.formatToSpanned(text));
            }

            task.execute();
            commandTask.execute();
        }
        view.scrollToEnd();
    }

    @Override
    public void onClickConsole() {
        try {
            ((InputMethodManager) view.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException e) {}
    }

    @Override
    public void onDestroyFragment() {
        task.cancel(true);
        commandTask.cancel(true);
    }

    @Override
    public void addCommandQueue(String command) {
        commandQueue.add(command);
    }

    @Override
    public String pollCommand() {
        return commandQueue.poll();
    }
}

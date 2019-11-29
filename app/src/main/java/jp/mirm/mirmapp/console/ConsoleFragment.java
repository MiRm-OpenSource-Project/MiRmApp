package jp.mirm.mirmapp.console;

import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import jp.mirm.mirmapp.R;

public class ConsoleFragment extends Fragment implements ConsoleContract.View {

    private ConsolePresenter presenter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_console, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.presenter = new ConsolePresenter(this);
        this.view = view;

        presenter.initialize();

        final EditText commandField = view.findViewById(R.id.commandTextField);

        commandField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().endsWith("\n")) {
                    commandField.setText(commandField.getText().toString().replaceAll("\n", ""));
                    String command = commandField.getText().toString();
                    presenter.sendCommand(command);
                }
            }
        });

        final TextView console = view.findViewById(R.id.consoleTextView);
        console.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onClickConsole();
            }
        });

    }

    @Override
    public void initConsole(String text) {
        TextView textView = view.findViewById(R.id.consoleTextView);
        textView.setText(text);
    }

    @Override
    public void append(CharSequence text) {
        TextView textView = view.findViewById(R.id.consoleTextView);
        textView.append(text);
    }

    @Override
    public void clearConsole() {
        TextView textView = view.findViewById(R.id.consoleTextView);
        textView.setText("");
    }

    @Override
    public void scrollToEnd() {
        final ScrollView scrollView = view.findViewById(R.id.consoleScrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void clearCommandArea() {
        EditText commandField = view.findViewById(R.id.commandTextField);
        commandField.setText("");
    }

    @Override
    public IBinder getWindowToken() {
        TextView textView = view.findViewById(R.id.consoleTextView);
        return textView.getWindowToken();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyFragment();
    }
}

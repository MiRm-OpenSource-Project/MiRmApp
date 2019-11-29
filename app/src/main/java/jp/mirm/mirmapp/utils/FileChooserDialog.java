package jp.mirm.mirmapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import jp.mirm.mirmapp.R;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public abstract class FileChooserDialog {

    private static final String PARENT_DIR = "..";

    private final Activity activity;
    private AlertDialog.Builder dialog;
    private ListView list;
    private TextView textView;

    private FileSelectedListener fileListener;

    private File currentPath;
    private File selectedFile;
    private String extension = null;

    public abstract void onSelected(File selectedFile);

    public abstract void onMakeFolder(File selectedFile);

    public FileChooserDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_filechooser, (ViewGroup) activity.findViewById(R.id.chooserRoot));

        list = layout.findViewById(R.id.fileList);
        textView = layout.findViewById(R.id.pathArea);
        dialog = new AlertDialog.Builder(activity);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int which, long id) {
                String fileChosen = (String) list.getItemAtPosition(which);
                File chosenFile = getChosenFile(fileChosen);
                selectedFile = chosenFile;
                if (chosenFile.isDirectory()) {
                    refresh(chosenFile);
                } else {
                    if (fileListener != null) {
                        fileListener.fileSelected(chosenFile);
                    }
                }
            }
        });

        dialog.setPositiveButton(R.string.dialog_filechooser_button_select, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onSelected(selectedFile);
            }
        });

        dialog.setNeutralButton(R.string.dialog_filechooser_button_new, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onMakeFolder(selectedFile);
            }
        });

        dialog.setNegativeButton(R.string.static_button_back, null);
        dialog.setView(layout);
        dialog.setTitle(R.string.dialog_filechooser_message_title);

        refresh(selectedFile = Environment.getExternalStorageDirectory());

        dialog.create();

        dialog.show();
    }

    public void refresh(File path) {
        try {
            this.currentPath = path;

            if (path.exists()) {

                File[] dirs = path.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return (file.isDirectory() && file.canRead());
                    }
                });

                File[] files = path.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        if (!file.isDirectory()) {
                            if (!file.canRead()) {
                                return false;
                            } else if (extension == null) {
                                return true;
                            } else {
                                return file.getName().toLowerCase().endsWith(extension);
                            }
                        } else {
                            return false;
                        }
                    }
                });

                // convert to an array
                int i = 0;
                String[] fileList;
                if (path.getParentFile() == null) {
                    fileList = new String[dirs.length + files.length];
                } else {
                    fileList = new String[dirs.length + files.length + 1];
                    fileList[i++] = PARENT_DIR;
                }

                Arrays.sort(dirs);
                Arrays.sort(files);
                for (File dir : dirs) {
                    fileList[i++] = dir.getName();
                }
                for (File file : files) {
                    fileList[i++] = file.getName();
                }

                // refresh the user interface
                setPathText(currentPath.getPath());

                list.setAdapter(new ArrayAdapter(activity,
                        android.R.layout.simple_list_item_1, fileList) {
                    @Override
                    public View getView(int pos, View view, ViewGroup parent) {
                        view = super.getView(pos, view, parent);
                        ((TextView) view).setSingleLine(true);
                        return view;
                    }
                });
            }
        } catch (Exception e) {

        }
    }

    private void setPathText(String path) {
        textView.setText(path);
    }


    private File getChosenFile(String fileChosen) {
        if (fileChosen.equals(PARENT_DIR)) {
            return currentPath.getParentFile();
        } else {
            return new File(currentPath, fileChosen);
        }
    }

    public interface FileSelectedListener {
        void fileSelected(File file);
    }

}

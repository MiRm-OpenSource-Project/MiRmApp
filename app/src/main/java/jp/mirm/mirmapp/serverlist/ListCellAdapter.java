package jp.mirm.mirmapp.serverlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import jp.mirm.mirmapp.R;

import java.util.List;

public class ListCellAdapter extends ArrayAdapter<ListCellItem> {

    private int resource;
    private List<ListCellItem> items;
    private LayoutInflater inflater;

    private ListPresenter presenter;

    public ListCellAdapter(ListPresenter presenter, @NonNull Context context, int resource, List<ListCellItem> items) {
        super(context, resource, items);

        this.resource = resource;
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.presenter = presenter;
    }

    @Override
    public @NonNull View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(resource, null);
        }

        final ListCellItem item = items.get(position);

        TextView serverId = view.findViewById(R.id.listCellServerId);
        serverId.setText(item.getServerId());

        TextView date = view.findViewById(R.id.listCellDate);
        date.setText(item.getDate());

        view.findViewById(R.id.listCellManageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.startManagement(item.getServerId());
            }
        });

        view.findViewById(R.id.listCellDeleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.disconnectServer(item.getServerId());
            }
        });

        return view;
    }
}

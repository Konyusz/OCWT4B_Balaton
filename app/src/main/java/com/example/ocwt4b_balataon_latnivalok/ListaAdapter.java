package com.example.ocwt4b_balataon_latnivalok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListaAdapter extends ArrayAdapter<Latvanyossag> {

    private List<Latvanyossag> originalList;
    private List<Latvanyossag> filteredList;
    private Context context;

    public ListaAdapter(Context context, List<Latvanyossag> lista) {
        super(context, 0, new ArrayList<>(lista));
        this.context = context;
        this.originalList = new ArrayList<>(lista);
        this.filteredList = lista;
    }

    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            text = text.toLowerCase();
            for (Latvanyossag item : originalList) {
                if (item.getNev().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        clear();
        addAll(filteredList);
        notifyDataSetChanged();
    }

    @Override
    public Latvanyossag getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Latvanyossag latvanyossag = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView nevText = convertView.findViewById(android.R.id.text1);
        nevText.setText(latvanyossag.getNev());

        return convertView;
    }
}

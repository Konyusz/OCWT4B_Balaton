package com.example.ocwt4b_balataon_latnivalok;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private ArrayList<String> nevek;
    private ArrayList<String> eredetiNevek;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        nevek = getIntent().getStringArrayListExtra("latvanyossagok_nevek");
        if (nevek == null) nevek = new ArrayList<>();
        eredetiNevek = new ArrayList<>(nevek);

        ListView listView = findViewById(R.id.listViewLatvanyossagok);
        EditText searchInput = findViewById(R.id.editTextSearch);
        Spinner spinnerSort = findViewById(R.id.spinnerSort);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nevek);
        listView.setAdapter(adapter);

        // Spinner listener
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilterAndSort(searchInput.getText().toString(), position);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Search listener
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = spinnerSort.getSelectedItemPosition();
                applyFilterAndSort(s.toString(), pos);
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = adapter.getItem(position);
            int originalIndex = eredetiNevek.indexOf(selected);
            Intent result = new Intent();
            result.putExtra("kivalasztott_pozicio", originalIndex);
            setResult(RESULT_OK, result);
            finish();
        });
    }

    private void applyFilterAndSort(String filter, int sortOption) {
        // 1) Filter
        List<String> temp = new ArrayList<>();
        for (String name : eredetiNevek) {
            if (name.toLowerCase().contains(filter.toLowerCase())) {
                temp.add(name);
            }
        }

        // 2) Sort
        switch (sortOption) {
            case 0: // Név A-Z
                Collections.sort(temp);
                break;
            case 1: // Név Z-A
                Collections.sort(temp, Collections.reverseOrder());
                break;
            case 2:

                break;
        }


        nevek.clear();
        nevek.addAll(temp);
        adapter.notifyDataSetChanged();
    }
}
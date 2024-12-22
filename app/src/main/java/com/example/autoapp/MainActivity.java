package com.example.autoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AutoAdapter autoAdapter;
    private List<Auto> autoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);

        autoList = new ArrayList<>();
        loadCarsFromJson(); // Загружаем данные из JSON

        autoAdapter = new AutoAdapter(autoList, (auto, position) -> openEditAutoDialog(auto, position));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(autoAdapter);

        setupSortSpinner(); // Настройка сортировки
        setupFilterSpinner(); // Настройка фильтрации

        fab.setOnClickListener(v -> openAddAutoDialog());
    }

    private void loadCarsFromJson() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.cars);
            InputStreamReader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Auto>>() {}.getType();
            autoList = gson.fromJson(reader, listType);

            reader.close();
            inputStream.close();
        } catch (Exception e) {
            Toast.makeText(this, "Не удалось загрузить данные", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSortSpinner() {
        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupFilterSpinner() {
        Spinner filterSpinner = findViewById(R.id.filterSpinner);
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(this,
                R.array.producers, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProducer = parent.getItemAtPosition(position).toString();
                filterByProducer(selectedProducer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterByProducer(String producer) {
        if (producer.equals("Все")) {
            autoAdapter.updateList(autoList);
        } else {
            List<Auto> filteredList = new ArrayList<>();
            for (Auto auto : autoList) {
                if (auto.getProducer().equals(producer)) {
                    filteredList.add(auto);
                }
            }
            autoAdapter.updateList(filteredList);
        }
    }

    private void sortList(int option) {
        switch (option) {
            case 0: // По возрастанию цены
                Collections.sort(autoList, Comparator.comparingInt(Auto::getPrice));
                break;
            case 1: // По убыванию цены
                Collections.sort(autoList, (a1, a2) -> Integer.compare(a2.getPrice(), a1.getPrice()));
                break;
        }
        autoAdapter.notifyDataSetChanged();
    }


    private void openAddAutoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить автомобиль");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_auto, null);
        EditText producerEditText = view.findViewById(R.id.producerEditText);
        EditText modelEditText = view.findViewById(R.id.modelEditText);
        EditText priceEditText = view.findViewById(R.id.priceEditText);
        EditText imageUrlEditText = view.findViewById(R.id.imageUrlEditText);

        builder.setView(view);

        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String producer = producerEditText.getText().toString().trim();
            String model = modelEditText.getText().toString().trim();
            String priceText = priceEditText.getText().toString().trim();
            String imageUrl = imageUrlEditText.getText().toString().trim();

            if (!producer.isEmpty() && !model.isEmpty() && !priceText.isEmpty() && !imageUrl.isEmpty()) {
                try {
                    int price = Integer.parseInt(priceText);
                    Auto newAuto = new Auto(producer, model, price, imageUrl);
                    autoList.add(newAuto);
                    autoAdapter.notifyItemInserted(autoList.size() - 1);
                    Toast.makeText(this, "Автомобиль добавлен", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Неверная цена", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void openEditAutoDialog(Auto auto, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Редактировать автомобиль");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_auto, null);
        EditText producerEditText = view.findViewById(R.id.producerEditText);
        EditText modelEditText = view.findViewById(R.id.modelEditText);
        EditText priceEditText = view.findViewById(R.id.priceEditText);
        EditText imageUrlEditText = view.findViewById(R.id.imageUrlEditText);

        producerEditText.setText(auto.getProducer());
        modelEditText.setText(auto.getModel());
        priceEditText.setText(String.valueOf(auto.getPrice()));
        imageUrlEditText.setText(auto.getImageUrl());

        builder.setView(view);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String producer = producerEditText.getText().toString().trim();
            String model = modelEditText.getText().toString().trim();
            String priceText = priceEditText.getText().toString().trim();
            String imageUrl = imageUrlEditText.getText().toString().trim();

            if (!producer.isEmpty() && !model.isEmpty() && !priceText.isEmpty() && !imageUrl.isEmpty()) {
                try {
                    int price = Integer.parseInt(priceText);

                    auto.setProducer(producer);
                    auto.setModel(model);
                    auto.setPrice(price);
                    auto.setImageUrl(imageUrl);

                    autoList.set(position, auto);
                    autoAdapter.notifyItemChanged(position);
                    Toast.makeText(this, "Данные обновлены", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Неверная цена", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}

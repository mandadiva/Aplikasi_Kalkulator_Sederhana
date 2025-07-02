package com.example.kalkulator_sederhana;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDisplay;
    private TextView textViewResult;
    private ListView listViewHistory;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private Button buttonAdd, buttonSubtract, buttonMultiply, buttonDivide, buttonEquals, buttonClear;
    private ArrayList<String> historyList;
    private ArrayAdapter<String> historyAdapter;
    private String currentInput = "";
    private String currentOperator = "";
    private int num1 = 0;
    private boolean isFirstInput = true;
    private boolean hasCalculated = false; // Tambahkan flag ini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi elemen UI
        editTextDisplay = findViewById(R.id.editTextDisplay);
        textViewResult = findViewById(R.id.textViewResult);
        listViewHistory = findViewById(R.id.listViewHistory);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonMultiply = findViewById(R.id.buttonMultiply);
        buttonDivide = findViewById(R.id.buttonDivide);
        buttonEquals = findViewById(R.id.buttonEquals);
        buttonClear = findViewById(R.id.buttonClear);

        // Inisialisasi ListView
        historyList = new ArrayList<>();
        historyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        listViewHistory.setAdapter(historyAdapter);

        // Set OnClickListener untuk tombol angka
        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if (isFirstInput || hasCalculated) { // Use hasCalculated here
                    currentInput = button.getText().toString();
                    isFirstInput = false;
                    hasCalculated = false; // Reset flag
                    Log.d("NumberClick", "First Input: currentInput = " + currentInput);
                } else {
                    currentInput += button.getText();
                    Log.d("NumberClick", "Append Input: currentInput = " + currentInput);
                }
                editTextDisplay.setText(currentInput);
            }
        };
        button0.setOnClickListener(numberClickListener);
        button1.setOnClickListener(numberClickListener);
        button2.setOnClickListener(numberClickListener);
        button3.setOnClickListener(numberClickListener);
        button4.setOnClickListener(numberClickListener);
        button5.setOnClickListener(numberClickListener);
        button6.setOnClickListener(numberClickListener);
        button7.setOnClickListener(numberClickListener);
        button8.setOnClickListener(numberClickListener);
        button9.setOnClickListener(numberClickListener);

        // Set OnClickListener untuk tombol operator
        View.OnClickListener operatorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.isEmpty() && !hasCalculated) { // Check hasCalculated
                    num1 = Integer.parseInt(currentInput);
                    isFirstInput = true;
                    hasCalculated = false; // Reset
                    Button button = (Button) v;
                    currentOperator = button.getText().toString();
                    editTextDisplay.setText(currentOperator);
                    currentInput = "";
                    Log.d("OperatorClick", "Operator: " + currentOperator + ", num1 = " + num1);
                } else if (hasCalculated) {
                    isFirstInput = true;
                    hasCalculated = false;
                    Button button = (Button) v;
                    currentOperator = button.getText().toString();
                    editTextDisplay.setText(currentOperator);
                    currentInput = "";
                    Log.d("OperatorClick", "Operator: " + currentOperator + ", num1 = " + num1);
                }
            }
        };
        buttonAdd.setOnClickListener(operatorClickListener);
        buttonSubtract.setOnClickListener(operatorClickListener);
        buttonMultiply.setOnClickListener(operatorClickListener);
        buttonDivide.setOnClickListener(operatorClickListener);

        // Set OnClickListener untuk tombol sama dengan
        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });

        // Set OnClickListener untuk tombol clear
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentInput = "";
                currentOperator = "";
                num1 = 0;
                isFirstInput = true;
                hasCalculated = false; // Reset
                editTextDisplay.setText("0");
                textViewResult.setText(R.string.hasil);
                Log.d("ClearClick", "Cleared!");
            }
        });
    }

    private void calculate() {
        if (!currentInput.isEmpty() && !currentOperator.isEmpty()) {
            try {
                int num2 = Integer.parseInt(currentInput);
                int result = 0;
                String expression = num1 + " " + currentOperator + " " + num2;

                Log.d("Calculate", "num1 = " + num1 + ", num2 = " + num2 + ", operator = " + currentOperator);

                switch (currentOperator) {
                    case "+":
                        result = num1 + num2;
                        break;
                    case "-":
                        result = num1 - num2;
                        break;
                    case "*":
                        result = num1 * num2;
                        break;
                    case "/":
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            Toast.makeText(this, R.string.tidak_dapat_dibagi_nol, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                }
                Log.d("Calculate", "Result: " + result);
                String resultText = expression + " = " + result;
                textViewResult.setText(getString(R.string.hasil) + result);
                historyList.add(resultText);
                historyAdapter.notifyDataSetChanged();

                // Reset untuk perhitungan selanjutnya
                currentInput = String.valueOf(result);
                currentOperator = "";
                num1 = result;
                isFirstInput = true;
                hasCalculated = true; // Set this flag
                editTextDisplay.setText(String.valueOf(result));

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
                currentInput = "";
                editTextDisplay.setText("0");
                isFirstInput = true;
                hasCalculated = false;
            }
        } else if (currentInput.isEmpty() && currentOperator.isEmpty()) {
            return;
        } else if (currentInput.isEmpty()) {
            return;
        } else if (currentOperator.isEmpty()) {
            return;
        }
    }
}

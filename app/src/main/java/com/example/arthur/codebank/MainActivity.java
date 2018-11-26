package com.example.arthur.codebank;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.example.arthur.codebank.databasehelper.DBHelp;

import net.sqlcipher.database.SQLiteDatabase;

import static com.example.arthur.codebank.AesAlgorithm.decrypt;
import static com.example.arthur.codebank.AesAlgorithm.encrypt;

public class MainActivity extends AppCompatActivity {
    private Button btnAdd;
    private Button btnUpdate;
    private Button btnDelete;

    private Button encBtn;
    private Button decBtn;
    private EditText encryptionKeyBox;
    private String temporaryString;


    private EditText passwordInputBox;

    private ListView listPasswords;

    private String savePassword = ""; // To save current password -> update or delete.


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase.loadLibs(this);

        listPasswords = findViewById(R.id.ListPasswords);
        listPasswords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) listPasswords.getItemAtPosition(i);
                passwordInputBox.setText(item);
                savePassword = item;
                temporaryString = item;
            }
        });
        passwordInputBox = findViewById(R.id.PasswordInputBox);
        btnAdd = findViewById(R.id.BtnAdd);
        btnUpdate = findViewById(R.id.BtnUpdate);
        btnDelete = findViewById(R.id.BtnDelete);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String boxtext = passwordInputBox.getText().toString();
                if( !boxtext.isEmpty() && !boxtext.trim().isEmpty()){
                    DBHelp.getInstance(MainActivity.this).insertNewCode(passwordInputBox.getText().toString());
                    reloadCodes();
                }else{

                    Toast.makeText(getApplicationContext(), "No password to add!",Toast.LENGTH_SHORT).show();
                }

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelp.getInstance(MainActivity.this).updateCode(savePassword,passwordInputBox.getText().toString());
                reloadCodes();

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelp.getInstance(MainActivity.this).deleteCode(passwordInputBox.getText().toString());
                reloadCodes();

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        reloadCodes();

        //Encryption and decryption

        encBtn = findViewById(R.id.EncBtn);
        decBtn = findViewById(R.id.DecBtn);
        encryptionKeyBox = findViewById(R.id.EncryptionKeyBox);

        encBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    temporaryString = encrypt(passwordInputBox.getText().toString(),encryptionKeyBox.getText().toString());
                    passwordInputBox.setText(temporaryString);
                    Toast.makeText(getApplicationContext(), "Encryption successful!!!",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Encryption FAILED",Toast.LENGTH_SHORT).show();
                }

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    temporaryString = decrypt(temporaryString,encryptionKeyBox.getText().toString());
                    passwordInputBox.setText(temporaryString);
                    Toast.makeText(getApplicationContext(), "Decryption successful!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Decryption FAILED",Toast.LENGTH_SHORT).show();
                }

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });



    }

    private void reloadCodes(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                DBHelp.getInstance(this).getAllCodes());
        listPasswords.setAdapter(adapter);
    }




}

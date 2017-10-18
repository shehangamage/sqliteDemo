package com.example.shehan.sqlliteclassday3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    Button btn, btn_delete;
    EditText txt_one, adress, teltxt;
    ListView list_text;
    AutoCompleteTextView autoTxtView;
    ArrayAdapter arrAd;
    String s;
    String dbName = "shehanDB.db";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ////create db----------------------------------------------------------------
        db = openOrCreateDatabase(dbName, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(1);
        db.setLocale(Locale.CANADA.getDefault());
        db.setLockingEnabled(true);
        //----------------------------------------------------------------------------

        btn = (Button) findViewById(R.id.btn_save);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        txt_one = (EditText) findViewById(R.id.txt_testing);
        adress = (EditText) findViewById(R.id.txt_adres);
        teltxt = (EditText) findViewById(R.id.txt_tel);
        list_text = (ListView) findViewById(R.id.listView);
        autoTxtView = (AutoCompleteTextView) findViewById(R.id.txt_auto_test);

        btn.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        //----
        arrAd = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        //set adaptor to list view
        list_text.setAdapter(arrAd);

        //set long litener to listview
        list_text.setOnItemLongClickListener(this);

        //congig auto complete text view
        autoTxtView.setAdapter(arrAd);
        autoTxtView.setThreshold(1);
        //----

        viewDataInListView();

        creatdDbTable();

        //List view - onItemClickListener
        //Expandable listview = onItemSelectedListener
    }

    public void creatdDbTable() {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS tbltest(NAME TEXT ,ADDRESS TEXT ,TEL TEXT)");
            Toast.makeText(MainActivity.this, "Table created.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick(View view) {
        if (view == btn) {
            try {
                db.execSQL("insert into tbltest(NAME,ADDRESS,TEL)values('" + txt_one.getText().toString() + "','" + adress.getText().toString() + "','" + teltxt.getText().toString() + "')");
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, txt_one.getText().toString(), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            viewDataInListView();


        } else if (view == btn_delete) {
            try {
                db.execSQL("delete from tbltest ");
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void viewDataInListView() {

        creatdDbTable();

        Vector vec = new Vector();
        Cursor c = db.rawQuery("select * from tbltest", null);
        while (c.moveToNext()) {
            String name = c.getString(0);
            String adrs = c.getString(1);
            String no = c.getString(2);
            vec.add(name + " - " + adrs + " - " + no);
        }
        arrAd.clear();
        for (int i = 0; i < vec.size(); i++) {
            arrAd.add(vec.elementAt(i).toString());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

        s = adapterView.getItemAtPosition(position).toString();
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirm");
        alert.setMessage("Do you want to delete?");
        final EditText ed = new EditText(this);
        alert.setView(ed);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ss[] = s.split("-");

                if (ed.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, " enter password", Toast.LENGTH_SHORT).show();
                } else {
                    String pass = ed.getText().toString();
                    if (pass.equals("shehan")) {
                        try {
                            db.execSQL("delete from tbltest where NAME='" + ss[0].trim() + "'");
                            Toast.makeText(MainActivity.this, ss[0] + " is Deleted", Toast.LENGTH_SHORT).show();
                            viewDataInListView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), ss[0] + "Password is incorrect", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create();
        alert.show();

        return false;
    }
}

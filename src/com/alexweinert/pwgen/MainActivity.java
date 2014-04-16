package com.alexweinert.pwgen;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        this.populateOptionSpinners();

        return true;
    }

    private void populateOptionSpinners() {
        // Taken from http://developer.android.com/guide/topics/ui/controls/spinner.html
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.optionChoices,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Use the same adapter for all spinners
        for (Spinner spinner : this.getOptionSpinners()) {
            spinner.setAdapter(adapter);
        }
    }

    private Iterable<Spinner> getOptionSpinners() {
        LinkedList<Spinner> returnValue = new LinkedList<Spinner>();
        returnValue.add((Spinner) this.findViewById(R.id.lowercaseSpinner));
        returnValue.add((Spinner) this.findViewById(R.id.uppercaseSpinner));
        returnValue.add((Spinner) this.findViewById(R.id.digitsSpinner));
        returnValue.add((Spinner) this.findViewById(R.id.symbolsSpinner));
        returnValue.add((Spinner) this.findViewById(R.id.ambiguousSpinner));
        return returnValue;
    }
}

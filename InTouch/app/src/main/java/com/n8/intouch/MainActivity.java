package com.n8.intouch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase firebase = ((InTouchApplication) getApplication()).getFirebase();

        Toast.makeText(this, firebase.getKey(), Toast.LENGTH_LONG).show();

        firebase.child("foo").setValue("goobaroo");
    }
}

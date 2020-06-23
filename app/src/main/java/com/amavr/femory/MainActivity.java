package com.amavr.femory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.ScrollingTabContainerView;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.amavr.tools.XMem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "XDBG.main";

    private Gson gson = new Gson();

    private String token = "";

    private Toolbar mToolbar;
    private AppCompatSpinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XMem.create(getApplication());

        mToolbar = findViewById(R.id.toolbar);
        mSpinner = findViewById(R.id.spinner_groups);

        setSupportActionBar(mToolbar);

        List<String> group_names = XMem.getInstance().getGroupsNames();
        ArrayAdapter<String> adp_group = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                group_names);
        adp_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adp_group);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                XMem.getInstance().setGroup(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.d(TAG, String.format("data: %s", data));

        Log.d(TAG, String.format("register token: %s", XMem.getInstance().getTag("a-token")));

        FirebaseUser fbu = FirebaseAuth.getInstance().getCurrentUser();
        if(fbu == null){
            goToAuth();
        }
        else{
            Log.d(TAG, String.format("register token: %s", fbu.getIdToken(false)));
        }

        NavController navController = Navigation.findNavController(this, R.id.hostNavFragment);
//        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
//        NavigationUI.setupWithNavController(bottomNav, navController);

//        btnTest = (Button)findViewById(R.id.btnTest);
//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Log.d(TAG, "Firebase signout!");
//                FirebaseUser fbu = FirebaseAuth.getInstance().getCurrentUser();
//                Log.d(TAG, String.format("Firebase user: %s", gson.toJson(fbu)));
//
//                /// из учетной записи гугл также выход,
//                // чтобы была возможность выбрать другого пользователя
//                GoogleSignInClient gc = (GoogleSignInClient)XMem.getInstance().getGoogleClient();
//                if(gc != null){
//                    gc.signOut();
//                    gc.revokeAccess();
//                }
//
//                /// после выхода на главном экране делать нечего,
//                /// поэтому - на экран авторизации
//                goToAuth();
//            }
//        });

//        getToken();
    }



    private void goToAuth(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, String.format("request: %s, result: %s", requestCode, resultCode));
        /// не было авторизации
        if(resultCode == 0) {
            finish();
        }
    }

    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        String msg = String.format("token: %s", token);
                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

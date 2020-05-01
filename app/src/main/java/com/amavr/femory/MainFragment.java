package com.amavr.femory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.amavr.tools.NotificationID;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "XDBG.main-fragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NavController nc;

    public MainFragment() {
        // Required empty public constructor
        super();
        Log.d(TAG, "constructor");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NavFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        Log.d(TAG, "newInstance");
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View navView = inflater.inflate(R.layout.fragment_main, container, false);
        nc = NavHostFragment.findNavController(this);

        navView.findViewById(R.id.btnChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");
                sendNotification("abc");
            }
        });

        ExpandableListView listView = (ExpandableListView)navView.findViewById(R.id.groupedList);

        //Создаем набор данных для адаптера
        ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();
        ArrayList<String> children1 = new ArrayList<String>();
        ArrayList<String> children2 = new ArrayList<String>();
        children1.add("Child_1");
        children1.add("Child_2");
        groups.add(children1);
        children2.add("Child_1");
        children2.add("Child_2");
        children2.add("Child_3");
        groups.add(children2);
        //Создаем адаптер и передаем context и список с данными
        ExpListAdapter adapter = new ExpListAdapter(getActivity().getApplicationContext(), groups);
        listView.setAdapter(adapter);

//        navView.findViewById(R.id.btnTasks).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nc.navigate(R.id.tasksFragment);
//            }
//        });
//
//        navView.findViewById(R.id.btnUsers).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nc.navigate(R.id.usersFragment);
//            }
//        });
//
//        navView.findViewById(R.id.btnOptions).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nc.navigate(R.id.optionsFragment);
//            }
//        });

        return navView;
    }

    private void sendNotification(String messageBody) {
        Log.d(TAG, "Here #1");
        Context ctx = getActivity();
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Log.d(TAG, "Here #2");

        Object nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager notificationManager = (NotificationManager) nm;
        createNotificationChannel(notificationManager);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(ctx, getString(R.string.channel_id))
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setContentTitle("FCM Message")
                        .setContentText(String.format("click at %s", new Date()))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        int id = NotificationID.getID();
        notificationManager.notify(id, notificationBuilder.build());
        Log.d(TAG, String.format("sent notification id: %s", id));
    }

    private void createNotificationChannel(NotificationManager nm) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        Log.d(TAG, String.format("Build.VERSION.SDK_INT: %s", Build.VERSION.SDK_INT));
        Log.d(TAG, String.format("Build.VERSION_CODES.O: %s", Build.VERSION_CODES.O));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Context ctx = getActivity();
            CharSequence name = ctx.getString(R.string.channel_name);
            String description = ctx.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
//            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
            nm.createNotificationChannel(channel);
        }
    }
}

package io.github.workbuddymonitor;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.app.AlarmManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    private EditText etHost, etLan, etToken;
    private Spinner spInterval;
    private TextView tvResult;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Store.migrate(this);
        setContentView(R.layout.activity_settings);

        etHost = findViewById(R.id.etHost);
        etLan = findViewById(R.id.etLan);
        etToken = findViewById(R.id.etToken);
        spInterval = findViewById(R.id.spInterval);
        tvResult = findViewById(R.id.tvResult);

        etHost.setText(Store.host(this));
        etLan.setText(Store.lanHost(this));
        etToken.setText(Store.token(this));

        String[] items = {"15 秒", "30 秒", "1 分钟", "3 分钟", "10 分钟"};
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spInterval.setAdapter(ad);
        spInterval.setSelection(Store.intervalIndex(this));

        Button btnTest = findViewById(R.id.btnTest);
        Button btnSave = findViewById(R.id.btnSave);
        btnTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tvResult.setText("测试中…");
                Store.setHost(SettingsActivity.this, etHost.getText().toString().trim());
                Store.setLanHost(SettingsActivity.this, etLan.getText().toString().trim());
                Store.setToken(SettingsActivity.this, etToken.getText().toString().trim());
                Fetcher.fetch(SettingsActivity.this, new Fetcher.Cb() {
                    public void onResult(final Snapshot s, final String err) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (s != null && err == null) {
                                    tvResult.setText("连接成功 · 总余额 " + MainActivity.fmtNum(s.totalBalance)
                                            + " · " + s.accountCount + " 个账号 · 已签 " + s.signedCount);
                                } else {
                                    tvResult.setText("连接失败：" + err);
                                }
                            }
                        });
                    }
                });
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Store.setHost(SettingsActivity.this, etHost.getText().toString().trim());
                Store.setLanHost(SettingsActivity.this, etLan.getText().toString().trim());
                Store.setToken(SettingsActivity.this, etToken.getText().toString().trim());
                Store.setIntervalIndex(SettingsActivity.this, spInterval.getSelectedItemPosition());
                Scheduler.schedule(SettingsActivity.this);
                WbWidget.pushAll(SettingsActivity.this);
                tvResult.setText("已保存，刷新间隔 " + (Store.interval(SettingsActivity.this) / 1000) + " 秒");
            }
        });
    }

    static PendingIntent pi(Context c) {
        Intent i = new Intent(c, TickReceiver.class);
        i.setAction("io.github.workbuddymonitor.TICK");
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= 23) flags |= PendingIntent.FLAG_IMMUTABLE;
        return PendingIntent.getBroadcast(c, 1001, i, flags);
    }
}

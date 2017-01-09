package com.lee.usagesample;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {
	private Button btnStat;
	private ListView lvAppTime;
	private List<UsageStats> usageStats;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Check if permission enabled
		if (UStats.getUsageStatsList(this).isEmpty()) {
			Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
			startActivity(intent);
		}

		lvAppTime = (ListView) findViewById(R.id.lvAppTime);
		btnStat = (Button) findViewById(R.id.btnStat);
		btnStat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				usageStats = UStats.getUsageStatsList(MainActivity.this);
				sortUsageStats();
				showAppTimeList();
			}
		});

	}

	private void sortUsageStats() {
		for (int i = 0; i < usageStats.size(); i++) {
			if (usageStats.get(i).getTotalTimeInForeground() < 1000) {
				usageStats.remove(i);
			}
		}

		// DESC 내림차순
		Collections.sort(usageStats, new Comparator<UsageStats>() {
			public int compare(UsageStats obj1, UsageStats obj2) {
				// TODO Auto-generated method stub
				return (obj1.getTotalTimeInForeground() > obj2.getTotalTimeInForeground()) ? -1
						: (obj1.getTotalTimeInForeground() < obj2.getTotalTimeInForeground()) ? 1 : 0;
			}
		});
	}

	private void showAppTimeList() {
		AppListAdapter adapter = new AppListAdapter(this, R.layout.item_list, usageStats);
		lvAppTime.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}

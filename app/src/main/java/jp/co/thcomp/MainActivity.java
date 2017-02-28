package jp.co.thcomp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		findViewById(R.id.LaunchTestActivity).setOnClickListener(this);
		findViewById(R.id.LaunchTestService).setOnClickListener(this);
		findViewById(R.id.StopTestService).setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent intent = new Intent();
		intent.setClass(this, TestService.class);
		stopService(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();

		switch(arg0.getId()){
		case R.id.LaunchTestActivity:
			intent.setClass(this, TestActivity.class);
			startActivity(intent);
			break;
		case R.id.LaunchTestService:
			intent.setClass(this, TestService.class);
			startService(intent);
			break;
		case R.id.StopTestService:
			intent.setClass(this, TestService.class);
			stopService(intent);
			break;
		}
	}
}

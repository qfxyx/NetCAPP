package com.example.jinanuniversity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jinanuniversity.R;

public class PhotoActivity extends Activity implements OnClickListener {

	private TextView txt_address;
	private ImageView img_address;
	private Button btn_search, btn_back, btn_office, btn_science;
	private int requestCode = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo);

		initView();
	}

	private void initView() {
		txt_address = (TextView) findViewById(R.id.txt_address);
		img_address = (ImageView) findViewById(R.id.img_address);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_office = (Button) findViewById(R.id.btn_office);
		btn_science = (Button) findViewById(R.id.btn_science);

		txt_address.setOnClickListener(this);
		img_address.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_office.setOnClickListener(this);
		btn_science.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(PhotoActivity.this, PhotoList.class);
		intent.putExtra("building", txt_address.getText().toString().trim());
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.btn_search:
			startActivity(intent);
			break;

		case R.id.btn_office:
			startActivity(intent);
			break;

		case R.id.btn_science:
			startActivity(intent);
			break;
			
		case R.id.txt_address:

		case R.id.img_address:
			startActivityForResult(new Intent(this, BuildingList.class),
					requestCode);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			String building = data.getStringExtra("building") == null ? ""
					: data.getStringExtra("building");
			if (building != "") {
				txt_address.setText(building);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}

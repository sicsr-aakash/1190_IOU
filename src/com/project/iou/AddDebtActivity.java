package com.project.iou;

import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddDebtActivity extends ActionBarActivity {

	Button buttonIOwe, buttonYouOwe;
	EditText editTextAddAmount, editTextAddDescription;
	Spinner spinnerNames;
	DatabaseHelper dbHelper = new DatabaseHelper(this);
	String selectedName, description;
	double amount;
	int selectedId;
	int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_debt);
		
		final ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
		bar.setTitle("IOU");
		
		buttonIOwe = (Button) findViewById(R.id.buttonOweYou);
		buttonYouOwe = (Button) findViewById(R.id.buttonOweMe);
		editTextAddAmount = (EditText) findViewById(R.id.editTextAmount);
		editTextAddDescription = (EditText) findViewById(R.id.editTextDescription);
		spinnerNames = (Spinner) findViewById(R.id.spinnerNames);
		
		List<Model> nameList;
    	nameList = dbHelper.getAllNames();
    	int sizeOfList = nameList.size();
    	String names[] = new String[sizeOfList];
		
    	final int id[] = new int[sizeOfList];
		
		for(int loop = 0; loop < sizeOfList; loop++){
			id[loop] = nameList.get(loop).id;
			names[loop] = nameList.get(loop).name;
		}
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, names);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);  
        spinnerNames.setAdapter(spinnerAdapter);
        
	     
        // --- Retrieving Value from spinner --- //
    	spinnerNames.setOnItemSelectedListener(new OnItemSelectedListener() {
	    	public void onItemSelected( AdapterView<?> parent,View view,int pos, long id) {
	    		selectedName = spinnerNames.getSelectedItem().toString();
	    		position = pos;
	        }
	    	public void onNothingSelected( AdapterView<?> parent) {}
        });
    	
    	buttonIOwe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(editTextAddDescription.getText().toString().equals("")){
					description = "No Description:";
				}
				else {
//					description = validateDescription(description);
					description = editTextAddDescription.getText().toString()+":";
				}
				
				if(editTextAddAmount.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(), "Amount you owe "+selectedName+" ?", Toast.LENGTH_SHORT);
					View view = toast.getView();
					view.setBackgroundColor(getResources().getColor(R.color.yellow));
					toast.show();
				}
				else {
					amount = Double.parseDouble(editTextAddAmount.getText().toString());
					String newAmount = amount + ":";
					int update = dbHelper.updateAmount(id[position], newAmount, description, "debit");
					if(update == 1){
						Toast toast = Toast.makeText(getApplicationContext(), "Transaction Recorded", Toast.LENGTH_SHORT);
						View view = toast.getView();
						view.setBackgroundColor(getResources().getColor(R.color.green));
						toast.show();
						Intent intent = new Intent(AddDebtActivity.this, MainActivity.class);
						startActivity(intent);
					}
					else {
						Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT);
						View view = toast.getView();
						view.setBackgroundColor(getResources().getColor(R.color.red));
						toast.show();
					}
				}
			}
		});
        
        buttonYouOwe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(editTextAddDescription.getText().toString().equals("")){
					description = "No Description:";
				}
				else {
					//description = validateDescription(description);
					description = editTextAddDescription.getText().toString()+":";
				}
				
				if(editTextAddAmount.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(), "Amount you owe "+selectedName+" ?", Toast.LENGTH_SHORT);
					View view = toast.getView();
					view.setBackgroundColor(getResources().getColor(R.color.yellow));
					toast.show();
				}
				else {
					amount = Double.parseDouble(editTextAddAmount.getText().toString());
					String newAmount = amount + ":";
					int update = dbHelper.updateAmount(id[position], newAmount, description, "credit");
					if(update == 1){
						Toast toast = Toast.makeText(getApplicationContext(), "Transaction Recorded", Toast.LENGTH_SHORT);
						View view = toast.getView();
						view.setBackgroundColor(getResources().getColor(R.color.green));
						toast.show();
						Intent intent = new Intent(AddDebtActivity.this, MainActivity.class);
						startActivity(intent);
					}
					else {
						Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT);
						View view = toast.getView();
						view.setBackgroundColor(getResources().getColor(R.color.red));
						toast.show();
					}
				}
			}
		});
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_debt, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_goBack) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
//	public String validateDescription(String description){
//		
//		return description;
//	}
}

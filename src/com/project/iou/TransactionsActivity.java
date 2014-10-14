package com.project.iou;

import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class TransactionsActivity extends ActionBarActivity {

	ListView listTransactions;
	Model model = null;
	DatabaseHelper dbHelper = new DatabaseHelper(this);
	int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactions);
		
		// Setting Action Bar Color and Title
		final ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
		bar.setTitle("IOU");
		
		// Get position of the selected item
		position = getIntent().getExtras().getInt("position");
		
		List<Model> nameList;
    	nameList = dbHelper.getAllNames();
    	
		String debit = nameList.get(position).debit;
		String credit = nameList.get(position).credit;
		String debitDescription = nameList.get(position).debitDescription;
		String creditDescription = nameList.get(position).creditDescription;
				
		List<String> debitList = Arrays.asList(debit.split(":"));
		List<String> creditList = Arrays.asList(credit.split(":"));
		List<String> debitDescriptionList = Arrays.asList(debitDescription.split(":"));
		List<String> creditDescriptionList = Arrays.asList(creditDescription.split(":"));
		
		int arraySize = debitList.size()+creditList.size();
		
		String amountArray[] = new String[arraySize];
		String descriptionArray[] = new String[arraySize];
		Integer icon_id[] = new Integer[arraySize];
		
		int j = 0;
		for(int i = 0; i <  arraySize; ){
			
			if(j < creditList.size()){
				
				icon_id[i] = R.drawable.green;
				amountArray[i] = creditList.get(j);
				descriptionArray[i] = creditDescriptionList.get(j);
				i++;
			}
			
			if(j < debitList.size()){
				
				icon_id[i] = R.drawable.red;
				amountArray[i] = debitList.get(j);
				descriptionArray[i] = debitDescriptionList.get(j);
				i++;
			}
			j++;
		}
		
		String amountArrayNew[] = new String[arraySize-2];
		String descriptionArrayNew[] = new String[arraySize-2];
		Integer icon_idNew[] = new Integer[arraySize-2];
		
		int k = arraySize-1;
		for(int i = 0; i <  arraySize-2; i++){
			amountArrayNew[i] = amountArray[k];
			descriptionArrayNew[i] = descriptionArray[k];
			icon_idNew[i] = icon_id[k];
			k--;
		}
		
		
		CustomAdapter adapter = new CustomAdapter(this, descriptionArrayNew, amountArrayNew, icon_idNew);
		listTransactions = (ListView) findViewById(R.id.listTransactions);
		listTransactions.setAdapter(adapter);
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
}

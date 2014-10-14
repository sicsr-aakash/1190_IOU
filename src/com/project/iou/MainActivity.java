package com.project.iou;

import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Uri uriContact;
	private static final int REQUEST_CODE_PICK_CONTACTS = 1;
	String valueOfNameSpinner;
	
	Button addName, addDebt, buttonAddContact,buttonViewTransaction, buttonDeletePerson;
	ListView listDebts;
	EditText editTextName;
	Spinner spinnerNames;
	
	Model model = null;
	DatabaseHelper dbHelper = new DatabaseHelper(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
		
		List<Model> nameList;
    	nameList = dbHelper.getAllNames();
    	int sizeOfList = nameList.size();
    	final int user_id[] = new int[sizeOfList];
    	Integer icon_id[] = new Integer[sizeOfList];
    	
    	String names[] = new String[sizeOfList];
    	String debit[] = new String[sizeOfList];
    	String credit[] = new String[sizeOfList];
    	float differenceInAmount[] = new float[sizeOfList];
    	
    	String amount[] = new String[sizeOfList];
    	
		for(int loop = 0; loop < sizeOfList; loop++){
			user_id[loop] = nameList.get(loop).id;
			names[loop] = nameList.get(loop).name;
			debit[loop] = nameList.get(loop).debit;
			credit[loop] = nameList.get(loop).credit;
		}
		
		
		
		for(int loop = 0; loop < sizeOfList; loop++){
			
			List<String> debitAmounts = Arrays.asList(debit[loop].split(":"));
			List<String> creditAmounts = Arrays.asList(credit[loop].split(":"));
			
			float sumOfDebit = 0, sumOfCredit = 0;
			
			for (int i = 0; i < debitAmounts.size(); i++) {
				sumOfDebit = sumOfDebit + Float.parseFloat(debitAmounts.get(i));
			}
			
			for (int i = 0; i < creditAmounts.size(); i++) {
				sumOfCredit = sumOfCredit + Float.parseFloat(creditAmounts.get(i));
			}
			
			Log.e(names[loop],Double.toString(sumOfDebit));
			Log.e(names[loop],Double.toString(sumOfCredit));
			
			if( sumOfDebit > sumOfCredit ){
				differenceInAmount[loop] = sumOfDebit - sumOfCredit;
				amount[loop] = Float.toString(differenceInAmount[loop]);
				icon_id[loop] = R.drawable.red;
			}
			else if( sumOfCredit > sumOfDebit ){
				differenceInAmount[loop] = sumOfCredit - sumOfDebit;
				amount[loop] = Float.toString(differenceInAmount[loop]);
				icon_id[loop] = R.drawable.green;
			}
			else {
				differenceInAmount[loop] = 0;
				amount[loop] = Float.toString(differenceInAmount[loop]);
				icon_id[loop] = R.drawable.yellow;
			}
		}
		
		
		CustomAdapter adapter = new CustomAdapter(this, names, amount, icon_id);
		listDebts = (ListView) findViewById(R.id.listDebts);
		listDebts.setAdapter(adapter);
		
		listDebts.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				
				// Fetching position of list value
				final int position = pos;
				
				// Invoking a dialog on click of list value
				LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
				View promptsView = inflater.inflate(R.layout.dialog_transaction_delete, null);
				AlertDialog.Builder nameDialog = new AlertDialog.Builder(MainActivity.this);
				buttonViewTransaction = (Button) promptsView.findViewById(R.id.buttonViewTransaction);
				buttonDeletePerson = (Button) promptsView.findViewById(R.id.buttonDeletePerson);
				
				// On Click for viewing transaction
				buttonViewTransaction.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// Navigating to Transaction activity
						Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
						intent.putExtra("position", position);
						startActivity(intent);
						
					}
				});
				
				buttonDeletePerson.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						// Alert dialog to confirm delete operation
						AlertDialog.Builder deletePromt = new AlertDialog.Builder(MainActivity.this);
						deletePromt.setMessage("Are you sure, you want to delete?");
						
						deletePromt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								dbHelper.deletePerson(user_id[position]);
								Toast toast = Toast.makeText(getApplicationContext()," Deleted...", Toast.LENGTH_SHORT);
								View view = toast.getView();
								view.setBackgroundColor(getResources().getColor(R.color.green));
								toast.show();
								finish();
								startActivity(getIntent());
								
							}
						});
						
						deletePromt.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						
						AlertDialog dialogCreated = deletePromt.create();
						dialogCreated.show();
					}
				});
				
				nameDialog.setView(promptsView);
				AlertDialog dialogCreated = nameDialog.create();
				dialogCreated.show();
			}
		});
		
	} // onCreate
	
	
	
	void showAddNameDialog(){
		
		// Dialog with Text field and select from contact option
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View promptsView = inflater.inflate(R.layout.activity_add_name_dialog, null);
		AlertDialog.Builder nameDialog = new AlertDialog.Builder(MainActivity.this);
		editTextName = (EditText) promptsView.findViewById(R.id.editTextName);
		buttonAddContact = (Button) promptsView.findViewById(R.id.buttonAddContact);
		
		buttonAddContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
		    }
		});
		
		nameDialog.setView(promptsView);
		nameDialog.setTitle("Add Person");
		
		nameDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!editTextName.getText().toString().equals("")){
					model = new Model(editTextName.getText().toString(), "0:", "Initial:", "0:", "Initial:");
					dbHelper.addNewName(model);
					Toast toast = Toast.makeText(getApplicationContext(), editTextName.getText() + " added !!", Toast.LENGTH_SHORT);
					View view = toast.getView();
					view.setBackgroundColor(getResources().getColor(R.color.green));
					toast.show();
					finish();
					startActivity(getIntent());
				}
				else{
					Toast toast = Toast.makeText(getApplicationContext(), "Name cannot be left blank..", Toast.LENGTH_SHORT);
					View view = toast.getView();
					view.setBackgroundColor(getResources().getColor(R.color.red));
					toast.show();
				}
			}
		});
		
		nameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog dialogCreated = nameDialog.create();
		dialogCreated.show();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
		super.onActivityResult(requestCode, resultCode, data);
 
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            uriContact = data.getData();
            editTextName.setText(retrieveContactName());
        }
    }
	
	private String retrieveContactName() {
		 
        String contactName = null;
        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
 
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
 
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();    
        return contactName;
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_addDebt:
	        	Intent intent1 = new Intent(this, AddDebtActivity.class);
	        	finish();
	        	startActivity(intent1);
	        	return true;
	        	
	        case R.id.action_addPerson:
	        	showAddNameDialog();
	            return true;
	        
	        case R.id.action_help:
	        	Intent intent2 = new Intent(this, HelpActivity.class);
	        	finish();
	        	startActivity(intent2);    
	            return true;
	            
	        case R.id.action_about:
	        	Intent intent3 = new Intent(this, AboutActivity.class);
	        	finish();
	        	startActivity(intent3);
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
} // class
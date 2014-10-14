package com.project.iou;

import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	// Table Name
    private static final String TABLE = "transactions";

    // Columns of the table
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DEBIT = "debit";
    private static final String DEBIT_DESCRIPTION = "debit_description";
    private static final String CREDIT = "credit";
    private static final String CREDIT_DESCRIPTION = "credit_description";
    
//    private static final String[] COLUMNS = {ID,NAME,DEBIT,DEBIT_DESCRIPTION,CREDIT,CREDIT_DESCRIPTION};
    
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
    // Database Name
    private static final String DATABASE_NAME = "IouDB";
 
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_TABLE = "CREATE TABLE transactions ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "name TEXT, "+
                "debit TEXT, "+
                "debit_description TEXT, "+
                "credit TEXT, "+
                "credit_description TEXT )";
 
        // create books table
        db.execSQL(CREATE_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS transactions");
        // create fresh books table
        this.onCreate(db);
    }
    
    // TRANSACTIONS IN THE TABLE //
    
    // Adding a new name
    public void addNewName(Model model){	
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(NAME, model.getName()); 
		values.put(DEBIT, model.getDebit());
		values.put(DEBIT_DESCRIPTION, model.getDebitDescription());
		values.put(CREDIT, model.getCredit());
		values.put(CREDIT_DESCRIPTION, model.getDebitDescription());
		// 3. insert
		db.insert(TABLE, null,values);
		// 4. close
		db.close(); 
    }
    
	public List<Model> getAllNames() {
		
       List<Model> names = new  LinkedList<Model>();
 
       // 1. build the query
       String query = "SELECT  * FROM " + TABLE;
 
       // 2. get reference to writable DB
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(query, null);

       // 3. go over each row, build model and add it to list
       Model name = null;
       if (cursor.moveToFirst()) {
           do {
               name = new Model();
               name.setId(Integer.parseInt(cursor.getString(0)));
               name.setName(cursor.getString(1));
               name.setDebit(cursor.getString(2));
               name.setDebitDescription(cursor.getString(3));
               name.setCredit(cursor.getString(4));
               name.setCreditDescription(cursor.getString(5));
               
               // Add name to names list
               names.add(name);
           } while (cursor.moveToNext());
       }
       // return names
       return names;
	}
	
	public int updateAmount(int id, String amount, String description, String type) {
		 
	    // 1. get reference to writable DB
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    // 2. create ContentValues to add key "column"/value
	    ContentValues values = new ContentValues();
	    
	    List<Model> nameList;
    	nameList = getAllNames();
    	int sizeOfList = nameList.size();
    	int position = 0;
    	
    	for(int loop=0; loop<sizeOfList; loop++){
    		if(nameList.get(loop).id == id){
    			position = loop;
    		}
    	}
    	if(type.equals("debit")){
	    	
    		amount = nameList.get(position).debit + amount;
	    	description = nameList.get(position).debitDescription + description; 
	    	values.put(DEBIT, amount);
			values.put(DEBIT_DESCRIPTION, description);
	    }
	    if(type.equals("credit")){
	    	
	    	amount = nameList.get(position).credit + amount;
	    	description = nameList.get(position).creditDescription + description;
	    	values.put(CREDIT, amount);
			values.put(CREDIT_DESCRIPTION, description);
	    }
	    
	    // 3. updating row
	    int i = db.update(TABLE, //table
	            values, // column/value
	            ID+" = ?", // selections
	            new String[] { String.valueOf(id) }); //selection args
	 
	    // 4. close
	    db.close();
	 
	    return i;
	 
	}
	
	public void deletePerson(int id) {
		 
		// 1. get reference to writable DB
	    SQLiteDatabase db = this.getWritableDatabase();
		 
	    // 2. delete a person
		db.delete(TABLE, //Table
				ID+" = ?", // selections
		new String[] { String.valueOf(id) }); //selections argument
		 
	    // 3. close
	    db.close();
    }
	
	
	
}
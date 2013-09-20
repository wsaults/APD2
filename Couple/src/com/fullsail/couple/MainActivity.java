/*
 * project 	Couple
 * 
 * package 	com.fullsail.couple
 * 
 * @author 	William Saults
 * 
 * date 	Sep 12, 2013
 */
package com.fullsail.couple;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity {
	
	EditText _emailEditText;
	EditText _passwordEditText;
	static Context _context;
	SharedPreferences _preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Parse.initialize(this, "7LdGck89MP9UnBeem9wYc1ER96MMOJKfGfWlP6rC", "KGEopcwTYaXZ2FUAmjw5aZwIPRTjw522TjJPya4H"); 
		ParseAnalytics.trackAppOpened(getIntent());
		
		_context = this;
		_preferences = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
		_emailEditText = (EditText) findViewById(R.id.emailEditText);
		_passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		
		// Restore the text in the edit text.
		String userName = _preferences.getString("username", "");
		if (userName.length() > 0) {
			_emailEditText.setText(userName);
		}

		Button registerButton = (Button) findViewById(R.id.registerButton);
		registerButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				createNewUser(_emailEditText.getText().toString(), _passwordEditText.getText().toString());
		    }
		});
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				loginUser(_emailEditText.getText().toString(), _passwordEditText.getText().toString());
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public Boolean areUserCredentialsValid(String username, String password) {
		// Make sure there is valid data in the fields
		if (username.length() <= 0) {
			Toast.makeText(getApplicationContext(), "Please enter an email", Toast.LENGTH_LONG).show();
			return false;
		}

		if (password.length() <= 0) {
			Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();
			return false;
		}
		
		if (isValidEmail(username.subSequence(0, username.length()))) {
			return true;
		} else {
			Toast.makeText(_context, "Please enter a vaild email address", Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	
	public void createNewUser(final String username, final String password) {
		if (!areUserCredentialsValid(username, password)) return;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
		query.whereEqualTo("username", _emailEditText.getText().toString());
		query.whereEqualTo("password", _passwordEditText.getText().toString());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> user, ParseException e) {
				if (e == null) {
		            Log.d("user", "Retrieved " + user.size() + " user");
		            Toast.makeText(_context, "You already have an account with Couple. Try pressing the Login button.", Toast.LENGTH_LONG).show();
		        } else {
		            Log.d("user", "Creating user: " + e.getMessage());
		            // Save the username locally
		    		SharedPreferences.Editor editor = _preferences.edit();
		    		editor.putString("username", username);
		    		editor.commit();
		    		
		    		// Save the username and password to parse
		    		ParseObject userObject = new ParseObject("CoupleUser");
		    		userObject.put("username", username);
		    		userObject.put("password", password);
		    		userObject.saveInBackground();
		    		
		    		Intent a = new Intent(getApplicationContext(),InviteActivity.class);
		    		a.putExtra("Email", username);
					setResult(RESULT_OK, a);
					startActivityForResult(a,0);
		        }	
			}
		});
	}
	
	public void loginUser(final String username, String password) {
		if (!areUserCredentialsValid(username, password)) return;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
		query.whereEqualTo("username", _emailEditText.getText().toString());
		query.whereEqualTo("password", _passwordEditText.getText().toString());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> user, ParseException e) {
				if (e == null) {
		            Log.d("user", "Retrieved " + user.size() + " user");
		            
		            // A valid user, matching username and password, was found. Login.
		            Boolean inviteSent = _preferences.getBoolean("inviteSentOrAccepted", false);
		            Intent a = null;
		    		if (!inviteSent) {
			            a = new Intent(getApplicationContext(),InviteActivity.class);
			            a.putExtra("Email", username);
		    		} else {
		    			a = new Intent(getApplicationContext(),TabActivity.class);
		    		}
		    		setResult(RESULT_OK, a);
					startActivityForResult(a,0);
		            
		        } else {
		            Log.d("user", "No matching credentials found: " + e.getMessage());
		        }	
			}
		});
	}
}

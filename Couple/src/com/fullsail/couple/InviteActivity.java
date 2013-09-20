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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InviteActivity extends Activity {
	
	Context _context;
	EditText _inviteEmailEditText;
	EditText _acceptInviteEditText;
	String _emailFromLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		_context = this;
		
		Bundle extras = getIntent().getExtras();
		_emailFromLogin = extras.getString("Email");
		_inviteEmailEditText = (EditText) findViewById(R.id.inviteEmailEditText);
		_acceptInviteEditText = (EditText) findViewById(R.id.acceptInviteEditText);
		
		Button sendInviteButton = (Button) findViewById(R.id.sendInviteButton);
		sendInviteButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				sendInvitation(_inviteEmailEditText.getText().toString());
		    }
		});
		
		Button acceptInviteButton = (Button) findViewById(R.id.acceptInviteButton);
		acceptInviteButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				acceptInvitation(_acceptInviteEditText.getText().toString());
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invite, menu);
		return true;
	}
	
	public Boolean areUserCredentialsValid(String username) {
		// Make sure there is valid data in the fields
		if (username.length() <= 0) {
			Toast.makeText(getApplicationContext(), "Please enter an email", Toast.LENGTH_LONG).show();
			return false;
		}

		if (isValidEmail(username.subSequence(0, username.length()))) {
			return true;
		} else {
			Toast.makeText(getApplicationContext(), "Please enter a vaild email address", Toast.LENGTH_LONG).show();
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
	
	public void sendInvitation(final String username) {
		if (!areUserCredentialsValid(username)) return;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Invite");
		query.whereEqualTo("toUser", username);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> invite, ParseException e) {
				if (e == null) {
					Log.d("invite", "Retrieved " + invite.size() + " invite");
					if (invite.size() >= 1) {
						Toast.makeText(getApplicationContext(), "That user has already been invited.", Toast.LENGTH_LONG).show();
					} else {
						// Save the username and password to parse
			    		ParseObject inviteObject = new ParseObject("Invite");
			    		inviteObject.put("fromuser", _emailFromLogin);
			    		inviteObject.put("toUser", username);
			    		String key = "123";
			    		inviteObject.put("code", key);
			    		inviteObject.saveInBackground();
			    		
			    		updatePreference();
			    		
			    		Intent a = new Intent(getApplicationContext(),TabActivity.class);
			     		setResult(RESULT_OK, a);
			     		startActivityForResult(a,0); 
					}  
		        } else {
		        	Toast.makeText(getApplicationContext(), "There has been an error.", Toast.LENGTH_LONG).show();
		        }	
			}
		});
	}
	
	public void acceptInvitation(String code) {
		if (code.length() > 0) {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Invite");
			query.whereEqualTo("toUser", _emailFromLogin);
			query.whereEqualTo("code", code);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> invite, ParseException e) {
					if (e == null) {
			            Log.d("invite", "Retrieved " + invite.size() + " invite");
			            
			            updatePreference();
			    		
			    		// TODO: connect users
			    		
			    		Intent a = new Intent(getApplicationContext(),TabActivity.class);
			    		setResult(RESULT_OK, a);
			    		startActivityForResult(a,0);
			            
			        } else {
			            Log.d("invite", "No matching invite found: " + e.getMessage());
			            Toast.makeText(getApplicationContext(), "No matching invite found", Toast.LENGTH_LONG).show();
			        }	
				}
			});
		} else {
			Toast.makeText(getApplicationContext(), "Please enter your code", Toast.LENGTH_LONG).show();
		}
	}
	
	public void updatePreference() {
        // Save the inviteSentOrAccepted preference
//		SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
//		SharedPreferences.Editor editor = preferences.edit();
//		editor.putBoolean("inviteSentOrAccepted", true);
//		editor.commit();
	}

}

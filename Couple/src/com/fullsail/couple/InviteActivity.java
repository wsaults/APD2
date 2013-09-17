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
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InviteActivity extends Activity {
	
	Context _context;
	EditText _inviteEmailEditText;
	EditText _acceptInviteEditText;
	String _emailFromLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);
		
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
				acceptInvitation(_inviteEmailEditText.getText().toString());
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
			// TODO: display error message.
			return false;
		}

		// TODO: do email validation.
		return true;
	}
	
	public void sendInvitation(String username) {
		if (!areUserCredentialsValid(username)) return;
		
		// Save the username and password to parse
		ParseObject inviteObject = new ParseObject("Invite");
		inviteObject.put("fromuser", username);
		inviteObject.put("toUser", username);
		inviteObject.put("code", "123");
		inviteObject.saveInBackground();
		
		updatePreference();
		
		Intent a = new Intent(getApplicationContext(),TabActivity.class);
		setResult(RESULT_OK, a);
		startActivityForResult(a,0);
	}
	
	public void acceptInvitation(String code) {
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
		        }	
			}
		});
	}
	
	public void updatePreference() {
        // Save the inviteSentOrAccepted preference
//		SharedPreferences preferences = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
//		SharedPreferences.Editor editor = preferences.edit();
//		editor.putBoolean("inviteSentOrAccepted", true);
//		editor.commit();
	}

}

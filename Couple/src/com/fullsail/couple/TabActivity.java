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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class TabActivity extends FragmentActivity implements
ActionBar.TabListener {

	static Context _context;
	static String _fromUser;
	static String _toUser;
	static Boolean _connected = false;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		_context = this;
		
		SharedPreferences preferences = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
		Boolean hasSeenChatInstructions = preferences.getBoolean("hasSeenChatInstructions", false);
		if (!hasSeenChatInstructions) {
			AlertDialog.Builder builder = new AlertDialog.Builder(_context);
			builder.setMessage("Deleting messages is easy! Just tap any message that you want to delete. You can also delete images by tap-holding on any image in your photos.")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    	   public void onClick(DialogInterface dialog, int id) {
			    		   SharedPreferences p = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
			    		   SharedPreferences.Editor e = p.edit();
			    		   e.putBoolean("hasSeenChatInstructions", true);
			    		   e.commit();
			    	   }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
		
		_fromUser = preferences.getString("fromUser", "");
		_toUser = preferences.getString("toUser", "");
		if (_toUser.length() == 0) {
			Toast.makeText(_context, "Something isn't right. I don't know who to send messages to", Toast.LENGTH_LONG).show();
		}
		if (_fromUser.length() == 0) {
			Toast.makeText(_context, "Something isn't right. I don't know who is sending messages", Toast.LENGTH_LONG).show();
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String key = extras.getString("Key");
			Toast.makeText(_context, key + " is the invite key.", Toast.LENGTH_LONG).show();
		}

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			Bundle args = new Bundle();
			switch (position) {
			case 0:
				// Create the chat fragment
				fragment = new ChatSectionFragment();
				args.putInt(ChatSectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				break;

			case 1:
				// Create the photos fragment
				fragment = new PhotosSectionFragment();
				args.putInt(PhotosSectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		// Return to home screen.
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class ChatSectionFragment extends Fragment {

		ListView _list;
		ListViewAdapter _adapter;
		EditText _chatEditText;
		String _userName;
		String[] _messages;
		String[] _times;
		Timer _timer;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public ChatSectionFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tab_chat, container, false);
			
			// Setup the timer
			_timer = new Timer();
			_timer.schedule(new TimerTask() {
				@Override
				public void run() {
					TimerMethod();
				}

			}, 0, 10000);

			// Generate sample data
			_messages = new String[] {};
			_times = new String[] {};
			
			SharedPreferences preferences = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
			_userName = preferences.getString("username", "") + ": ";

			// Pass results to ListViewAdapter Class
			_adapter = new ListViewAdapter(getActivity(), _messages, _times, _userName);

			// Locate the ListView
			_list = (ListView) rootView.findViewById(R.id.chatListView);

			// Binds the Adapter to the ListView
			if (_list != null) {
				_list.setAdapter(_adapter);
			}

			_chatEditText = (EditText) rootView.findViewById(R.id.chatEditText);

			// Capture clicks on ListView items
			_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					_connected = Connectivity.getConnectionStatus(_context);
					if (!_connected) {
						Toast.makeText(getActivity(), "Please make sure you have an internet connection", Toast.LENGTH_LONG).show();
					} else {
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Messenger");
						query.whereEqualTo("fromUser", _fromUser);
						query.whereEqualTo("toUser", _toUser);
						query.whereEqualTo("message", _messages[position]);
						query.whereEqualTo("time", _times[position]);
						query.findInBackground(new FindCallback<ParseObject>() {
							@Override
							public void done(List<ParseObject> messages, ParseException e) {
								if (e == null) {
									Log.d("Messenger", "There are: " + messages.size() + " messages");
									for (ParseObject message : messages) {
										message.deleteInBackground();
										Toast.makeText(_context, "Message deleted.", Toast.LENGTH_LONG).show();
									}
									refreshChatLog();
								} else {
									Log.d("Messaging", "Error:" + e.getMessage());
								}	
							}
						});
					}
				}
			});

			Button sendButton = (Button) rootView.findViewById(R.id.chatSendButton);
			sendButton.setOnClickListener(new View.OnClickListener() { 
				@Override
				public void onClick(View v) {
					if (_chatEditText.getText().toString().length() > 0) {
						updateChatLog(_chatEditText.getText().toString());
					}
				}
			});

			return rootView;
		}

		private void TimerMethod()
		{
			//This method is called directly by the timer
			//and runs in the same thread as the timer.

			//We call the method that will work with the UI
			//through the runOnUiThread method.
			getActivity().runOnUiThread(Timer_Tick);
		}

		private Runnable Timer_Tick = new Runnable() {
			public void run() {

				//This method runs in the same thread as the UI.               

				//Do something to the UI thread here
				queryMessages();
			}
		};

		public void queryMessages() {
			_connected = Connectivity.getConnectionStatus(_context);
			if (!_connected) {
				Toast.makeText(getActivity(), "Please make sure you have an internet connection", Toast.LENGTH_LONG).show();
			} else {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Messenger");
				query.whereEqualTo("fromUser", _fromUser);
				query.whereEqualTo("toUser", _toUser);
				query.setLimit(30);
				query.orderByAscending("createdAt");
				query.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(List<ParseObject> messages, ParseException e) {
						if (e == null) {
							_messages = new String [] {};
							_times = new String [] {};
							Log.d("Messenger", "There are: " + messages.size() + " messages");
							for (ParseObject message : messages) {
								String m = message.getString("message");
								_messages = append(_messages, m);
								String t = message.getString("time");
								_times = append(_times, t);
							}
							refreshChatLog();
						} else {
							Log.d("Messaging", "Error:" + e.getMessage());
						}	
					}
				});
			}
		}

		public void refreshChatLog() {
			// Pass results to ListViewAdapter Class
			_adapter = new ListViewAdapter(getActivity(), _messages, _times, _userName);
			_list.setAdapter(_adapter);
			((ListViewAdapter)_list.getAdapter()).notifyDataSetChanged();
			scrollListViewToBottom(_list);
		}

		@SuppressLint("SimpleDateFormat")
		public void updateChatLog(String text) {
			_connected = Connectivity.getConnectionStatus(_context);
			if (!_connected) {
				Toast.makeText(getActivity(), "Please make sure you have an internet connection", Toast.LENGTH_LONG).show();
			} else {
				_chatEditText.setText("");
				// Create messenger object
				SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
				String currentDateandTime = sdf.format(new Date());
				ParseObject messengerObj = new ParseObject("Messenger");
				messengerObj.put("fromUser", _fromUser);
				messengerObj.put("toUser", _toUser);
				messengerObj.put("message", text);
				messengerObj.put("time", currentDateandTime);
				messengerObj.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						// Handle success or failure here ...
						if (e == null) {
							queryMessages();
						} else {
							Log.d("updateChatLog", "Error:" + e.getMessage());
						}

					}
				});
			}
		}

		// Thank you stack overflow!
		// Convenience method for appending strings to string arrays.
		static <T> T[] append(T[] arr, T element) {
			final int N = arr.length;
			arr = Arrays.copyOf(arr, N + 1);
			arr[N] = element;
			return arr;
		}

		// Scrolls the passed listview to the bottom.
		private void scrollListViewToBottom(final ListView list) {
			list.post(new Runnable() {
				@Override
				public void run() {
					// Select the last row so it will scroll into view...
					list.setSelection(_adapter.getCount() - 1);
				}
			});
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class PhotosSectionFragment extends Fragment {

		ArrayList<Bitmap> _bitmapArray;
		ArrayList<String> _photoNames;
		GridView _grid;
		private static final int CAMERA_PIC_REQUEST = 1337;
		Timer _timer;
		ImageAdapter _adapter;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public PhotosSectionFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tab_photos,container, false);

			// Setup the timer
			_timer = new Timer();
			_timer.schedule(new TimerTask() {
				@Override
				public void run() {
					TimerMethod();
				}
			}, 0, 15000);

			_bitmapArray = new ArrayList<Bitmap>();
			_photoNames = new ArrayList<String>();

			_grid = (GridView) rootView.findViewById(R.id.photosGridView);

			_adapter = new ImageAdapter(getActivity(), _bitmapArray);
			_grid.setAdapter(_adapter);

			// Capture clicks on ListView items
			_grid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// Send single item click data to SingleItemView Class
					Intent i = new Intent(getActivity(), PhotoActivity.class);
					// Pass image
					i.putExtra("image", _bitmapArray.get(position));
					// Open PhotoActivity
					startActivity(i);
				}
			});

			_grid.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					_connected = Connectivity.getConnectionStatus(_context);
					if (!_connected) {
						Toast.makeText(getActivity(), "Please make sure you have an internet connection", Toast.LENGTH_LONG).show();
						return false;
					}
					ParseQuery<ParseObject> query = ParseQuery.getQuery("ImageWarehouse");
					query.whereEqualTo("fromUser", _fromUser);
					query.whereEqualTo("toUser", _toUser);
					query.whereEqualTo("imageName", _photoNames.get(position));
					query.findInBackground(new FindCallback<ParseObject>() {
						@Override
						public void done(List<ParseObject> images, ParseException e) {
							if (e == null) {
								Log.d("ImageWarehouse", "There are: " + images.size() + " images");
								for (ParseObject image : images) {
									image.deleteInBackground();
									Toast.makeText(_context, "Image deleted.", Toast.LENGTH_LONG).show();
								}
								refreshImageGridView();
							} else {
								Log.d("Messaging", "Error:" + e.getMessage());
							}	
						}
					});
					return false;
				}
			});

			Button sendButton = (Button) rootView.findViewById(R.id.cameraButton);
			sendButton.setOnClickListener(new View.OnClickListener() { 
				@Override
				public void onClick(View v) {
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
				}
			});

			return rootView;
		}

		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
				if(data.getAction() != null)
				{
					Bitmap image = (Bitmap) data.getExtras().get("data");

					if(image != null) {       
						saveImage(image);
					}
				}
			}
		}

		private void TimerMethod()
		{
			//This method is called directly by the timer
			//and runs in the same thread as the timer.

			//We call the method that will work with the UI
			//through the runOnUiThread method.
			getActivity().runOnUiThread(Timer_Tick);
		}

		private Runnable Timer_Tick = new Runnable() {
			public void run() {

				//This method runs in the same thread as the UI.               

				//Do something to the UI thread here
				queryImages();
			}
		};

		@SuppressLint("SimpleDateFormat")
		void saveImage(Bitmap photo) {
			_connected = Connectivity.getConnectionStatus(_context);
			if (!_connected) {
				Toast.makeText(getActivity(), "Please make sure you have an internet connection", Toast.LENGTH_LONG).show();
			} else {
				// Getting the SDCard Path
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyddhhmm");
				String currentDateandTime = sdf.format(new Date());
				String imageName = "image_"+ currentDateandTime +".jpeg";

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				// get byte array here
				byte[] data = stream.toByteArray();

				ParseObject imageWarehourseObj = new ParseObject("ImageWarehouse");
				imageWarehourseObj.put("fromUser", _fromUser);
				imageWarehourseObj.put("toUser", _toUser);
				imageWarehourseObj.put("imageName", imageName);
				if (data != null){
					ParseFile imgFile = new ParseFile (imageName, data);
					imgFile.saveInBackground();
					imageWarehourseObj.put("image", imgFile);
				}
				imageWarehourseObj.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						// Handle success or failure here ...
						if (e == null) {
							queryImages();
						} else {
							Log.d("queryImages", "Error:" + e.getMessage());
						}
					}
				});
			}
		}

		public void queryImages() {
			_connected = Connectivity.getConnectionStatus(_context);
			if (!_connected) {
				Toast.makeText(getActivity(), "Please make sure you have an internet connection", Toast.LENGTH_LONG).show();
			} else {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("ImageWarehouse");
				query.whereEqualTo("fromUser", _fromUser);
				query.whereEqualTo("toUser", _toUser);
				query.setLimit(30);
				query.orderByAscending("createdAt");
				query.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(List<ParseObject> images, ParseException e) {
						if (e == null) {
							Log.d("Image Warehouse", "There are: " + images.size() + " images");
							_bitmapArray = new ArrayList<Bitmap>();
							_photoNames = new ArrayList<String>();
							for (final ParseObject image : images) {
								ParseFile imageFile = (ParseFile)image.get("image");
								imageFile.getDataInBackground(new GetDataCallback() {
									public void done(byte[] data, ParseException e) {
										if (e == null) {
											// data has the bytes for the image
											Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length);
											_bitmapArray.add(bitmap); // Add a bitmap
											_photoNames.add((String) image.getString("imageName")); // Add image name
										} else {
											// something went wrong
											Log.d("queryImages.getDataInBackground", "Error:" + e.getMessage());
										}
										refreshImageGridView();
									}
								});
							}
						} else {
							Log.d("queryImages", "Error:" + e.getMessage());
						}	
					}
				});
			}
		}

		public void refreshImageGridView() {
			// Pass results to GridViewAdapter Class
			_adapter = new ImageAdapter(getActivity(), _bitmapArray);
			_grid.setAdapter(_adapter);
			((ImageAdapter)_grid.getAdapter()).notifyDataSetChanged();
		}
	}
}

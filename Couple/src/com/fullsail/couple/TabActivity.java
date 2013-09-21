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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class TabActivity extends FragmentActivity implements
		ActionBar.TabListener {
	
	static Context _context;

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
		
		String[] _message;
	    String[] _time;
	    ListView _list;
	    ListViewAdapter _adapter;
	    EditText _chatEditText;
	    String _userName;
		
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public ChatSectionFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tab_chat, container, false);
			
	        // Generate sample data
			_message = new String[] {};
			_time = new String[] {};
			
			SharedPreferences preferences = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
	        _userName = preferences.getString("username", "") + ": ";
	        
	        // Pass results to ListViewAdapter Class
			_adapter = new ListViewAdapter(getActivity(), _message, _time, _userName);
			
			// Locate the ListView
			_list = (ListView) rootView.findViewById(R.id.chatListView);
			
	        // Binds the Adapter to the ListView
			if (_list != null) {
				_list.setAdapter(_adapter);
			}
			
			_chatEditText = (EditText) rootView.findViewById(R.id.chatEditText);
			
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
		
		public void updateChatLog(String text) {
			_message = append(_message, text);
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
			String currentDateandTime = sdf.format(new Date());
			_time = append(_time, currentDateandTime);
			_chatEditText.setText("");
			
			// Pass results to ListViewAdapter Class
			_adapter = new ListViewAdapter(getActivity(), _message, _time, _userName);
			_list.setAdapter(_adapter);
			((ListViewAdapter)_list.getAdapter()).notifyDataSetChanged();
			
			scrollListViewToBottom(_list);
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
		
		int[] _imageSource;
		GridView _grid;
	    GridViewAdapter _adapter;
		
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public PhotosSectionFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tab_photos,container, false);

			_imageSource = new int[] { R.drawable.cat1, R.drawable.cat2,
					R.drawable.cat3, R.drawable.cat4, R.drawable.cat5, R.drawable.cat6, R.drawable.cat7};

			_adapter = new GridViewAdapter(getActivity(), _imageSource);

			// Locate the GridView
			_grid = (GridView) rootView.findViewById(R.id.photosGridView);

			_grid.setAdapter(new ImageAdapter(_context));
			
			return rootView;
		}
		
		void saveImage(Bitmap photo) {
			// Getting the SDCard Path
			File sdcard = Environment.getExternalStorageDirectory();
			File editedFile = new File( sdcard, "myphoto.jpeg" );
			FileOutputStream fOut;
			try {
				fOut = new FileOutputStream( editedFile );
				photo.compress( Bitmap.CompressFormat.JPEG, 90, fOut );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

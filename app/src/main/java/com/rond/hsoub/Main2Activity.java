package com.rond.hsoub;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rond.hsoub.API.CustomAPI;
import com.rond.hsoub.API.CustomAPIEnum;
import com.rond.hsoub.API.DownloadImageTask;
import com.rond.hsoub.API.JsonLinks;
import com.rond.hsoub.Classes.DBHelper;
import com.rond.hsoub.Classes.GeneralInstances;
import com.rond.hsoub.Classes.Serialization;
import com.rond.hsoub.Models.community;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Main2Activity instance;

    @SafeVarargs
    public final void setFragment(android.support.v4.app.Fragment newFragment, Pair<String, String>... Parms){
        Bundle bundle = new Bundle();
        for (Pair<String, String> Parm : Parms) bundle.putString(Parm.first, Parm.second);
        newFragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment).addToBackStack("TAG_FRAGMENT");
        fragmentTransaction.commit();

    }

    public void showPopup(View anchorView) {

        //// TODO: 12/24/2016 Check with large lines of text
        View popupView = getLayoutInflater().inflate(R.layout.fragment_add_new_post, null);



        PopupWindow popupWindow = new PopupWindow(popupView, Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT, true);

        final TextView btnAddLink = (TextView) popupView.findViewById(R.id.btnAddLink);
        final TextView btnAddComment = (TextView) popupView.findViewById(R.id.btnAddComment);
        final EditText txtCommunity = (EditText) popupView.findViewById(R.id.txtCommunity);
        final EditText txtDirectLink = (EditText) popupView.findViewById(R.id.txtDirectLink);
        final EditText txtFillTitle = (EditText) popupView.findViewById(R.id.txtFillTitle);
        final EditText txtDescription = (EditText) popupView.findViewById(R.id.txtDescription);
        final CheckBox ckbxOpenTerms = (CheckBox) popupView.findViewById(R.id.ckbxOpenTerms);
        final Button btnSendTopic = (Button) popupView.findViewById(R.id.btnSendTopic);

        txtFillTitle.setFocusableInTouchMode(true);
        txtFillTitle.requestFocus();
        if(txtFillTitle.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        btnAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddLink.setBackgroundColor(Color.parseColor("#FFAA00"));
                btnAddComment.setBackgroundColor(Color.parseColor("#FFFFFF"));
                txtDirectLink.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.GONE);
            }
        });

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddLink.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btnAddComment.setBackgroundColor(Color.parseColor("#FFAA00"));
                txtDirectLink.setVisibility(View.GONE);
                txtDescription.setVisibility(View.VISIBLE);
            }
        });

        //tv.setText(....);

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);
        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x99CCCCCC));
        int location[] = new int[2];
        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);
        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, location[0], location[1] + anchorView.getHeight());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("hsoub", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);

        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Set the fragment initially
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new MainFragment()).addToBackStack("TAG_FRAGMENT");
        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.bringToFront();
                drawerView.requestLayout();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //navViewMenu
        final Menu menu = navigationView.getMenu();

        final SubMenu subMenu = menu.addSubMenu(getResources().getString(R.string.communities));
        new CustomAPI(CustomAPIEnum.getLatestCommunities){
            @Override
            public void getLatestCommunitiesListener(ArrayList<community> Communities) {
                // TODO: 12/30/2016 :/
                if(Communities==null){
                    Communities=(ArrayList<community>) Serialization.deserializeObject(new DBHelper(getApplicationContext()).getDictionary("communityDrawer"));
                }
                if(Communities!=null) {
                    new DBHelper(getApplicationContext()).setDictionary("communityDrawer",Serialization.serializeObject(Communities));
                    for (int i = 0; i < Communities.size(); i++) {
                        MenuItem item = subMenu.add(Communities.get(i).getName());
                        final int finalI = i;
                        final ArrayList<community> finalCommunities = Communities;
                        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                setFragment(new PostsListFragment(), new Pair<>("link", JsonLinks.get_more_posts(finalCommunities.get(finalI).getID())));
                                return false;
                            }
                        });
                        item.setIcon(R.drawable.ic_search);
                    }
                }
                MenuItem itemSeeMoreCommunities = subMenu.add(getResources().getString(R.string.see_more_communities));
                itemSeeMoreCommunities.setIcon(R.drawable.ic_search);
                itemSeeMoreCommunities.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        setFragment(new communitiesFragment());
                        return false;
                    }
                });
            }
        }.execute("{\"s\":\"\",\"search_community_slug\":\"\",\"community_ids\":[]}");

        View headerView = navigationView.getHeaderView(0);
        TextView lblUserName = (TextView) headerView.findViewById(R.id.username);
        TextView lblEmail = (TextView) headerView.findViewById(R.id.email);
        CircleImageView imgProfileImage = (CircleImageView)headerView.findViewById(R.id.profile_image);

        View.OnClickListener openProfile = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new profileFragment(),new Pair<>("userName",GeneralInstances.user.getUserLink()));
            }
        };
        if(isLoggedIn) {
            lblUserName.setOnClickListener(openProfile);
            lblEmail.setOnClickListener(openProfile);
            imgProfileImage.setOnClickListener(openProfile);
        }

        FloatingActionButton fabSettings = (FloatingActionButton) headerView.findViewById(R.id.fabSettings);
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new settingsFragment());
            }
        });

        final FloatingActionButton fabAddNewTopic = (FloatingActionButton) findViewById(R.id.fabAddNewTopic);
        fabAddNewTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
            showPopup(fabAddNewTopic);
            }
        });
        if(!isLoggedIn){
            //fabAddNewTopic.setVisibility(View.GONE);
            fabSettings.setVisibility(View.GONE);

            lblUserName.setText(getResources().getString(R.string.hello_user));
            lblEmail.setText(getResources().getString(R.string.hello_email));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imgProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile, getApplicationContext().getTheme()));
            } else {
                imgProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile));
            }

            menu.findItem(R.id.nav_my_profile).setVisible(false);
            menu.findItem(R.id.nav_notifications).setVisible(false);
            menu.findItem(R.id.nav_messages).setVisible(false);
            menu.findItem(R.id.nav_settings).setVisible(false);

            //// TODO: 12/24/2016 hide notification & messages buttons in top options menu
        } else {
            fabAddNewTopic.setVisibility(View.VISIBLE);
            fabSettings.setVisibility(View.VISIBLE);

            lblUserName.setText(getResources().getString(R.string.hello_user_known).replace("X",GeneralInstances.user.getFirst_name()));
            lblEmail.setText(GeneralInstances.user.getEmail());

            new DownloadImageTask(imgProfileImage)
                    .execute(GeneralInstances.user.getAvatar());

            menu.findItem(R.id.nav_my_profile).setVisible(true);
            menu.findItem(R.id.nav_notifications).setVisible(true);
            menu.findItem(R.id.nav_messages).setVisible(true);
            menu.findItem(R.id.nav_settings).setVisible(true);
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.replaceAll(" ", "+");
                setFragment(new PostsListFragment(), new Pair<>("link", JsonLinks.searchLink(query)), new Pair<>("searchPrefix", "{\"s\":\"" + query + "\",\"search_community_slug\":\"\",\"filter\":\"popular\",\"in\":\"posts\",\"post_ids\":["));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_message) {
            setFragment(new messagesFragment());
        } else if(id == R.id.action_notification){
            setFragment(new notificationsFragment());
        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            setFragment(new profileFragment(),new Pair<>("userName",GeneralInstances.user.getUserLink()));
        } else if (id == R.id.nav_notifications) {
            setFragment(new notificationsFragment());
        } else if (id == R.id.nav_messages) {
            setFragment(new messagesFragment());
        } else if (id == R.id.nav_settings) {
            setFragment(new settingsFragment());
        } else if (id == R.id.nav_posts_list) {
            setFragment(new MainFragment());
        } else if (id == R.id.nav_about_website) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(JsonLinks.web_about_hsoub));
            startActivity(intent);
        } else if (id == R.id.nav_about_app) {
            setFragment(new webBrowserFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
package zesters.sistemaderegistrofenal;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    protected View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Indica que a este fragmento le gustaría influir en el set de acciones en la barra de acción.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        View header = inflater.inflate(R.layout.fragment_header_navigation_drawer, container, false);
        mDrawerListView.addHeaderView(header);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        ArrayList<DrawerItem> dataList = new ArrayList<DrawerItem>();

        dataList.add(new DrawerItem(getString(R.string.title_add_register)));
        dataList.add(new DrawerItem(getString(R.string.add_customer), 0));
        dataList.add(new DrawerItem(getString(R.string.add_book), 0));
        dataList.add(new DrawerItem(getString(R.string.subtitle_search_register)));
        dataList.add(new DrawerItem(getString(R.string.title_search_customer), 0));
        dataList.add(new DrawerItem(getString(R.string.title_search_book), 0));
        dataList.add(new DrawerItem(getString(R.string.subtitle_delete)));
        dataList.add(new DrawerItem(getString(R.string.title_activity_elimina_cliente), 0));
        dataList.add(new DrawerItem(getString(R.string.subtitle_list_register)));
        dataList.add(new DrawerItem(getString(R.string.title_activity_lista_libros),0));
        dataList.add(new DrawerItem(getString(R.string.title_activity_lista_editorial),0));
        dataList.add(new DrawerItem(getString(R.string.title_activity_modifica_registro)));
        dataList.add(new DrawerItem(getString(R.string.title_activity_modifica_cliente),0));
        dataList.add(new DrawerItem(getString(R.string.title_activity_modifica_libro),0));

        CustomDrawerAdapter adapter = new CustomDrawerAdapter(getActivity(), R.layout.navigation_drawer_row,
                dataList);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     * Los usuarios de este fragmento deben llamar este método para establecer las interacciones de cajón de navegación.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setFocusableInTouchMode(false);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //ActionBarDrawerToggle une a las interacciones apropiadas
        //entre la barra de navegacion y la action bar
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                /*if (!mUserLearnedDrawer) {
                    //El usuario manualmente abrió el cajón; almacene esta bandera para prevenir automostracion
                    // de la barra de navegacion en el futuro
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }*/

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(20);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    public class DrawerItem {
        String ItemName;
        String title;

        public DrawerItem(String itemName, int imgResID) {
            super();
            ItemName = itemName;
        }

        public String getItemName() {
            return ItemName;
        }
        public void setItemName(String itemName) {
            ItemName = itemName;
        }

        public DrawerItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
    }

    public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

        Context context;
        List<DrawerItem> drawerItemList;
        int layoutResID;

        public CustomDrawerAdapter(Context context, int layoutResourceID,
                                   List<DrawerItem> listItems) {
            super(context, layoutResourceID, listItems);
            this.context = context;
            this.drawerItemList = listItems;
            this.layoutResID = layoutResourceID;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            DrawerItemHolder drawerHolder;
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                drawerHolder = new DrawerItemHolder();

                view = inflater.inflate(layoutResID, parent, false);
                drawerHolder.title = (TextView) view
                        .findViewById(R.id.title_nav_drawer);
                drawerHolder.ItemName = (TextView) view
                        .findViewById(R.id.option_nav_drawer);
                drawerHolder.itemLayout = (LinearLayout) view
                        .findViewById(R.id.item_layout);
                drawerHolder.titleLayout = (LinearLayout) view
                        .findViewById(R.id.header_layout);

                view.setTag(drawerHolder);

            } else {
                drawerHolder = (DrawerItemHolder) view.getTag();

            }

            DrawerItem dItem = this.drawerItemList.get(position);

            if (dItem.getTitle() != null) {
                drawerHolder.titleLayout.setVisibility(LinearLayout.VISIBLE);
                drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
                drawerHolder.title.setText(dItem.getTitle());

            } else {
                drawerHolder.titleLayout.setVisibility(LinearLayout.INVISIBLE);
                drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);

                drawerHolder.ItemName.setText(dItem.getItemName());
            }
            return view;
        }

        class DrawerItemHolder {
            TextView ItemName;
            TextView title;
            LinearLayout itemLayout;
            LinearLayout titleLayout;
        }
    }
}

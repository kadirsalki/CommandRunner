package destekinfo.com.commandrunner.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import destekinfo.com.commandrunner.R;
import destekinfo.com.commandrunner.adapters.GridAdapter;
import destekinfo.com.commandrunner.db.DatabaseHandler;
import destekinfo.com.commandrunner.host.App;
import destekinfo.com.commandrunner.host.AppList;
import destekinfo.com.commandrunner.utils.Utils;


public class DeployFragment extends Fragment {

    GridView gridView;
    DatabaseHandler handler;
    AppList appList;
    GridAdapter adapterViewAndroid;
    EditText appNameText;
    EditText passwordText;
    LayoutInflater inf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inf = inflater;
        handler = new DatabaseHandler(this.getContext());
        appList = handler.getAppList(1);

        View view =  inflater.inflate(R.layout.fragment_deploy, container, false);
        setHasOptionsMenu(true);

        adapterViewAndroid = new GridAdapter(this.getContext(), appList);
        gridView = (GridView)view.findViewById(R.id.deploy_grid_view);
        gridView.setAdapter(adapterViewAndroid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                try{
                    final String appName = appList.get(i).getAppName();


                    View passView = inf.inflate(R.layout.pass_dialog, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(DeployFragment.this.getContext()).create();
                    alertDialog.setCancelable(false);
                    passwordText = (EditText)passView.findViewById(R.id.passwordText);

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DEPLOY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pasword = passwordText.getText().toString();
                            if(pasword.isEmpty()){
                                Toast.makeText(DeployFragment.this.getContext(), "Password can not be empty "
                                        , Toast.LENGTH_LONG).show();
                            }else{
                                try {
                                    String result = Utils.deployApp("ansible",pasword,"10.35.252.112",appName);
                                    Toast.makeText(DeployFragment.this.getContext(), "Result :"+result
                                            , Toast.LENGTH_LONG).show();
                                }catch (Exception e){
                                    Toast.makeText(DeployFragment.this.getContext(), "Restart exception"+e.getMessage()
                                            , Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setView(passView);
                    alertDialog.show();

                }catch (Exception e){
                    Toast.makeText(DeployFragment.this.getContext(), "exception"+e.getMessage()
                            , Toast.LENGTH_LONG).show();
                    Log.e("exception",e.getMessage());
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int post = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(DeployFragment.this.getContext());
                builder
                        .setTitle("Delete App")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handler.deleteApp(appList.get(post).getId());
                                appList.remove(post);
                                createNewAdapter();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
                return true;
            }
        });

        return view;
    }

    private void createNewDeployAppAlert(){
        final View view = inf.inflate(R.layout.deploy_app_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(DeployFragment.this.getContext()).create();
        alertDialog.setCancelable(false);

        appNameText = (EditText)view.findViewById(R.id.deployAppNameText);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String appName = appNameText.getText().toString();
                if(appName.isEmpty()){
                    Toast.makeText(DeployFragment.this.getContext(), "App name can not be empty "
                            , Toast.LENGTH_LONG).show();
                }else{
                    addNewDeployApp(appName);
                    createNewAdapter();
                }
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    public void createNewAdapter(){
        adapterViewAndroid = new GridAdapter(DeployFragment.this.getContext(), appList);
        gridView.setAdapter(adapterViewAndroid);
    }

    public void addNewDeployApp(String appName){
        App app = new App();
        app.setHostName("");
        app.setAppName(appName);
        app.setCommand("");
        app.setType(1);
        handler.addApp(app);
        appList.add(app);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_user) {
            createNewDeployAppAlert();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
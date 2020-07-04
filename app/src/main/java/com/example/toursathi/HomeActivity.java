package com.example.toursathi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toursathi.Adapter.GroupListAdapter;
import com.example.toursathi.Constant.ApiInterface;
import com.example.toursathi.Constant.ApiUtils;
import com.example.toursathi.ResponseModel.User;
import com.example.toursathi.ResponseModel.singleGroupDetailModel;
import com.example.toursathi.ResponseModel.singleGroupModel;
import com.example.toursathi.Service.LocationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GroupListAdapter groupListAdapter;
    Button btncreategroup,btnjoingroup;
    User user = new User();
    List<singleGroupModel> grouplist = new ArrayList<>();
    List<singleGroupDetailModel> groupdetaillist = new ArrayList<>();
    singleGroupModel singleGroupModel = new singleGroupModel();
    String groupname,groupcode;
    public ApiInterface apiInterface;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btncreategroup = findViewById(R.id.btncreategroup);
        btnjoingroup= findViewById(R.id.btnjoingroup);
        apiInterface= ApiUtils.getAPIService();
        user = ((UserClient)getApplicationContext()).getUser();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        final Call<List<singleGroupModel>> callgrouplist = apiInterface.getGroupList(user.getMobileno());
        callgrouplist.enqueue(new Callback<List<singleGroupModel>>() {
            @Override
            public void onResponse(Call<List<singleGroupModel>> call, Response<List<singleGroupModel>> response) {
                if (response.isSuccessful())
                {
                    grouplist = response.body();
                    Toast.makeText(HomeActivity.this,"grouplist got",Toast.LENGTH_SHORT).show();
                    ((UserClient)getApplicationContext()).setSingleGroupModelList(grouplist);
                    groupListAdapter = new GroupListAdapter(grouplist,HomeActivity.this);
                    recyclerView.setAdapter(groupListAdapter);
                }
                else {
                    Toast.makeText(HomeActivity.this,String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<singleGroupModel>> call, Throwable t) {
                Toast.makeText(HomeActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        groupListAdapter.setOnItemClickListener(new GroupListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                Call<List<singleGroupDetailModel>> callgroupdetail = apiInterface.getGroupDetails(grouplist.get(position).getGroupcode());
                callgroupdetail.enqueue(new Callback<List<singleGroupDetailModel>>() {
                    @Override
                    public void onResponse(Call<List<singleGroupDetailModel>> call, Response<List<singleGroupDetailModel>> response) {
                        if (response.isSuccessful())
                        {
                            groupdetaillist = response.body();
                            ((UserClient)getApplicationContext()).setGroupdetaillist(groupdetaillist);
                            ((UserClient)getApplicationContext()).setSingleGroupModel(grouplist.get(position));
                            Toast.makeText(HomeActivity.this,"group details got",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this,GroupActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(HomeActivity.this,String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<singleGroupDetailModel>> call, Throwable t) {
                        Toast.makeText(HomeActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        btncreategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.groupnamedialoglayout);
                dialog.show();

                final EditText editgroupname = (EditText) dialog.findViewById(R.id.editgroupname);
                Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
                Button btncreate = (Button) dialog.findViewById(R.id.btncreate);

                btncreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupname = editgroupname.getText().toString();
                        if (groupname.equals(""))
                        {
                            Toast.makeText(HomeActivity.this,"please enter group name",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Call<singleGroupModel> callgroupmodel = apiInterface.getGroupModel(groupname,user.getMobileno(),user.getName());
                            callgroupmodel.enqueue(new Callback<singleGroupModel>() {
                                @Override
                                public void onResponse(Call<singleGroupModel> call, Response<singleGroupModel> response) {
                                    if (response.isSuccessful())
                                    {
                                        singleGroupModel = response.body();
                                        grouplist.add(singleGroupModel);
                                        ((UserClient)getApplicationContext()).setSingleGroupModelList(grouplist);
                                        groupListAdapter.notifyDataSetChanged();
                                    }
                                    else {
                                        Toast.makeText(HomeActivity.this,String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<singleGroupModel> call, Throwable t) {
                                    Toast.makeText(HomeActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }

                    }
                });

                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });


        btnjoingroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.groupcodedialoglayout);
                dialog.show();

                final EditText editgroupcode = (EditText) dialog.findViewById(R.id.editgroupcode);
                Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
                Button btnjoin = (Button) dialog.findViewById(R.id.btnjoin);

                btnjoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupcode = editgroupcode.getText().toString();
                        if (groupcode.equals(""))
                        {
                            Toast.makeText(HomeActivity.this,"please enter group code",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Call<singleGroupModel> callgroupmodel = apiInterface.getGroupModel(groupname,user.getMobileno(),user.getName());
                            callgroupmodel.enqueue(new Callback<singleGroupModel>() {
                                @Override
                                public void onResponse(Call<singleGroupModel> call, Response<singleGroupModel> response) {
                                    if (response.isSuccessful())
                                    {
                                        singleGroupModel = response.body();
                                        grouplist.add(singleGroupModel);
                                        ((UserClient)getApplicationContext()).setSingleGroupModelList(grouplist);
                                        groupListAdapter.notifyDataSetChanged();
                                    }
                                    else {
                                        Toast.makeText(HomeActivity.this,String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<singleGroupModel> call, Throwable t) {
                                    Toast.makeText(HomeActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }

                    }
                });

                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        startLocationService();
    }


    public void stopLocationService()
    {
        if(isLocationServiceRunning()){
            Intent serviceIntent = new Intent(this, LocationService.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                HomeActivity.this.stopService(serviceIntent);
            }else{
                stopService(serviceIntent);
            }
        }
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(this, LocationService.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

                HomeActivity.this.startForegroundService(serviceIntent);
            }else{
                startService(serviceIntent);
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.toursathi.Service.LocationService".equals(service.service.getClassName())) {
                //Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        //Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }
}

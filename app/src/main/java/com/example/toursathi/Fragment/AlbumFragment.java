package com.example.toursathi.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toursathi.Adapter.GroupListAdapter;
import com.example.toursathi.Adapter.albumadapter;
import com.example.toursathi.Constant.ApiInterface;
import com.example.toursathi.HomeActivity;
import com.example.toursathi.Model.UploadImgModel;
import com.example.toursathi.R;
import com.example.toursathi.ResponseModel.User;
import com.example.toursathi.ResponseModel.singleGroupModel;
import com.example.toursathi.UserClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AlbumFragment extends Fragment {
    private static final int PICK_IMG = 1;
    View v;
    Button btnupload;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    List<UploadImgModel> uploadedimglist=new ArrayList<>();


    RecyclerView recyclerView;
    albumadapter albumadapter;

    singleGroupModel singleGroupModel = new singleGroupModel();
    User user = new User();

    ApiInterface apiInterface;
    public AlbumFragment() {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.albumfragmentlayout, container, false);
        singleGroupModel = ((UserClient)getContext().getApplicationContext()).getSingleGroupModel();
        user = ((UserClient)getContext().getApplicationContext()).getUser();
        recyclerView=v.findViewById(R.id.recyclerview);
        btnupload = v.findViewById(R.id.btnupload);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Album/"+singleGroupModel.getGroupcode());
        mStorageRef = FirebaseStorage.getInstance().getReference("Album/"+singleGroupModel.getGroupcode());
        getUploadedImages("firsttimeopen");

        //pusher implementation
        PusherOptions options = new PusherOptions();
        options.setCluster("ap2");
        Pusher pusher = new Pusher("dc0a97d420312dc803d2", options);


        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {

            }

            @Override
            public void onError(String message, String code, Exception e) {
                Toast.makeText(getContext(),"There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e,Toast.LENGTH_SHORT).show();
            }
        }, ConnectionState.ALL);



        Channel channel = pusher.subscribe("channel"+singleGroupModel.getGroupcode());

        channel.bind("uploadimageevent", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                    getUploadedImages("pusherevent");
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMG);
            }
        });

        return v;
    }

    void getUploadedImages(final String indication)
    {
        uploadedimglist.clear();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadImgModel uploadedimg = postSnapshot.getValue(UploadImgModel.class);
                    uploadedimglist.add(uploadedimg);
                }
                if (indication.equals("firsttimeopen"))
                {
                    albumadapter = new albumadapter(uploadedimglist,getContext());
                    recyclerView.setAdapter(albumadapter);
                }

                if (indication.equals("pusherevent"))
                {
                    albumadapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK
                && data != null && data.getClipData() != null) {
                    List<Uri> selectedimglist = new ArrayList<>();
                    int count = data.getClipData().getItemCount();

                    int CurrentImageSelect = 0;

                    while (CurrentImageSelect < count) {
                        Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                        selectedimglist.add(imageuri);
                        CurrentImageSelect = CurrentImageSelect + 1;
                    }
                    for (int i=0; i<selectedimglist.size();i++)
                    {
                        uploadFile(selectedimglist.get(i),i,count);
                    }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Uri mImageUri, final int whichnum, final int count) {
        if (mImageUri != null) {
            final String imgname = System.currentTimeMillis()+"."+ getFileExtension(mImageUri);
            final StorageReference fileReference = mStorageRef.child(imgname);

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {

                                    UploadImgModel uploadImgModel = new UploadImgModel(uri.toString(),imgname,user.getName());
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(uploadImgModel);
                                    if (whichnum==count)
                                    {
                                        Call<String> uploadimgcall = apiInterface.uploadimagecall(singleGroupModel.getGroupcode(),user.getMobileno(),user.getName());
                                        uploadimgcall.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                if (response.isSuccessful())
                                                {
                                                    Toast.makeText(getContext(),"server infromed",Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(getContext(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


}

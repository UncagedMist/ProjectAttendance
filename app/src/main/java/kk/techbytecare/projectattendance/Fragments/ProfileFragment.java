package kk.techbytecare.projectattendance.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import kk.techbytecare.projectattendance.Common.Common;
import kk.techbytecare.projectattendance.R;

public class ProfileFragment extends Fragment {

    @BindView(R.id.profileAvatar)
    CircleImageView profileAvatar;

    private TextView txt_name,txt_phone,txtClg,txtUser;
    private Button btn_browse;
    private CircleImageView image_preview;

    private Uri selectedFileUri;

    private StorageReference storageReference;

    private CollectionReference userRef;


    public ProfileFragment() {
        userRef = FirebaseFirestore.getInstance().collection(Common.FIREBASE_DB);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @OnClick(R.id.txt_home)
    void navigateHome() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame,new HomeFragment());
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragment = inflater.inflate(R.layout.fragment_profile, container, false);

        Unbinder unbinder = ButterKnife.bind(this,myFragment);

        Toolbar toolbar = myFragment.findViewById(R.id.toolbar);
        toolbar.setTitle("Profile Details");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        txtUser = myFragment.findViewById(R.id.txt_user);
        txt_name = myFragment.findViewById(R.id.txt_name);
        txt_phone = myFragment.findViewById(R.id.txt_phone);
        txtClg = myFragment.findViewById(R.id.txt_clg);

        profileAvatar = myFragment.findViewById(R.id.profileAvatar);

        profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageUpdateDialog();
            }
        });
        if (Common.isConnectedToInternet(getContext())) {
            loadUserDetails();
        }
        else    {
            Toast.makeText(getActivity(), "Please Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }
        return myFragment;
    }

    private void showImageUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload Profile Picture");
        builder.setMessage("Please Select & Upload the Picture.");
        builder.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View upload_profile_image = inflater.inflate(R.layout.upload_profile_image,null);

        image_preview = upload_profile_image.findViewById(R.id.image_preview);
        btn_browse = upload_profile_image.findViewById(R.id.btn_browse);

        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        builder.setView(upload_profile_image);

        builder.setPositiveButton("ADD PROFILE IMAGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedFileUri != null)    {
                    uploadPicture();
                    loadUserDetails();
                }
                else    {
                    Toast.makeText(getActivity(), "Please select a image.", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void uploadPicture() {
        final ProgressDialog mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Uploading...");
        mDialog.setCancelable(false);
        mDialog.show();

        String imageName = UUID.randomUUID().toString();
        final StorageReference imageFolder = storageReference.child("profile_images/"+imageName);

        Task uploadTask = imageFolder.putFile(selectedFileUri);

        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful())    {
                    Toast.makeText(getContext(), ""
                            +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                return imageFolder.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())    {
                    Uri  downloadUri = (Uri)task.getResult();
                    String downloadedPic = downloadUri.toString();
                    Map<String,Object> imgData = new HashMap<>();
                    imgData.put("profileImage",downloadedPic);
                    userRef.document(Common.currentUser.getPhoneNumber()).update(imgData);
                    Toast.makeText(getContext(), "Image Uploaded...", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mDialog.dismiss();
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)   {

            if (requestCode == Common.REQUEST_CODE)  {

                if (data != null)   {

                    selectedFileUri = data.getData();

                    if (selectedFileUri != null && !selectedFileUri.getPath().isEmpty())    {
                        image_preview.setImageURI(selectedFileUri);
                        btn_browse.setText("Image Selected...");
                    }
                    else {
                        Toast.makeText(getActivity(), "Image Not Selected...", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }

    private void loadUserDetails() {

        if (Common.currentUser != null) {
            txtUser.setText(new StringBuilder(Common.currentUser.getfName())
                    .append("'s ")
                    .append("Profile")
            );
            txt_name.setText(new StringBuilder(Common.currentUser.getfName())
                    .append(" ")
                    .append(Common.currentUser.getlName()));
            txt_phone.setText(Common.currentUser.getPhoneNumber());
            txtClg.setText(Common.currentUser.getCollegeName());

            if (!TextUtils.isEmpty(Common.currentUser.getProfileImage())) {

                Picasso.
                        get()
                        .load(Common.currentUser.getProfileImage())
                        .into(profileAvatar);
            }
            else    {
                try {
                    profileAvatar.setImageDrawable(getContext().getResources().getDrawable(R.drawable.default_avatar));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserDetails();
    }
}

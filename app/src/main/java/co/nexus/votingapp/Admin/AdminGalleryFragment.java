package co.nexus.votingapp.Admin;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import co.nexus.votingapp.Helpers.GlideApp;
import co.nexus.votingapp.Helpers.SpacingItemDecoration;
import co.nexus.votingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminGalleryFragment extends Fragment {
    private ImageButton btn;
    private final String TAG = "AdminGalleryFragment";
    private ArrayList<StorageReference> items;
    private RecyclerView recyclerView;
    private TextView textViewAdminGallery;
    private AdapterGridGallery mAdapter;
    private int size;
    private final int GALLERY_REQUEST_CODE = 100;

    public AdminGalleryFragment() {
        // Required empty public constructor
    }

    public AdminGalleryFragment(ImageButton btn){
        this.btn = btn;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = new ArrayList<>();

//        processData();

    }

    private void processData(){
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("gallery");

        items.clear();

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Log.d(TAG, "OnSuccess");
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                            Log.d(TAG, prefix.toString());
                        }

                        size = listResult.getItems().size();
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.d(TAG, "Item : "+item.getDownloadUrl() );
                            size--;
                            item.getDownloadUrl().addOnSuccessListener(getActivity(), new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    items.add(item);
                                    if(size==0){
                                        doAdapterStuff();
                                    }
                                }
                            });

                        }

//                        mAdapter.notifyDataSetChanged();

//                        doAdapterStuff();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Log.d(TAG, "Failed to get images");
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_gallery, container, false);

        textViewAdminGallery = root.findViewById(R.id.textViewAdminGallery);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add button clicked");
                pickFromGallery();
            }
        });

        processData();


        recyclerView = root.findViewById(R.id.recyclerViewAdminGallery);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, dpToPx(getContext(), 2), true));




        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
//                    imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

                    showConfimationDialog(imgDecodableString);

                    break;

            }
        else
            Log.d(TAG, "Result Code not match");

    }

    private void showConfimationDialog(String imgDecodableString){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_image_center);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        (dialog.findViewById(R.id.dialog_add_button_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.d(TAG, "Dialog Add Button clicked");
                uploadImageToStorage(imgDecodableString);
            }
        });

        (dialog.findViewById(R.id.dialog_image_button_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.d(TAG, "Dialog Dismiss Button clicked");
            }
        });

        ((ImageView)(dialog.findViewById(R.id.dialog_image_gallery))).setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));



        dialog.show();
    }


    private void uploadImageToStorage(String imgDecodableString){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("gallery");

        String path = imgDecodableString;
        String filename = path.substring(path.lastIndexOf("/")+1);
        StorageReference profRef = imagesRef.child(filename);

        Uri file = Uri.fromFile(new File(path));

        UploadTask uploadTask = profRef.putFile(file);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG, "Failed to upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d(TAG, "Success uploading");
                processData();
            }
        });
    }



    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }


    private void doAdapterStuff(){
        if(items.size()>0)
            textViewAdminGallery.setVisibility(View.GONE);
        else
            textViewAdminGallery.setVisibility(View.VISIBLE);

        mAdapter = new AdapterGridGallery(getContext(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridGallery.OnItemClickListener() {
            @Override
            public void onItemClick(View view, StorageReference ref, int position) {
                Log.d(TAG, "Item clicked : "+position);
                showPreviewDialog(position);
            }

//            @Override
//            public void onItemClick(View view, Integer obj, int position) {
//                Snackbar.make(parent_view, "Item " + position + " clicked", Snackbar.LENGTH_SHORT).show();
//            }
        });

        mAdapter.notifyDataSetChanged();
    }



    private void showPreviewDialog(int pos){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_image_preview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        (dialog.findViewById(R.id.dialog_imagebutton_preview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.d(TAG, "Dialog Delete Button clicked");
                deleteFileFromStorage(pos);
            }
        });


        ImageView imageView = (ImageView)(dialog.findViewById(R.id.dialog_image_gallery));
        GlideApp.with(getContext())
                .load(items.get(pos))
                .into(imageView);

        dialog.show();
    }

    private void deleteFileFromStorage(int pos){
        StorageReference ref = items.get(pos);
        ref.delete().addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Delete success");
                Toast.makeText(getContext(), "Successfully deleted!", Toast.LENGTH_SHORT).show();
                processData();
            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Delete failed");
                Toast.makeText(getContext(), "Failed to deleted!", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}

package cmpt276.as2.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.databinding.ActitivityEditSingleChildBinding;
import cmpt276.as2.parentapp.databinding.ActivityEditChildBinding;
import cmpt276.as2.parentapp.model.ChildManager;

public class ActitivityEditSingleChildActivity extends AppCompatActivity {

    private static ChildManager childManager;
    private ActitivityEditSingleChildBinding binding;
    private static final int GALLERY_REQUEST = 123;
    private static final int CAMERA_REQUEST = 100;


    ImageView childIcon;
    Button takePicButton;
    Button galleryButton;

    // Intent Tags.
    private static final String CHILD_NAME_TAG = "child name";
    private static final String CHILD_IDX_TAG = "child index tag";
    private static final String BITMAP_CHILD_TAG = "bitmap child tag";

    // Intent Data
    private static boolean isEditChild;
    private String childName;
    private int editChildIdx;
    private Bitmap icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActitivityEditSingleChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        childManager = ChildManager.getInstance();

        childIcon = findViewById(R.id.icon);
        takePicButton = findViewById(R.id.takeNewImg);
        galleryButton= findViewById(R.id.gallerySelectBtn);


        // Ask for camera run-time permissions.
        if (ContextCompat.checkSelfPermission(ActitivityEditSingleChildActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActitivityEditSingleChildActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_REQUEST);
        }

        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"pick profile"),GALLERY_REQUEST);
            }
        });


        extractIntent();
        Button finishedButton = findViewById(R.id.finishedButton);

        if (isEditChild) {
            setTitle("Editing Child");
            finishedButton.setText("Edit Child");
        } else {
            setTitle("Adding New Child");
            finishedButton.setText("Add Child");
        }

        addChild();

    }

    private void addChild() {

        Button addChildButton = findViewById(R.id.finishedButton);

        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get child's name
                EditText childNameSlot = findViewById(R.id.editTextPersonName);
                String childName = childNameSlot.getText().toString();

                // Get the current icon as a drawable and parse into bitmap.
                // Citation: https://stackoverflow.com/questions/25906707/i-want-to-get-image-from-imageview?answertab=votes#tab-top
                Bitmap image = null;
                if (childIcon.getDrawable() instanceof BitmapDrawable) {
                    BitmapDrawable drawable = (BitmapDrawable) childIcon.getDrawable();
                    image = drawable.getBitmap();
                }

                if (isEditChild) {
                    if (!childName.equals("")) {
                        childManager.getChildList().get(editChildIdx).setName(childName);
                        childManager.getChildList().get(editChildIdx).setIcon(image);
                    }
                } else {
                    childManager.addChild(childName, image);
                }

                finish();
            }
        });
    }

    // create Intent to pass data
    public static Intent makeIntent(Context context, String childName, int childIndex,
                                    boolean isEditChild, Bitmap icon) {
        Intent intent = new Intent(context, ActitivityEditSingleChildActivity.class);

        if (isEditChild) {
            //Convert child's icon to byte array and putExtra it
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.PNG, CAMERA_REQUEST, stream);
            byte[] byteArray = stream.toByteArray();

            intent.putExtra(CHILD_IDX_TAG, childIndex);
            intent.putExtra(BITMAP_CHILD_TAG, byteArray);
        }

        // Put data into intent
        intent.putExtra(CHILD_NAME_TAG, childName);

        ActitivityEditSingleChildActivity.isEditChild = isEditChild;
        return intent;
    }

    private void extractIntent() {
        EditText nameEditText = findViewById(R.id.editTextPersonName);

        Intent intent = getIntent();

        // inject data where needed.
        childName = intent.getStringExtra(CHILD_NAME_TAG);

        // decode image byte array and set the image if editing
        if (isEditChild) {
            byte[] byteArray = intent.getByteArrayExtra(BITMAP_CHILD_TAG);
            icon = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            childIcon.setImageBitmap(icon);
            editChildIdx = intent.getIntExtra(CHILD_IDX_TAG, 0);
        }

        nameEditText.setText(childName);
    }

    // Helpful resource: https://www.youtube.com/watch?v=XRD-lVwlSjU&t=245s
    // Takes pic and injects it into the imageView.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap= null;
        if (requestCode == CAMERA_REQUEST) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK&& data!=null){
            Log.e("data", "data is not null");
            Uri profileImage =data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profileImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        childIcon.setImageBitmap(bitmap);

    }

}
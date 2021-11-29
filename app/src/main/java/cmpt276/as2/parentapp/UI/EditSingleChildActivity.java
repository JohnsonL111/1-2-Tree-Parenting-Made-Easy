package cmpt276.as2.parentapp.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import cmpt276.as2.parentapp.R;

import cmpt276.as2.parentapp.databinding.ActitivityEditSingleChildBinding;
import cmpt276.as2.parentapp.model.ChildManager;

/**
 * Interface UI for editing/adding a single child.
 */
public class EditSingleChildActivity extends AppCompatActivity {

    private static ChildManager childManager;
    private ActitivityEditSingleChildBinding binding;
    private static final int GALLERY_REQUEST = 123;
    private static final int CAMERA_REQUEST = 100;
    public static final String CHILD_LIST = "childListNames";
    public static final String CHILD_LIST_TAG = "childList";

    ImageView childIcon;
    Button takePicButton;
    Button galleryButton;

    // Intent Tags.
    private static final String CHILD_NAME_TAG = "child name";
    private static final String CHILD_IDX_TAG = "child index tag";
    private static final String BITMAP_CHILD_TAG = "image";

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
        childManager = getChildData();

        childIcon = findViewById(R.id.icon);
        takePicButton = findViewById(R.id.takeNewImg);
        galleryButton = findViewById(R.id.gallerySelectBtn);


        // Ask for camera run-time permissions.
        if (ContextCompat.checkSelfPermission(EditSingleChildActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditSingleChildActivity.this, new String[]{
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
                startActivityForResult(Intent.createChooser(intent, "pick profile"), GALLERY_REQUEST);
            }
        });


        extractIntent();
        Button finishedButton = findViewById(R.id.finishedButton);

        if (isEditChild) {
            setTitle(getString(R.string.editChildTitle));
            finishedButton.setText(R.string.editChildTextButton);
        } else {
            setTitle(getString(R.string.addChildTitle));
            finishedButton.setText(R.string.addChildTextButton);
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
                String newChildName = childNameSlot.getText().toString();

                // Get the current icon as a drawable and parse into bitmap.
                // Citation: https://stackoverflow.com/questions/25906707/i-want-to-get-image-from-imageview?answertab=votes#tab-top
                Bitmap image = null;
                if (childIcon.getDrawable() instanceof BitmapDrawable) {
                    BitmapDrawable drawable = (BitmapDrawable) childIcon.getDrawable();
                    image = drawable.getBitmap();
                }

                // Cases for editing an existing child an adding a new child.
                if (isEditChild) {
                    if (changesAreValid(newChildName)) {
                        String currentChildName = childManager.getChildList().get(editChildIdx).getName();
                        childManager.getChildList().get(editChildIdx).setName(newChildName);
                        childManager.updateTaskChildNames(currentChildName, newChildName);
                        childManager.updateTaskHistoryChildNames(currentChildName, newChildName);
                    }
                    childManager.getChildList().get(editChildIdx).setIcon(encodeBase64(image));
                    childManager.coinFlip.changeIcon(newChildName, encodeBase64(image));
                } else {
                    if (changesAreValid(newChildName)) {
                        childManager.addChild(childName, encodeBase64(image));
                    }
                }
                saveChildData();
                finish();
            }
        });
    }

    private Boolean changesAreValid(String newChildName) {
        Boolean validChanges = false;
        if (childManager.checkIfNameExist(newChildName)) {
            if (!newChildName.equals(childManager.getChildList().get(editChildIdx).getName())) {
                Toast.makeText(EditSingleChildActivity.this, "Child Name Exists Already!", Toast.LENGTH_SHORT).show();
            }
        } else if (newChildName.equals("")) {
            Toast.makeText(EditSingleChildActivity.this, "Can't have no name!", Toast.LENGTH_SHORT).show();
        } else {
            validChanges = true;
        }
        return validChanges;
    }

    // create Intent to pass data
    public static Intent makeIntent(Context context, String childName, int childIndex,
                                    boolean isEditChild, Bitmap icon) {
        Intent intent = new Intent(context, EditSingleChildActivity.class);

        if (isEditChild) {
            intent.putExtra(CHILD_IDX_TAG, childIndex);
            intent.putExtra(BITMAP_CHILD_TAG, "bitmap.png");
        }

        // Put data into intent
        intent.putExtra(CHILD_NAME_TAG, childName);

        EditSingleChildActivity.isEditChild = isEditChild;
        return intent;
    }

    private void extractIntent() {
        EditText nameEditText = findViewById(R.id.editTextPersonName);

        Intent intent = getIntent();

        // inject data where needed.
        childName = intent.getStringExtra(CHILD_NAME_TAG);

        // decode image byte array and set the image if editing
        if (isEditChild) {
            Bitmap icon = null;
            String filename = getIntent().getStringExtra("image");
            try {
                FileInputStream is = this.openFileInput(filename);
                icon = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

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
        Bitmap bitmap = null;
        if (data != null) {
            if (requestCode == CAMERA_REQUEST) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
                Uri profileImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profileImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            bitmap = decodeBase64(childManager.getChildList().get(editChildIdx).getIcon());
        }
        childIcon.setImageBitmap(bitmap);

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static String encodeBase64(Bitmap img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imgBase64 = Base64.encodeToString(b, Base64.DEFAULT);
        return imgBase64;
    }

    private ChildManager getChildData() {
        SharedPreferences prefs = this.getSharedPreferences(CHILD_LIST_TAG, MODE_PRIVATE);
        if (!prefs.contains(CHILD_LIST)) {
            return ChildManager.getInstance();
        } else {
            Gson gson = new Gson();
            return gson.fromJson(prefs.getString(CHILD_LIST, ""), ChildManager.class);
        }
    }

    private void saveChildData() {
        SharedPreferences prefs = this.getSharedPreferences(CHILD_LIST_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Saves the class into json.
        Gson gson = new Gson();
        editor.putString(CHILD_LIST, gson.toJson(childManager));
        editor.apply();
    }

}
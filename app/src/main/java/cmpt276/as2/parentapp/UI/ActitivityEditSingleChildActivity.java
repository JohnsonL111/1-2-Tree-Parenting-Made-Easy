package cmpt276.as2.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.databinding.ActitivityEditSingleChildBinding;
import cmpt276.as2.parentapp.databinding.ActivityEditChildBinding;
import cmpt276.as2.parentapp.model.ChildManager;

public class ActitivityEditSingleChildActivity extends AppCompatActivity {

    private static ChildManager childManager;
    private ActitivityEditSingleChildBinding binding;
    ImageView childIcon;
    Button takePicButton;



    // Intent Tags.
    private static final String CHILD_NAME_TAG = "child name";

    // Intent Data
    static boolean isEditChild;
    String childName;
    //Bitmap childIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActitivityEditSingleChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        childManager = ChildManager.getInstance();

        childIcon = findViewById(R.id.icon);
        takePicButton = findViewById(R.id.takeNewImg);


        // Ask for camera run-time permissions.
        if (ContextCompat.checkSelfPermission(ActitivityEditSingleChildActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActitivityEditSingleChildActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
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
                // Get child's name.
                EditText childNameSlot = findViewById(R.id.editTextPersonName);
                String childName = childNameSlot.getText().toString();
                childManager.addChild(childName);
                finish();

            }
        });
    }

    // create Intent to pass data
    public static Intent makeIntent(Context context, String childName, int childIndex,
                                    boolean isEditChild) {
        Intent intent = new Intent(context, ActitivityEditSingleChildActivity.class);
        // Load child name and bit image through SP.
        intent.putExtra(CHILD_NAME_TAG, childName);

        ActitivityEditSingleChildActivity.isEditChild = isEditChild;
        return intent;
    }

    private void extractIntent() {
        Intent intent = getIntent();
        childName = intent.getStringExtra(CHILD_NAME_TAG);
        // extract bitmap here

        // inject data into sections
        EditText nameEditText = findViewById(R.id.editTextPersonName);

        nameEditText.setText(childName);
        // inject bitmap here
    }

    // Helpful resource: https://www.youtube.com/watch?v=XRD-lVwlSjU&t=245s
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            childIcon.setImageBitmap(bitmap);
        }
    }

}
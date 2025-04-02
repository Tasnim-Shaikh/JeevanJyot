package com.example.jeevanjyotandroidapplication;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;
import java.util.ArrayList;
import java.util.List;
public class Report extends DrawerBaseForPatient {
    ActivityDashboardBinding activityDashboardBinding;
    private Button selectFileButton, viewFilesButton;
    private TextView statusTextView;
    private ListView fileListView;
    private List<Uri> selectedFiles = new ArrayList<>();
    private List<String> fileNames = new ArrayList<>();
    private ArrayAdapter<String> fileAdapter;
    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    if (result.getData().getClipData() != null) {
                        // Multiple files selected
                        selectedFiles.clear();
                        fileNames.clear();
                        int count = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri fileUri = result.getData().getClipData().getItemAt(i).getUri();
                            selectedFiles.add(fileUri);
                            fileNames.add(fileUri.getLastPathSegment());
                        }
                    } else if (result.getData().getData() != null) {
                        // Single file selected
                        selectedFiles.clear();
                        fileNames.clear();
                        Uri fileUri = result.getData().getData();
                        selectedFiles.add(fileUri);
                        fileNames.add(fileUri.getLastPathSegment());
                    }
                    Toast.makeText(this, selectedFiles.size() + " file(s) selected", Toast.LENGTH_SHORT).show();
                    statusTextView.setText(selectedFiles.size() + " file(s) selected");
                    fileAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "No files selected", Toast.LENGTH_SHORT).show();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        // Inflate Doctor's layout inside the drawer
        getLayoutInflater().inflate(R.layout.report, activityDashboardBinding.contentFrame, true);

        selectFileButton = findViewById(R.id.uploadButton);
        viewFilesButton = findViewById(R.id.viewButton);
        statusTextView = findViewById(R.id.statusTextView);
        fileListView = findViewById(R.id.fileListView);

        fileAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
        fileListView.setAdapter(fileAdapter);
        fileListView.setVisibility(View.GONE);

        selectFileButton.setOnClickListener(v -> openFileChooser());
        viewFilesButton.setOnClickListener(v -> toggleFileListView());

        // Open file when clicking a ListView item
        fileListView.setOnItemClickListener((parent, view, position, id) -> openFile(selectedFiles.get(position)));
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        Intent chooser = Intent.createChooser(intent, "Select files");
        filePickerLauncher.launch(chooser);
    }

    private void toggleFileListView() {
        if (selectedFiles.isEmpty()) {
            Toast.makeText(this, "No files uploaded", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the list of files when "View Uploaded Documents" is clicked
        fileListView.setVisibility(View.VISIBLE);
    }

    private void openFile(Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No app available to open this file", Toast.LENGTH_SHORT).show();
        }
    }
}

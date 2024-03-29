package com.example.firebasecrudapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditCourseActivity extends AppCompatActivity {

    private TextInputEditText courseNameEdt, coursePriceEdt, courseSuitedForEdt, courseImgEdt, courseLinkEdt,courseDescEdt;
    private Button updateCourseBtn , deleteCourseBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabse;
    private DatabaseReference databaseReference;
    private String courseID;
    private  CourseRVModal courseRVModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        firebaseDatabse = FirebaseDatabase.getInstance();
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        courseSuitedForEdt = findViewById(R.id.idEdtCourseSuidtedFor);
        courseImgEdt = findViewById(R.id.idEdtCourseImageLink);
        courseLinkEdt = findViewById(R.id.idEdtCourseLink);
        courseDescEdt = findViewById(R.id.idEdtCourseDesc);
        updateCourseBtn = findViewById(R.id.idBtnUpdateCourse);
        deleteCourseBtn = findViewById(R.id.idBtnDeleteCourse);
        loadingPB = findViewById(R.id.idPBLoading);
        databaseReference = firebaseDatabse.getReference("Courses").child(courseID);

        if (courseRVModal != null){
            courseNameEdt.setText(courseRVModal.getCourseName());
            coursePriceEdt.setText(courseRVModal.getCoursePrice());
            courseSuitedForEdt.setText(courseRVModal.getBestSuitedFor());
            courseImgEdt.setText(courseRVModal.getCourseImg());
            courseLinkEdt.setText(courseRVModal.getCourseLink());
            courseDescEdt.setText(courseRVModal.getCourseDesc());
            courseID = courseRVModal.getCourseID();
        }



        courseRVModal = getIntent().getParcelableExtra("course");

        updateCourseBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                loadingPB.setVisibility(View.VISIBLE);
                String courseName = courseNameEdt.getText().toString();
                String coursePrice = coursePriceEdt.getText().toString();
                String courseSuitedFor = courseSuitedForEdt.getText().toString();
                String courseImg = courseImgEdt.getText().toString();
                String courseLink = courseLinkEdt.getText().toString();
                String courseDesc = courseDescEdt.getText().toString();

                Map<String,Object> map = new HashMap<>();
                map.put("courseName",courseName);
                map.put("coursePrice",coursePrice);
                map.put("bestSuitedFor",courseSuitedFor);
                map.put("courseImg",courseImg);
                map.put("courseLink",courseLink);
                map.put("courseDesc",courseDesc);
                map.put("courseID",courseID);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.updateChildren(map);
                        Toast.makeText(EditCourseActivity.this, "Course Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditCourseActivity.this,MainActivity.class));
                        //finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditCourseActivity.this, "Error Updating Course", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                    }
                });



            }
        });

        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                databaseReference.removeValue();
                Toast.makeText(EditCourseActivity.this, "Course Deleted Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditCourseActivity.this,MainActivity.class));
                // finish();
            }
        });

    }
}
package com.example.maaster.itp939juniorproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.LocaleDisplayNames;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ConnectDb extends AppCompatActivity {

    private  String mlabURL = "mongodb://hciadmin:cs374admin@ds053136.mlab.com:53136/student";
    private MongoDBConnection mongoDBConnection;
    private DBCursor cursor;
    private DBObject object;
    private ArrayList<Teacher> teacherList;
    private ArrayList<Course> courseList;
    private ArrayList<LinearLayout> layouts;

    private Student student;
    private Animator mCurrentAnimator;
    private ImageView iv;
    private ArrayList<FileInputStream> inputStreams;
    private Context context;
    private ImageView imageView ;


    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur very frequently.
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        context = getBaseContext();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        inputStreams = new ArrayList<>();
        teacherList = new ArrayList<>();
        courseList = new ArrayList<>();

        getTeacherFromDB();

        student = (Student) getIntent().getExtras().get("data");
        TextView textView = (TextView) findViewById(R.id.textname);
        String course=" ";


        textView.setText(student.getName()+course);
        getImage();


        ArrayList<Integer> imageID = new ArrayList<>();
        imageID.add(R.id.t1);
        imageID.add(R.id.t2);

        for (int i = 0; i < inputStreams.size(); i++) {
            imageView = (ImageView) findViewById(imageID.get(i));
            imageView.setImageBitmap(BitmapFactory.decodeStream(inputStreams.get(i)));

        }




        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cast image from imageView to bitmap
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                zoomImageFromThumb(iv, getImageUri(context, bitmap));

            }
        });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }//cast bitmap to uri

    private void zoomImageFromThumb(final View thumbView, Uri uri) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }



        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expaned_image);

        Picasso.with(this).load(uri).into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;


            }
        });
    }//zoom image

    public void getTeacherFromDB() {
        mongoDBConnection = new MongoDBConnection(ConstantProject. IP_PUBLIC_ADDRESS, "Teacher", "test");
        cursor = mongoDBConnection.getCursor();
        object = cursor.next();

        try {

            while(object!=null) {

                BasicDBList courses = (BasicDBList) object.get("course");

                for (int i = 0; i <courses.size() ; i++) {
                    DBObject courseObject = (DBObject) courses.get(i);
                    courseList.add(new Course((String) courseObject.get("section"),(String) courseObject.get("name")));

                }

                Teacher teacher = new Teacher();
                teacher.setName((String)object.get("name"));
                teacher.setCountCourse(courses.size());
                teacher.setImageName((String) object.get("image"));

                for (int i = 0; i < courses.size() ; i++) {
                    teacher.addCourse(courseList.get(i));
                }

                teacherList.add(teacher);
                object = cursor.next();
                courseList.clear();

            }



        } catch (Exception e) {
            //e.printStackTrace();

            return;
        }

    }

    public void getImage() {

        try {

            layouts = new ArrayList<>();

            MongoClient mongoClient = new MongoClient(new MongoClientURI(ConstantProject.IP_PUBLIC_ADDRESS));
            DB db = mongoClient.getDB("test");
            GridFS gfsPhoto = new GridFS(db, "photo");

            for (int i = 0; i < student.getCourse().size(); i++) {
                for (int j = 0; j <teacherList.size() ; j++) {
                    for (int k = 0; k <teacherList.get(k).getCountCourse() ; k++) {
                        if(student.getCourse().get(i).equalsIgnoreCase(teacherList.get(j).getCourse().get(k).getName())
                                && student.getSection().get(i).equalsIgnoreCase(teacherList.get(j).getCourse().get(k).getSection())) {
                            Log.v("cc"," " +teacherList.get(j).getImageName());

                            GridFSDBFile imageForOutput = gfsPhoto.findOne(teacherList.get(j).getImageName());

                            File outFile;
                            iv= (ImageView)findViewById(R.id.t1);
                            Context context =  iv.getContext();
                            outFile = File.createTempFile("xyz", null, context.getCacheDir());
                            imageForOutput.writeTo(outFile);

                            FileInputStream is = new FileInputStream(outFile);
                            inputStreams.add(is);

                           // iv.setImageBitmap(BitmapFactory.decodeStream(is));

                            outFile.delete();


                        }
                    }
                }
            }




        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

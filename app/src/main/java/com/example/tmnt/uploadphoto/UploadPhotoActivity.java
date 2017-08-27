package com.example.tmnt.uploadphoto;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UploadPhotoActivity extends AppCompatActivity {

    private NoScrollGridView gv;
    private Button next;

    private UploadPhotoAdapter mUploadPhotoAdapter;
    private List<Drawable> photos;
    private UploadDialogFragment fDialogFragment;

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    private static final String IMAGE_FILE_NAME = "ic_live.jpg";

    public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/";
    private Bitmap photo;
    private String picName;
    private String name;
    private String content;

    private static final String TAG = "UploadPhotoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_photo);

        gv = (NoScrollGridView) findViewById(R.id.gV);
        next = (Button) findViewById(R.id.btn_next);

        photos = new ArrayList<Drawable>();
        mUploadPhotoAdapter = new UploadPhotoAdapter(UploadPhotoActivity.this, photos);

        gv.setAdapter(mUploadPhotoAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == photos.size() || (photos.size() == 0 && position == 0)) {
                    showUploadDialog();
                }
            }
        });

    }

    private void showUploadDialog() {
        fDialogFragment = new UploadDialogFragment();
        fDialogFragment.show(getSupportFragmentManager(), "fDialogFragment");

        dialogOption(fDialogFragment);
    }

    public void dialogOption(final UploadDialogFragment fragment) {
        fragment.setOnDoOptionOnDialog(new UploadDialogFragment.OnDoOptionOnDialog() {
            @Override
            public void onTakePhoto(View view) {
                takePhoto();
            }

            @Override
            public void onChoosePhoto(View view) {
                pickPhoto();
            }

            @Override
            public void onCancel(View view) {

                fragment.dismiss();
            }
        });

        mUploadPhotoAdapter.setOnClickDelete(new UploadPhotoAdapter.OnClickDelete() {
            @Override
            public void onClickDelete(View view, int position) {
                if (photos.size() != 0) {
                    photos.remove(position);
                    mUploadPhotoAdapter.notifyDataSetChanged();
                }
            }
        });

        next.setClickable(false);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name != null && content != null) {
                    new LoadImageUploadTask().execute(name, content);
                }

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (fDialogFragment != null) {
            fDialogFragment.dismiss();
        }

    }

    /*
        * 从相册获取
        */
    public void pickPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    /*
     * 从相机获取
     */
    public void takePhoto() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            intentFromCapture
                    .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                            SD_PATH, IMAGE_FILE_NAME)));
        } else {

        }
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:

                    startPhotoZoom(data.getData());

                    break;
                case CAMERA_REQUEST_CODE:
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File tempFile = new File(SD_PATH,
                                IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
                                .show();
                    }

                    break;

                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }

                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例
        intent.putExtra("aspectX", 1.2);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 120);
        intent.putExtra("outputY", 80);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    private void getImageToView(Intent data) {
        if (data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                photo = extras.getParcelable("data");
                Log.i(TAG, "getImageToView: " + photos);
                name = UUID.randomUUID().toString() + ".jpg";

                // 图片保存到临时文件
                FileUtils.saveBitmap(photo, name);

                content = BitmapToBase64Util.bitmapToBase64(photo);
                //上传图片到服务器
                next.setClickable(true);

                Drawable drawable = new BitmapDrawable(photo);
                photos.add(drawable);
                mUploadPhotoAdapter.getData(photos);
                mUploadPhotoAdapter.notifyDataSetChanged();
            } else {
                Uri uri = data.getData();
                Drawable drawable = new BitmapDrawable(BitmapFactory.decodeFile(uri.getPath()));
                photos.add(drawable);
                mUploadPhotoAdapter.getData(photos);
                mUploadPhotoAdapter.notifyDataSetChanged();
            }

        }
    }

    class LoadImageUploadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(UploadPhotoActivity.this, s, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            return UploadPhoto.uploadPhoto(params[0], params[1]);
        }
    }
}

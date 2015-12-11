package com.llf.mybitmapapp.dialog;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.llf.mybitmapapp.R;
import com.llf.mybitmapapp.util.CommonUtils;

/**
 * Created by burke.li on 10/14/2015.
 */
public class DialogImageInformation extends DialogView implements View.OnClickListener {
    private View btn_cancel;
    private TextView tv_filePath;
    private TextView tv_mediaUri;
    private TextView tv_fileName;
    private TextView tv_description;
    private TextView tv_displayName;
    private TextView tv_size;
    private TextView tv_title;
    private TextView tv_mimeType;
    private TextView tv_height;
    private TextView tv_width;
    private TextView tv_orientation;

    private Uri contentUri;
    private String filePath;

    private String[] columns = new String[]{MediaStore.Images.ImageColumns.DESCRIPTION,
                                            MediaStore.Images.ImageColumns.DISPLAY_NAME,
                                            MediaStore.Images.ImageColumns.SIZE,
                                            MediaStore.Images.ImageColumns.TITLE,
                                            MediaStore.Images.ImageColumns.MIME_TYPE,
                                            MediaStore.Images.ImageColumns.HEIGHT,
                                            MediaStore.Images.ImageColumns.WIDTH,
                                            MediaStore.Images.ImageColumns.ORIENTATION};

    public DialogImageInformation(Context context, Uri contentUri, String filePath) {
        super(context, R.layout.dialog_image_information);
        this.context = context;
        this.contentUri = contentUri;
        this.filePath = filePath;
    }

    @Override
    public void showDialog() {
        super.showBegin(false);
        btn_cancel = super.dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        tv_filePath = (TextView) super.dialog.findViewById(R.id.tv_filePath);
        tv_mediaUri = (TextView) super.dialog.findViewById(R.id.tv_mediaUri);
        tv_fileName = (TextView) super.dialog.findViewById(R.id.tv_fileName);
        tv_description = (TextView) super.dialog.findViewById(R.id.tv_description);
        tv_displayName = (TextView) super.dialog.findViewById(R.id.tv_displayName);
        tv_size = (TextView) super.dialog.findViewById(R.id.tv_size);
        tv_title = (TextView) super.dialog.findViewById(R.id.tv_title);
        tv_mimeType = (TextView) super.dialog.findViewById(R.id.tv_mimeType);
        tv_height = (TextView) super.dialog.findViewById(R.id.tv_height);
        tv_width = (TextView) super.dialog.findViewById(R.id.tv_width);
        tv_orientation = (TextView) super.dialog.findViewById(R.id.tv_orientation);
        getMediaInformation(contentUri, filePath);
        super.showEnd();
    }

    private void getMediaInformation(Uri contentUri, String filePath) {

        tv_filePath.setText(filePath);
        tv_mediaUri.setText(contentUri.toString());
        Cursor cursor = context.getContentResolver().query(contentUri, columns, null, null, null);
        if(cursor.moveToFirst()){
            int indexDescription = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION);
            int indexDisplayName = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION);
            int indexSize = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION);
            int indexTitle = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION);
            int indexMimeType = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION);
            int indexHeight = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION);
            int indexWidth = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION);
            int indexOrientation = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION);
            tv_description.setText(cursor.getString(indexDescription));
            tv_displayName.setText(cursor.getString(indexDisplayName));
            tv_size.setText(cursor.getString(indexSize));
            tv_title.setText(cursor.getString(indexTitle));
            tv_mimeType.setText(cursor.getString(indexMimeType));
            tv_height.setText(cursor.getString(indexHeight));
            tv_width.setText(cursor.getString(indexWidth));
            tv_orientation.setText(cursor.getString(indexOrientation));
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_cancel:
                super.closeDialog(false);
                break;
        }
    }


}
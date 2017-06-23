/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boc.bocsoft.mobile.bocmobile.base.widget.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.crop.enjoycrop.EnjoyCropLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.crop.enjoycrop.core.BaseLayerView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.crop.enjoycrop.core.clippath.ClipPathLayerView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.crop.enjoycrop.core.clippath.ClipPathSquare;
import com.boc.bocsoft.mobile.bocmobile.base.widget.crop.enjoycrop.core.mask.ColorMask;
import com.boc.bocsoft.mobile.bocmobile.base.widget.crop.enjoycrop.utils.BitmapUtils;
import java.io.File;
import java.io.InputStream;

import static com.ft.key.audio.AudioDevice.mContext;

/**
 * The activity can crop specific region of interest from an image.可裁剪的activity
 */
public class CropImage extends Activity {

  private EnjoyCropLayout cropLayout;
  private Uri saveUri;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = this;

    //requestWindowFeature(Window.FEATURE_NO_TITLE);
    //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.view_crop);

    cropLayout = (EnjoyCropLayout) findViewById(R.id.view_crop);

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();

    Bitmap.CompressFormat mOutputFormat;
    Bitmap mBitmap = null;
    if (extras != null) {
      saveUri = (Uri) extras.getParcelable(MediaStore.EXTRA_OUTPUT);
      mBitmap = (Bitmap) extras.getParcelable("data");
    }

    //mx 某些照片无法加载
    if(mBitmap == null){
      Uri target = intent.getData();
      try {
        InputStream stream = mContext.getContentResolver().openInputStream(target);
        //mBitmap = Drawable.createFromStream(stream, null);
        mBitmap = BitmapFactory.decodeStream(stream);
        stream.close();
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    if (mBitmap == null) {
      finish();
      return;
    }

    setListener();
    // Make UI fullscreen.

    LogUtils.d("dding","---crop :"+mBitmap);
    cropLayout.setImage(mBitmap);
    defineCropParams();

  }

  private void setListener(){

    findViewById(R.id.discard).setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
          }
        });

    findViewById(R.id.save).setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            onSaveClicked();
          }
        });
  }

  private void defineCropParams() {
    //设置裁剪集成视图，这里通过一定的方式集成了遮罩层与预览框
    BaseLayerView layerView = new ClipPathLayerView(this);
    layerView.setMask(new ColorMask("#80000000")); //设置遮罩层,这里使用半透明的遮罩层
    ClipPathSquare clipPathSquare =
        new ClipPathSquare(getResources().getDisplayMetrics().widthPixels + 4);
    int border = (int) (getResources().getDisplayMetrics().density*1 +0.5);
    clipPathSquare.setBorderWidth(border);
    clipPathSquare.setBorderColor(0xfffefefe);
    layerView.setShape(clipPathSquare); //设置预览框形状
    cropLayout.setLayerView(layerView); //设置裁剪集成视图
    cropLayout.setRestrict(true); //设置边界限制，如果设置了该参数，预览框则不会超出图片
  }

  private void onSaveClicked() {
    Bitmap crop = cropLayout.crop();
    Intent intent =  new Intent();
    BitmapUtils.saveBitmaps(getApplication(),crop,new File(saveUri.getPath()));
    intent.setData(saveUri);
    //Bundle extras = new Bundle();
    //extras.putParcelable("data", crop);
    //intent.putExtras(extras);
    setResult(RESULT_OK,intent);
    finish();
  }
}


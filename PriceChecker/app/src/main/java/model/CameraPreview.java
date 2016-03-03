package model;


import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private MultiFormatReader mMultiFormatReader;
    private int mWidth, mHeight;
    private int mLeft, mTop, mAreaWidth, mAreaHeight;
    private AlertDialog mDialog;
    private static CameraManager cameraManager;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //scan
        mMultiFormatReader = new MultiFormatReader();

        //BARCODE WIDTH
        mWidth = 640;
        mHeight = 480;

        mDialog =  new AlertDialog.Builder(context).create();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            System.out.println("error començant el camera preview");
            Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the MyCamera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            //Scan
            mCamera.setPreviewCallback(mPreviewCallback);
            //
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            System.out.println("error comen�ant el camera preview");
        }
    }

    //Scan
    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera myCamera) {
            // TODO Auto-generated method stub
            if (mDialog.isShowing())
                return;
            LuminanceSource source = new PlanarYUVLuminanceSource(data, mWidth, mHeight, mLeft, mTop, mAreaWidth, mAreaHeight, true); //no horizontal -> true

            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                Result result = mMultiFormatReader.decode(bitmap, null);
                if (result != null) {
                    mDialog.setTitle("Result");
                    mDialog.setMessage(result.getText());
                    mDialog.show();
                }
            } catch (NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    public void setArea(int left, int top, int areaWidth, int width) {
        double ratio = (double) width / mWidth; //(double) TO SAVE DECIMALS. IMPORTANT!
        //double ratio = (double) areaWidth / mWidth; //(double) TO SAVE DECIMALS. IMPORTANT!
        mLeft = (int) (left / (ratio + 1));
        mTop = (int) (top / (ratio + 1));
        mAreaWidth = mWidth - mLeft * 2;
        //barcode height is different than width
        mAreaHeight = mHeight - mTop * 2;
    }

}
package utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.Display;
import android.widget.FrameLayout;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import model.CameraPreview;
import model.HoverView;
import uab.tfg.pricechecker.R;

/**
 * Created by david.cara on 3/2/16.
 */
public class MyCamera {

    private android.hardware.Camera mCamera;
    private CameraPreview mPreview;

    public Camera GetCamera()
    {
        return this.mCamera;
    }

    public void flashON(){
        android.hardware.Camera.Parameters param = mCamera.getParameters();
        //flash enc�s tant a la preview com a la captura (te�rica)
        param.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(param);
    }

    public void flashOFF(){
        android.hardware.Camera.Parameters param = mCamera.getParameters();
        param.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(param);
    }

    public boolean isFlashON(){
        android.hardware.Camera.Parameters param = mCamera.getParameters();
        if(param.getFlashMode().equals("torch")){ //si est� enc�s
            return true;
        }else{
            return false;
        }
    }

    public void setCameraParameters(){
        android.hardware.Camera.Parameters param = mCamera.getParameters();
        //Canvi dels par�metres
        //API
        //14	ICE_CREAM_SANDWICH	Android 4.0 Ice Cream Sandwich
        param.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE); //auto focus
        //--//
        mCamera.setParameters(param);
        mCamera.setDisplayOrientation(90); //c�mera vertical
    }

    public void initializeCamera(Activity activity, HoverView mHoverView, Display display){
        // Create an instance of MyCamera
        //vigilar que no sigui null!!!!!!!!!!!!!!!!
        //SEMPRE CAL COMPROVAR O PETA
        boolean check = checkCameraHardware(activity);
        if(check){
            mCamera = getCameraInstance(); //m�tode creat
            //setCameraDisplayOrientation(this, 1,  mCamera);
        }
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(activity, mCamera);
        mHoverView.update(display.getWidth(), display.getHeight());
        mPreview.setArea(mHoverView.getHoverLeft(), mHoverView.getHoverTop(), mHoverView.getHoverAreaWidth(), display.getWidth());
        //asociem el frame layout al preview de la c�mera
        FrameLayout preview = (FrameLayout) activity.findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    public void releaseCamera(){
        if (mCamera != null) {
            mCamera.release(); //alliberem els recursos
            mCamera = null;
            mPreview.getHolder().removeCallback(mPreview);
        }
    }

    private static android.hardware.Camera getCameraInstance(){ //MyCamera: android hardware
        android.hardware.Camera c = null;
        try {
            c = android.hardware.Camera.open(); // obtenim una inst�ncia de la c�mera
        }
        catch (Exception e){
            System.out.println("CMERA NO DISPONIBLE");
        }
        return c;
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            //PER MOSTRAR-HO PER PANTALLA!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //Toast.makeText(this, "Phone has camera", Toast.LENGTH_LONG).show();
            return true;
        } else {
            // no camera on this device
            //Toast.makeText(this, "Phone has no camera", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}

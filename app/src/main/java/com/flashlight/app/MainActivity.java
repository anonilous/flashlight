package com.flashlight.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements View.OnClickListener {

    private ToggleButton toggleButton;

    private Camera camera;
    private boolean hasCamera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Parameters params;
//    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // flash switch button
        toggleButton = (ToggleButton) this.findViewById(R.id.toggleButton1);
        toggleButton.setOnClickListener(this);

        //Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        if (!hasCameraHardware(this)) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
//            return;
        } else {
            Toast.makeText(getApplicationContext(), "Your device support flash light!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }


    /**
     * Check if this device has a camera and support flashlight or not
     */
    private boolean hasCameraHardware(Context context) {
        hasCamera = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (hasCamera) {
            // this device has a camera
            if (hasFlash) {
                return true;
            } else {
                return false;
            }
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
	 * Get the camera
	 */
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Failed to Open. Error: ", e.getMessage());
            }
        }
    }

//    /** A safe way to get an instance of the Camera object. */
//    public static Camera getCameraInstance(){
//        Camera camera = null;
//        try {
//            camera = Camera.open(); // attempt to get a Camera instance
////            params = camera.getParameters();
//        }
//        catch (Exception e){
//            // Camera is not available (in use or does not exist)
//        }
//        return camera; // returns null if camera is unavailable
//    }

    /**
     * Switch button click event to toggle flash on/off
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (isFlashOn) {
            // turn off flash
            turnOffFlash();
        } else {
            // turn on flash
            turnOnFlash();
        }

    }

    /**
     * Turning on flash
     */
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            // playSound();

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
            Toast.makeText(getApplicationContext(), "Turned on flash", Toast.LENGTH_SHORT).show();
            // changing button/switch image
            // toggleButtonImage();
        }
    }

        /**
         * Turning off flash
         */
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            // playSound();

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
            Toast.makeText(getApplicationContext(), "Turned off flash", Toast.LENGTH_SHORT).show();
            // changing button/switch image
            // toggleButtonImage();
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        // on pause turn off the flash
        turnOffFlash();
    }
    
    @Override
    protected void onRestart() {
       super.onRestart();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // on resume turn on the flash
        if(hasFlash)
        turnOnFlash();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        // on starting the app get the camera params
        getCamera();
//        getCameraInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        
        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }


}
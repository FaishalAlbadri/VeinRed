package com.tugasakhir.veinred.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.flir.thermalsdk.image.fusion.FusionMode;
import com.flir.thermalsdk.live.Identity;
import com.flir.thermalsdk.live.connectivity.ConnectionStatusListener;
import com.google.android.material.snackbar.Snackbar;
import com.tugasakhir.veinred.databinding.ActivityCameraBinding;
import com.tugasakhir.veinred.util.BitmapFrameBuffer;
import com.tugasakhir.veinred.util.CameraHandler;
import com.tugasakhir.veinred.util.FrameDataHolder;
import com.tugasakhir.veinred.util.ImageWriter;
import com.tugasakhir.veinred.util.UsbPermissionHandler;

import static com.tugasakhir.veinred.base.VeinredApplication.cameraHandler;
import static com.tugasakhir.veinred.base.VeinredApplication.connectedCameraIdentity;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    private ActivityCameraBinding binding;
    public UsbPermissionHandler usbPermissionHandler = new UsbPermissionHandler();
    public LinkedBlockingQueue<BitmapFrameBuffer> framesBuffer = new LinkedBlockingQueue<>(21);
    public static FusionMode curr_fusion_mode = FusionMode.THERMAL_ONLY;
    ScaleGestureDetector mScaleGestureDetector;
    private ImageWriter imageWriter = null;
    public static double left = 0;
    public static double top = 0;
    public static double width = 200;
    public static double height = 200;
    int touchx = -1;
    int touchy = -1;

    int modeCamera = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnCapture.setOnClickListener(v -> onCaptureImage());
        binding.btnCalibrate.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CalibrateActivity.class));
        });

        OpenCVLoader.initDebug();
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        connectCamera(cameraHandler.getFlirOne());

        binding.btnSwitchCamera.setOnClickListener(v -> {
            if (modeCamera == 0) {
                modeCamera = 1;
            } else {
                modeCamera = 0;
            }
        });

        binding.btnFilter.setOnClickListener(v -> {
            if (modeCamera == 0) {
                modeCamera = 1;
            }
            switchFilter();

        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (binding.imgCamera != null && CameraHandler.thermal_width != -1 && CameraHandler.thermal_height != -1) {
                double pos_w = width * scaleGestureDetector.getScaleFactor();
                double pos_h = height * scaleGestureDetector.getScaleFactor();

                touchx = (int) (scaleGestureDetector.getFocusX());
                touchy = (int) (scaleGestureDetector.getFocusY());

                if (pos_w > 0 && pos_h > 0 && left + pos_w < CameraHandler.thermal_width && top + pos_h < CameraHandler.thermal_height) {
                    width = pos_w;
                    height = pos_h;
                }
            }
            return true;
        }
    }

    public void switchFilter() {
        switch (curr_fusion_mode) {
            case THERMAL_ONLY:
                curr_fusion_mode = FusionMode.BLENDING;
                break;
            case BLENDING:
                curr_fusion_mode = FusionMode.MSX;
                break;
            case MSX:
                curr_fusion_mode = FusionMode.THERMAL_FUSION;
                break;
            case THERMAL_FUSION:
                curr_fusion_mode = FusionMode.PICTURE_IN_PICTURE;
                break;
            case PICTURE_IN_PICTURE:
                curr_fusion_mode = FusionMode.COLOR_NIGHT_VISION;
                break;
            case COLOR_NIGHT_VISION:
                curr_fusion_mode = FusionMode.THERMAL_ONLY;
                break;
        }
    }

    private void onCaptureImage() {
        imageWriter = new ImageWriter(this);
    }

    private void disconnectCamera() {
        connectedCameraIdentity = null;
        Log.d(TAG, "disconnect: Called with: connectedCameraIdentity = [" + connectedCameraIdentity + "]");
        new Thread(() -> {
            cameraHandler.disconnectCamera();
        }).start();
    }


    private void connectCamera(Identity identity) {
        if (connectedCameraIdentity != null) {
            disconnectCamera();
        }

        if (identity == null) {
            Log.e(TAG, "connectCamera: No camera available");
            Snackbar.make(binding.parentlayout, "connectCamera: No camera available", Snackbar.LENGTH_LONG).show();
            return;
        }

        connectedCameraIdentity = identity;

        if (UsbPermissionHandler.isFlirOne(identity)) {
            usbPermissionHandler.requestFlirOnePermisson(identity, this, permissionListener);
        } else {
            connectDevice(identity);
        }
    }

    private void connectDevice(Identity identity) {
        new Thread(() -> {
            try {
                cameraHandler.connectCamera(identity, connectionStatusListener);
                runOnUiThread(() -> {
                    cameraHandler.startStream(streamDataListener);
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Could not connect: " + e);
                    disconnectCamera();
                    connectCamera(cameraHandler.getFlirOne());
                });
            }
        }).start();
    }

    public ConnectionStatusListener connectionStatusListener = errorCode -> {
        Log.d(TAG, "onDisconnected: errorCode:" + errorCode);
    };

    public final CameraHandler.StreamDataListener streamDataListener = new CameraHandler.StreamDataListener() {
        @Override
        public void images(BitmapFrameBuffer dataHolder) {
            runOnUiThread(() -> {
                if (modeCamera == 0) {
                    binding.imgCamera.setImageBitmap(dataHolder.dcBitmap);
                } else {
                    binding.imgCamera.setImageBitmap(dataHolder.msxBitmap);
                }
            });
        }

        @Override
        public void images(Bitmap msxBitmap, Bitmap dcBitmap) {
            try {
                framesBuffer.put(new BitmapFrameBuffer(msxBitmap, dcBitmap));
            } catch (InterruptedException e) {
                //if interrupted while waiting for adding a new item in the queue
                Log.e(TAG, "images(), unable to add incoming images to frames buffer, exception:" + e);
            }

            runOnUiThread(() -> {
                Log.d(TAG, "framebuffer size:" + framesBuffer.size());
                BitmapFrameBuffer poll = framesBuffer.poll();
                if (poll != null) {
                    if (modeCamera == 0) {
                        binding.imgCamera.setImageBitmap(poll.dcBitmap);
                    } else {
                        binding.imgCamera.setImageBitmap(poll.msxBitmap);
                    }
                }
            });
        }

        @Override
        public void images(FrameDataHolder dataHolder) {
            try {
                if (imageWriter != null) {
                    imageWriter.saveImages(dataHolder);
                    imageWriter = null;
                    Animation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(300);
                    animation.setInterpolator(new LinearInterpolator());
                    binding.imgCamera.startAnimation(animation);
                    Toast.makeText(getApplicationContext(), "Saved Image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public UsbPermissionHandler.UsbPermissionListener permissionListener = new UsbPermissionHandler.UsbPermissionListener() {
        @Override
        public void permissionGranted(@NonNull Identity identity) {
            connectDevice(identity);
        }

        @Override
        public void permissionDenied(@NonNull Identity identity) {
            Snackbar.make(binding.parentlayout, "Permission was denied for identity", Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void error(ErrorType errorType, Identity identity) {
            Snackbar.make(binding.parentlayout, "Error when asking for permission for FLIR ONE, error:" + errorType + " identity:" + identity, Snackbar.LENGTH_LONG).show();
        }
    };

    @Override
    public void onBackPressed() {
        disconnectCamera();
        finish();
        super.onBackPressed();
    }
}
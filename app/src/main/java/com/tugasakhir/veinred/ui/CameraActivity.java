package com.tugasakhir.veinred.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;

import com.flir.thermalsdk.androidsdk.live.connectivity.UsbPermissionHandler;
import com.flir.thermalsdk.image.fusion.FusionMode;
import com.flir.thermalsdk.live.Identity;
import com.flir.thermalsdk.live.connectivity.ConnectionStatusListener;
import com.google.android.material.snackbar.Snackbar;
import com.tugasakhir.veinred.databinding.ActivityCameraBinding;
import com.tugasakhir.veinred.util.BitmapFrameBuffer;
import com.tugasakhir.veinred.util.CameraHandler;
import com.tugasakhir.veinred.util.FrameDataHolder;
import com.tugasakhir.veinred.util.ImageWriter;
import static com.tugasakhir.veinred.base.VeinredApplication.cameraHandler;
import static com.tugasakhir.veinred.base.VeinredApplication.connectedCameraIdentity;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    public static final String CONNECTED = "CONNECTED";
    public static final String CONNECTING = "CONNECTING";
    public static final String DISCONNECTED = "DISCONNECTED";
    public static final String DISCONNECTING = "DISCONNECTING";
    private ActivityCameraBinding binding;
    public UsbPermissionHandler usbPermissionHandler = new UsbPermissionHandler();
    public LinkedBlockingQueue<BitmapFrameBuffer> framesBuffer = new LinkedBlockingQueue<>(21);
    public static FusionMode curr_fusion_mode = FusionMode.THERMAL_ONLY;
    ScaleGestureDetector mScaleGestureDetector;
    private ImageWriter imageWriter = null;

    @SuppressLint("StaticFieldLeak")
    private static CameraActivity instance;

    public static double left = 0;
    public static double top = 0;
    public static double width = 200;
    public static double height = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void disconnectCamera() {
        updateConnectionText(connectedCameraIdentity, DISCONNECTING);
        connectedCameraIdentity = null;
        Log.d(TAG, "disconnect: Called with: connectedCameraIdentity = [" + connectedCameraIdentity + "]");
        new Thread(() -> {
            cameraHandler.disconnectCamera();
            runOnUiThread(() -> updateConnectionText(null, DISCONNECTED));
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

        updateConnectionText(identity, CONNECTING);
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
                    updateConnectionText(identity, CONNECTED);
                    cameraHandler.startStream(streamDataListener);
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Could not connect: " + e);
                    updateConnectionText(identity, DISCONNECTED);
                });
            }
        }).start();
    }

    private void updateConnectionText(Identity identity, String status) {
        String deviceId = identity != null ? " " + identity.deviceId : "";
    }

    public ConnectionStatusListener connectionStatusListener = errorCode -> {
        Log.d(TAG, "onDisconnected: errorCode:" + errorCode);
        runOnUiThread(() -> updateConnectionText(connectedCameraIdentity, DISCONNECTED));
    };

    public final CameraHandler.StreamDataListener streamDataListener = new CameraHandler.StreamDataListener() {
        @Override
        public void images(BitmapFrameBuffer dataHolder) {

        }

        @Override
        public void images(Bitmap msxBitmap, Bitmap dcBitmap) {

        }

        @Override
        public void images(FrameDataHolder dataHolder) {

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
}
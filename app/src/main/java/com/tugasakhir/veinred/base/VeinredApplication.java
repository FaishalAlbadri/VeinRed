package com.tugasakhir.veinred.base;

import android.app.Application;

import com.flir.thermalsdk.live.Identity;
import com.tugasakhir.veinred.util.CameraHandler;

public class VeinredApplication extends Application {

    public static CameraHandler cameraHandler;

    public static Identity connectedCameraIdentity;
}

package com.tugasakhir.veinred.util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.flir.thermalsdk.live.CommunicationInterface;
import com.flir.thermalsdk.live.Identity;
import com.flir.thermalsdk.log.ThermalLog;
import org.jetbrains.annotations.Nullable;

public class UsbPermissionHandler {
    private static final String TAG = "UsbPermissionHandler";
    private UsbPermissionReceiver usbPermissionReceiver = new UsbPermissionReceiver();
    @VisibleForTesting
    UsbPermissionListener usbPermissionListener = null;
    @VisibleForTesting
    static TestLogger testLogger;

    public UsbPermissionHandler() {
    }

    public void requestFlirOnePermisson(@NonNull Identity identity, @NonNull Context uiContext, @NonNull UsbPermissionListener usbPermissionListener) {
        this.usbPermissionListener = usbPermissionListener;
        if (!ConnectionUtil.isFlirOne(identity)) {
            Logw("UsbPermissionHandler", "requestFlirOnePermission for a non FLIR ONE identity:" + identity);
            usbPermissionListener.error(UsbPermissionHandler.UsbPermissionListener.ErrorType.UNKNOWN_IDENTITY, identity);
        } else if (hasFlirOnePermission(identity, uiContext)) {
            usbPermissionListener.permissionGranted(identity);
        } else {
            PendingIntent permissionIntent = PendingIntent.getBroadcast(uiContext, 0, new Intent("com.flir.thermalsdk.androidsdk.live.connectivity.UsbPermissionReceiver.USB_PERMISSION"), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            IntentFilter filter = new IntentFilter("com.flir.thermalsdk.androidsdk.live.connectivity.UsbPermissionReceiver.USB_PERMISSION");
            uiContext.registerReceiver(this.usbPermissionReceiver, filter);
            UsbManager manager = ConnectionUtil.getUsbManager(uiContext);
            UsbDevice device = ConnectionUtil.findDevice(identity, uiContext);
            if (device == null) {
                usbPermissionListener.error(UsbPermissionHandler.UsbPermissionListener.ErrorType.INVALID_IDENTITY, identity);
            } else {
                manager.requestPermission(device, permissionIntent);
            }
        }
    }

    public static boolean hasFlirOnePermission(@Nullable Identity identity, @Nullable Context applicationContext) {
        if (identity != null && applicationContext != null) {
            if (isEmulator(identity)) {
                return true;
            } else {
                return !isFlirOne(identity) ? false : ConnectionUtil.hasFlirOnePermission(identity, applicationContext);
            }
        } else {
            return false;
        }
    }

    private static boolean isEmulator(@Nullable Identity identity) {
        return identity.communicationInterface.equals(CommunicationInterface.EMULATOR);
    }

    public static boolean isFlirOne(@Nullable Identity identity) {
        return identity == null ? false : ConnectionUtil.isFlirOne(identity);
    }

    @VisibleForTesting
    UsbPermissionReceiver getUsbPermissionReceiver() {
        return new UsbPermissionReceiver();
    }

    private void permissionDenied(UsbDevice device) {
        if (this.usbPermissionListener != null) {
            this.usbPermissionListener.permissionDenied(ConnectionUtil.createFlirOneIdentity(device));
        } else {
            Logw("UsbPermissionHandler", "UsbPermissionListener was null when reporting permission denied for device:" + device);
        }

    }

    private void permissionGranted(UsbDevice device) {
        if (this.usbPermissionListener != null) {
            this.usbPermissionListener.permissionGranted(ConnectionUtil.createFlirOneIdentity(device));
        } else {
            Logw("UsbPermissionHandler", "UsbPermissionListener was null when reporting permission granted for usb device:" + device);
        }

    }

    private static void Logw(String tag, String message) {
        if (testLogger != null) {
            testLogger.logw(tag, message);
        } else {
            ThermalLog.w(tag, message);
        }

    }

    private static void Logd(String tag, String message) {
        if (testLogger != null) {
            testLogger.logd(tag, message);
        } else {
            ThermalLog.d(tag, message);
        }

    }

    private static void Loge(String tag, String message) {
        if (testLogger != null) {
            testLogger.loge(tag, message);
        } else {
            ThermalLog.e(tag, message);
        }

    }

    @VisibleForTesting
    final class UsbPermissionReceiver extends BroadcastReceiver {
        private static final String TAG = "UsbPermissionReceiver";
        @VisibleForTesting
        static final String ACTION_USB_PERMISSION = "com.flir.thermalsdk.androidsdk.live.connectivity.UsbPermissionReceiver.USB_PERMISSION";

        UsbPermissionReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.flir.thermalsdk.androidsdk.live.connectivity.UsbPermissionReceiver.USB_PERMISSION".equals(action)) {
                synchronized(this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra("device");
                    if (device == null) {
                        UsbPermissionHandler.Logw("UsbPermissionReceiver", "onReceive, device was null in usb permission receiver");
                        return;
                    }

                    if (intent.getBooleanExtra("permission", false)) {
                        UsbPermissionHandler.this.permissionGranted(device);
                    } else {
                        UsbPermissionHandler.this.permissionDenied(device);
                    }
                }

                context.unregisterReceiver(UsbPermissionHandler.this.usbPermissionReceiver);
            }

        }
    }

    public interface UsbPermissionListener {
        void permissionGranted(@NonNull Identity var1);

        void permissionDenied(@NonNull Identity var1);

        void error(ErrorType var1, Identity var2);

        public static enum ErrorType {
            UNKNOWN_IDENTITY,
            INVALID_IDENTITY;

            private ErrorType() {
            }
        }
    }

    @VisibleForTesting
    interface TestLogger {
        void logd(String var1, String var2);

        void logw(String var1, String var2);

        void loge(String var1, String var2);
    }
}

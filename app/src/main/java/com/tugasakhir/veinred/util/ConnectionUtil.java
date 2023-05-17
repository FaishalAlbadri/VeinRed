package com.tugasakhir.veinred.util;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import androidx.annotation.NonNull;
import com.flir.thermalsdk.live.CameraType;
import com.flir.thermalsdk.live.CommunicationInterface;
import com.flir.thermalsdk.live.Identity;
import com.flir.thermalsdk.live.IpSettings;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

class ConnectionUtil {
    ConnectionUtil() {
    }

    public static Identity createFlirOneIdentity(@NonNull UsbDevice usbDevice) {
        Identity newDevice = new Identity(CommunicationInterface.USB, CameraType.FLIR_ONE, String.valueOf(usbDevice.getProductName()), (IpSettings)null);
        return newDevice;
    }

    public static UsbDevice findDevice(Identity identity, Context applicationContext) {
        if (identity != null && applicationContext != null) {
            UsbManager manager = getUsbManager(applicationContext);
            Map<String, UsbDevice> devices = manager.getDeviceList();
            Iterator var4 = devices.values().iterator();

            UsbDevice dev;
            do {
                if (!var4.hasNext()) {
                    return null;
                }

                dev = (UsbDevice)var4.next();
            } while(!identity.deviceId.equals(dev.getProductName()));

            return dev;
        } else {
            return null;
        }
    }

    public static final UsbManager getUsbManager(Context context) {
        return (UsbManager)context.getSystemService(Context.USB_SERVICE);
    }

    public static boolean isFlirOne(Identity identity) {
        return identity.cameraType.equals(CameraType.FLIR_ONE);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for(int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 15];
        }

        return new String(hexChars);
    }

    public static byte[] hex2Byte(String input) {
        byte[] val = new byte[input.length() / 2];

        for(int i = 0; i < val.length; ++i) {
            int index = i * 2;
            int j = Integer.parseInt(input.substring(index, index + 2), 16);
            val[i] = (byte)j;
        }

        return val;
    }

    public static boolean hasFlirOnePermission(@NotNull Identity identity, @NotNull Context context) {
        UsbDevice device = findDevice(identity, context);
        return device == null ? false : getUsbManager(context).hasPermission(device);
    }

    private @NotNull String bytesToString(byte[] data) {
        return new String(data, Charset.defaultCharset());
    }
}


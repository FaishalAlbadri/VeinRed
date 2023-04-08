package com.tugasakhir.veinred.util;

import com.flir.thermalsdk.image.DistanceUnit;
import com.flir.thermalsdk.image.ThermalImage;
import com.flir.thermalsdk.image.palettes.Palette;
import com.flir.thermalsdk.image.palettes.PaletteManager;

import java.util.Arrays;
import java.util.List;

public class CalibrationHandler {
    public static double atmosphericTemperature = -1;
    public static double distance = -1;
    public static double emissivity = -1;
    public static double externalOpticsTemperature = -1;
    public static double externalOpticsTransmission = -1;
    public static double reflectiveTemperature = -1;
    public static double relativeHumidity = -1;
    public static double transmission = -1;
    public static DistanceUnit distanceUnit = DistanceUnit.METER;
    public static Palette palette = null;
    private static final String[] palettes = {"iron", "Arctic", "blackhot", "bw", "Coldest", "ColorWheel_Redhot", "ColorWheel6", "ColorWheel12", "DoubleRainbow2", "lava", "rainbow", "rainHC", "whitehot", "Hottest"};
    public static boolean calibrationButtonHidden = true;

    public CalibrationHandler() {
    }

    static void calibrate(ThermalImage img) {
        setDefaults(img);
        img.getImageParameters().setAtmosphericTemperature(atmosphericTemperature);
        img.getImageParameters().setDistance(distance);
        img.getImageParameters().setEmissivity(emissivity);
        img.getImageParameters().setExternalOpticsTemperature(externalOpticsTemperature);
        img.getImageParameters().setExternalOpticsTransmission(externalOpticsTransmission);
        img.getImageParameters().setReflectedTemperature(reflectiveTemperature);
        img.getImageParameters().setRelativeHumidity(relativeHumidity);
        img.getImageParameters().setTransmission(transmission);
        img.setDistanceUnit(distanceUnit);

        List<String> arr = Arrays.asList(palettes);
        if (arr.contains(palette.name)) {
            img.setPalette(PaletteManager.getDefaultPalettes().get(arr.indexOf(palette.name)));
        }
    }

    /**
     * Set the Atmospheric temperature for the given ThermalImage
     *
     * @param temp Temperature in
     */
    public static void setAtmosphericTemperature(double temp) {
        atmosphericTemperature = cToK(temp);
    }

    public static void setDistance(double dist) {
        distance = dist;
    }

    public static void setEmissivity(double emiss) {
        emissivity = emiss;
    }

    public static void setExternalOpticsTemperature(double temp) {
        externalOpticsTemperature = cToK(temp);
    }

    public static void setExternalOpticsTransmission(double transmission) {
        externalOpticsTransmission = transmission;
    }

    public static void setReflectiveTemperature(double temp) {
        reflectiveTemperature = cToK(temp);
    }

    public static void setRelativeHumidity(double humidity) {
        relativeHumidity = humidity;
    }

    public static void setTransmission(double trans) {
        transmission = trans;
    }

    public static void setDistanceUnit(DistanceUnit unit) {
        distanceUnit = unit;
    }

    private static void setDefaults(ThermalImage img) {
        if (palette == null) {
            palette = PaletteManager.getDefaultPalettes().get(0);
            img.setPalette(palette);
        }
        if (atmosphericTemperature == -1) {
            atmosphericTemperature = img.getImageParameters().getAtmosphericTemperature();
        }
        if (distance == -1) {
            distance = img.getImageParameters().getDistance();
        }
        if (emissivity == -1) {
            emissivity = img.getImageParameters().getEmissivity();
        }
        if (externalOpticsTemperature == -1) {
            externalOpticsTemperature = img.getImageParameters().getExternalOpticsTemperature();
        }
        if (externalOpticsTransmission == -1) {
            externalOpticsTransmission = img.getImageParameters().getExternalOpticsTransmission();
        }
        if (reflectiveTemperature == -1) {
            reflectiveTemperature = img.getImageParameters().getReflectedTemperature();
        }
        if (relativeHumidity == -1) {
            relativeHumidity = img.getImageParameters().getRelativeHumidity();
        }
        if (transmission == -1) {
            transmission = img.getImageParameters().getTransmission();
        }
        if (calibrationButtonHidden) {
//            FlirCameraActivity.getInstance().toggleCalibrationButton();
        }
    }

    public static void setPalette(String name) {
        List<String> arr = Arrays.asList(palettes);
        if (arr.contains(name)) {
            palette = PaletteManager.getDefaultPalettes().get(arr.indexOf(name));
        }
    }

    private static double kToF(double k) {
        return ((k - 273.15) * 9 / 5) + 32;
    }

    private static double fToK(double f) {
        return ((f - 32) * 5 / 9) + 273.15;
    }

    public static double kToC(double k) {
        return k - 273.15;
    }

    private static double cToK(double c) {
        return c + 273.15;
    }

    private static double cToF(double c) {
        return (c * 9 / 5) + 32;
    }

    private static double fToC(double f) {
        return (f - 32) * 5 / 9;
    }
}

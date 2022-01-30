package com.apuliacreativehub.eculturetool.ui.component;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.CaptureActivity;

public class QRCodeHelper extends CaptureActivity {

    public static Bitmap generateQRCode(String text) throws WriterException {
        // Initialize multi format writer
        MultiFormatWriter writer = new MultiFormatWriter();

        // Initialize bit matrix
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 350, 350);

        // Initialize barcode encoder
        BarcodeEncoder encoder = new BarcodeEncoder();

        // Initialize bitmap
        Bitmap bitmap = encoder.createBitmap(matrix);

        // Return QRCode
        return bitmap;
    }

}
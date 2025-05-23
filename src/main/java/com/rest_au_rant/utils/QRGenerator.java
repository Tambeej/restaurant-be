package com.rest_au_rant.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QRGenerator {
    public static String generateQRCode(Long tableId) throws WriterException, IOException {
        String qrContent = String.valueOf(tableId);
        String filePath = "qrcodes/table_" + tableId + ".png";
        File qrCodeFile = new File("qrcodes/table_" + tableId + ".png");
        qrCodeFile.getParentFile().mkdirs();

        int width = 300;
        int height = 300;

        BitMatrix matrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, width, height);
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);

        return filePath;
    }
}

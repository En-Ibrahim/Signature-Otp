package com.example.demo.imageProcc;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageProcessingService {
    static {
        try {
            // نجيب الـ path الفعلي داخل resources
            String libName = "opencv_java490.dll";

            // نحاول نقرأ الملف من resources
            URL resource = ImageProcessingService.class.getClassLoader().getResource("lib/" + libName);
            if (resource == null) {
                throw new IllegalStateException("OpenCV library not found in resources/lib/");
            }

            // نحول الـ URL إلى File (عشان نقدر نحمله)
            File libFile = new File(resource.toURI());
            System.load(libFile.getAbsolutePath());

            log.info("✅ OpenCV library loaded successfully from: " + libFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("❌ Failed to load OpenCV library dynamically: " + e.getMessage(), e);
        }
    }

    public double compareSignatures(MultipartFile image1, MultipartFile image2) {
        try {
            // 1️⃣ تحويل MultipartFile إلى Mat
            Mat img1 = Imgcodecs.imdecode(new MatOfByte(image1.getBytes()), Imgcodecs.IMREAD_GRAYSCALE);
            Mat img2 = Imgcodecs.imdecode(new MatOfByte(image2.getBytes()), Imgcodecs.IMREAD_GRAYSCALE);

            if (img1.empty() || img2.empty()) {
                throw new RuntimeException("One of the images is empty or unreadable");
            }

            // 2️⃣ إنشاء ORB detector
            ORB orb = ORB.create();

            // 3️⃣ استخراج keypoints و descriptors
            MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
            MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
            Mat descriptors1 = new Mat();
            Mat descriptors2 = new Mat();

            orb.detectAndCompute(img1, new Mat(), keypoints1, descriptors1);
            orb.detectAndCompute(img2, new Mat(), keypoints2, descriptors2);

            if (descriptors1.empty() || descriptors2.empty()) {
                throw new RuntimeException("Could not extract features from one of the images");
            }

            // 4️⃣ استخدام Brute Force Matcher
            BFMatcher matcher = BFMatcher.create(Core.NORM_HAMMING, true);
            MatOfDMatch matches = new MatOfDMatch();
            matcher.match(descriptors1, descriptors2, matches);

            // 5️⃣ تحليل نتائج المطابقة
            List<DMatch> matchList = matches.toList();

            if (matchList.isEmpty()) return 0.0;

            double maxDist = 0;
            double minDist = Double.MAX_VALUE;

            for (DMatch match : matchList) {
                double dist = match.distance;
                if (dist < minDist) minDist = dist;
                if (dist > maxDist) maxDist = dist;
            }

            // 6️⃣ اعتبر الماتش "قوي" لو المسافة أقل من (2 × الحد الأدنى)
            List<DMatch> goodMatches = new ArrayList<>();
            for (DMatch match : matchList) {
                if (match.distance <= 2 * minDist) {
                    goodMatches.add(match);
                }
            }

            // 7️⃣ حساب نسبة التشابه
            double similarity = (double) goodMatches.size() / matchList.size();

            return Math.round(similarity * 100.0) / 100.0; // النتيجة بدقتين عشريتين

        } catch (IOException e) {
            throw new RuntimeException("Error reading image files: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error comparing signatures: " + e.getMessage(), e);
        }
    }
}




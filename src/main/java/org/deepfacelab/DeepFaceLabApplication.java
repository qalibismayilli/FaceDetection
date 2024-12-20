package org.deepfacelab;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;

public class DeepFaceLabApplication {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        VideoCapture videoCapture = new VideoCapture(0);
        if (!videoCapture.isOpened()) {
            System.out.println("Error: Could not open video.");
            return;
        }

        CascadeClassifier faceDetector = new CascadeClassifier("C:/MyOpenCV/opencv/sources/data/haarcascades/haarcascade_frontalface_default.xml");

        Mat frame = new Mat();
        MatOfRect faces = new MatOfRect();

        long lastSavedTime = System.currentTimeMillis(); // Track last save time

        while (true) {
            videoCapture.read(frame); // Capture a frame
            if (frame.empty()) {
                System.out.println("Error: No frame captured.");
                break;
            }

            // Detect faces
            faceDetector.detectMultiScale(frame, faces);

            // Check if 5 seconds have passed since the last save
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSavedTime >= 5000) { // 5000 ms = 5 seconds
                lastSavedTime = currentTime; // Update last saved time

                // Save each detected face
                int faceIndex = 0; // Counter for faces in the same frame
                for (Rect rect : faces.toArray()) {
                    Mat face = new Mat(frame, rect); // Crop the face
                    String filename = String.format("C:/Users/Galib/Pictures/detected_face_%d_%d.jpg", currentTime, faceIndex);
                    Imgcodecs.imwrite(filename, face); // Save detected face
                    System.out.println("Saved face to: " + filename);
                    faceIndex++;
                }
            }

            // Draw rectangles around faces and display the frame
            for (Rect rect : faces.toArray()) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2); // Draw rectangle
            }
            HighGui.imshow("Camera Feed", frame);

            // Break the loop on 'Esc' key press
            if (HighGui.waitKey(30) == 27) {
                break;
            }
        }

        videoCapture.release();
        HighGui.destroyAllWindows();
    }
}
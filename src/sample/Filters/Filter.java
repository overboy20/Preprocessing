package sample.Filters;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


//містить статичні методи, кожен з яких реалізує певний фільтр
public class Filter {

    public static Mat bilateralFilter(Mat image, int d, double sigmaColor, double sigmaSpace){
        Mat result = new Mat();
        Imgproc.bilateralFilter(image, result, d, sigmaColor, sigmaSpace);
        return result;
    }

    public static Mat adaptiveBilateralFilter(Mat image, int kSize, int sigmaSpace){
        if (kSize%2 != 1) {
            kSize -= 1;
        }

        Mat result = new Mat();
        Size s = new Size(kSize, kSize);
        Imgproc.adaptiveBilateralFilter(image, result, s, sigmaSpace);
        return result;
    }

    public static Mat blur(Mat image, int kSize){
        Mat result = new Mat();
        Size s = new Size(kSize, kSize);
        Imgproc.blur(image, result, s);
        return result;
    }

    public static Mat gaussianBlur(Mat image, int kSize, Double sigmaX){
        if (kSize%2 != 1) {
            kSize -= 1;
        }
        Mat result = new Mat();
        Size s = new Size(kSize, kSize);
        Imgproc.GaussianBlur(image, result, s, sigmaX);
        return result;
    }

    public static Mat medianBlur(Mat image, int kSize){
        if (kSize%2 == 0) kSize -= 1;
        Mat result = new Mat();
        Imgproc.medianBlur(image, result, kSize);
        return result;
    }

    public static Mat laplacian(Mat image, int kSize, double scale, double delta){
        Mat result = new Mat();
        Mat grey = new Mat();
        int ddepth = CvType.CV_16S;

        if (kSize%2 == 0) kSize -= 1;

        Imgproc.GaussianBlur(image, result, new Size(11,11), 20, 0);
        Imgproc.cvtColor(result, grey, Imgproc.COLOR_BGR2GRAY);

        Imgproc.Laplacian(grey, result, ddepth, kSize, scale, delta);
        double minVal, maxVal;
        Core.MinMaxLocResult minMaxLocResult=Core.minMaxLoc(grey);
        minVal = minMaxLocResult.minVal;
        maxVal = minMaxLocResult.maxVal;

        Mat draw = new Mat();
        result.convertTo(draw, CvType.CV_8U, 255.0 / (maxVal - minVal), -minVal * 255.0 / (maxVal - minVal));
        return result;
    }

    public static Mat sobel(Mat image, int dx, int dy, int ksize){
        Mat result = new Mat(), grad = new Mat();
        //int ddepth = -1, dx = 2, dy = 2, scale = 1, delta = 3, ksize = 5;
        int ddepth = -2;
        double scale = 1;
        double delta = 0;

        if (ksize%2 == 0) ksize -= 1;
        if (ksize > 31) ksize = 31;

        Imgproc.GaussianBlur(image, result, new Size(11,11), 0, 0);
        Imgproc.cvtColor(result, result, Imgproc.COLOR_BGR2GRAY);

        /// Generate grad_x and grad_y
        Mat grad_x = new Mat(), grad_y = new Mat();
        Mat abs_grad_x = new Mat(), abs_grad_y = new Mat();

        /// Gradient X
        Imgproc.Sobel(result, grad_x, ddepth, 1, 0, ksize, scale, delta);
        Core.convertScaleAbs(grad_x, abs_grad_x);

        /// Gradient Y
        Imgproc.Sobel(result, grad_y, ddepth, 0, 1, ksize, scale, delta);
        Core.convertScaleAbs(grad_y, abs_grad_y);

        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);

        return grad;
    }
}
package sample.tools;



public class InputHandler {

    private final String e01 = "Input values must be integer!";
    private final String e02 = "d must be > 0!";
    private final String e03 = "K size must be positive and odd!";
    private final String e04 = "Input value must be > 0!";
    private final String e05 = "Delta must be positive!";
    private final String e06 = "Values must be positive!";
    private final String e07 = "Value must be double and bigger than 1!";


    public Error checkBilateral(String d, String sigmaColor, String sigmaSpace) {
        try {
            int dd = Integer.parseInt(d);
            int sc = Integer.parseInt(sigmaColor);
            int ss = Integer.parseInt(sigmaSpace);
            if (dd < 0) return new Error(e02);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Error(e01);
        }
        return new Error();
    }

    public Error checkAdaptive(String kSize, String sigmaSpace) {
        try {
            int ks = Integer.parseInt(kSize);
            int ss = Integer.parseInt(sigmaSpace);
            if (ks%2 != 1) {
                return new Error(e03);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
            return new Error(e01);
        }

        return new Error();
    }

    public Error checkBlur(String kSize){
        try {
            int ks = Integer.parseInt(kSize);
            if (ks <= 0) {
                return new Error(e04);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Error(e01);
        }
        return new Error();
    }

    public Error checkGaussian(String kSize, String sigmaX) {
        try {
            int ks = Integer.parseInt(kSize);
            int sx = Integer.parseInt(sigmaX);
            if (ks%2 != 1 || ks <= 0) return new Error(e03);

        } catch (Exception e){
            System.out.println(e.getMessage());
            return new Error(e01);
        }
        return new Error();
    }

    public Error checkMedian(String kSize) {
        try {
            int ks = Integer.parseInt(kSize);
            if (ks%2 != 1 || ks <= 0) return new Error(e03);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Error(e01);
        }
        return new Error();
    }

    public Error checkLaplacian(String kSize, String scale, String delta){
        try {
            int ks = Integer.parseInt(kSize);
            int s = Integer.parseInt(scale);
            int d = Integer.parseInt(delta);
            if (ks <= 0 || ks%2 != 1) return new Error(e03);
            if (d <= 0) return new Error(e05);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Error(e01);
        }
        return new Error();
    }

    public Error checkSobel(String dx, String dy, String ksize) {
        try {
            int x = Integer.parseInt(dx);
            int y = Integer.parseInt(dy);
            int ks = Integer.parseInt(ksize);
            if (ks <= 0 || ks%2 != 1 || x <= 0 || y <= 0) return new Error(e06);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Error(e01);
        }
        return new Error();
    }

    public Error checkErodeDilate(String ksize) {
        try {
            int k = Integer.parseInt(ksize);
            if (k <= 0) return new Error(e06);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Error(e01);
        }
        return new Error();
    }

    public Error checkContrast(String value){
        try {
            double v = Double.parseDouble(value);
            if (v < 1) return new Error(e07);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Error(e07);
        }
        return new Error();
    }
}

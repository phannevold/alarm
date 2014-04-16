import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.opencv.core.Core.absdiff;
import static org.opencv.imgproc.Imgproc.threshold;

/**
 * @author Petter Hannevold
 */
public class ImageAnalyzer {

	private Mat mat1, mat2, resultMat1, resultMat2, image;

	private MatOfByte matOfByte = new MatOfByte();
	ByteArrayInputStream in;

	private boolean originalImage;
	private double threshold = 150.0;
	private double maxval = 10000.0;

	private boolean motionDetected = false;

	public ImageAnalyzer(Mat mat, Mat mat2) {
		this.mat1 = mat;
		this.mat2 = mat2;
		resultMat1 = new Mat();
		resultMat2 = new Mat();
	}

	public BufferedImage generateImage() {

		int[] motionSquare = detectMotion();

		image = originalImage ? mat2 : resultMat2;

		Highgui.imencode(".jpg", image, matOfByte);
		byte[] imgArray = matOfByte.toArray();
		in = new ByteArrayInputStream(imgArray);

		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(in);
		} catch (IOException ignored) { }

		Graphics2D g = (Graphics2D) (bufferedImage != null ? bufferedImage.getGraphics() : null);

		if (g != null) {
			g.drawRect(motionSquare[1], motionSquare[0], motionSquare[3], motionSquare[2]);
		}

		Main.setDetection(hasValue(motionSquare));

		return bufferedImage;
	}

	private int[] detectMotion() {
		absdiff(mat1, mat2, resultMat1);
		threshold(resultMat1, resultMat2, threshold, maxval, Imgproc.THRESH_BINARY);

		return findBoundaries();
	}

	private int[] findBoundaries() {
		int[] motionBoundaries = new int[4];

		outer1:
		for (int i = 0; i < resultMat2.height(); i++) {
			for (int j = 0; j < resultMat2.width(); j++) {
				if (hasValue(resultMat2.get(i,j))) {
					motionBoundaries[0] = i;
					motionBoundaries[1] = j;
					break outer1;
				}
			}
		}

		outer2:
		for (int i = resultMat2.height(); i > 0; i--) {
			for (int j = resultMat2.width(); j > 0; j--) {
				if (hasValue(resultMat2.get(i,j))) {
					motionBoundaries[2] = i;
					motionBoundaries[3] = j;
					break outer2;
				}
			}
		}
		return motionBoundaries;
	}

	private boolean hasValue(double[] doubles) {
		if (doubles != null) {
			for (double d : doubles) {
				if (d != 0.0) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasValue(int[] ints) {
		if (ints != null) {
			for (int i : ints) {
				if (i != 0) {
					return true;
				}
			}
		}
		return false;
	}

	public void flipOrigImage() {
		originalImage = !originalImage;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getMaxval() {
		return maxval;
	}

	public void setMaxval(double maxval) {
		this.maxval = maxval;
	}

	public boolean isMotionDetected() {
		return motionDetected;
	}
}

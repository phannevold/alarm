import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import java.io.File;
import java.io.IOException;

/**
 * @author Petter Hannevold
 */

public class Main {

	private static final String LIB_NAME = "opencv_java248.dll";

	private static Frame frame;


	public static void main(String... args) throws IOException {

		int width=0, height=0;
		String path;


		String osName = System.getProperty("os.name");
		System.out.println(osName);

		if (osName.toLowerCase().contains("windows")) {

			if (System.getProperty("os.arch").equals("amd64")) {
				path = "windows" + File.separator + "x64" + File.separator + LIB_NAME;
			} else {
				path = "windows" + File.separator + "x86" + File.separator + LIB_NAME;
			}
		} else if (osName.toLowerCase().contains("linux")) {
			path = "linux" + File.separator + "libopencv_java300.so";
		} else {
			throw new Error("Unsupported OS");
		}

		System.load(new File(path).getAbsolutePath());
		VideoCapture capture = new VideoCapture(0);
		if (capture.open(0)) {
			width = (int) capture.get(Highgui.CV_CAP_PROP_FRAME_WIDTH);
			height = (int) capture.get(Highgui.CV_CAP_PROP_FRAME_HEIGHT);
		}



		Mat image = new Mat();
		Mat image2 = new Mat();
		ImageAnalyzer analyzer = new ImageAnalyzer(image, image2);

		frame = new Frame(width, height, analyzer);

		while(true) {
			capture.read(image);
			capture.read(image2);
			frame.draw(analyzer.generateImage());
		}
	}

	static public void setDetection(boolean detected) {
		frame.setDetected(detected);
	}
}

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * @author Petter Hannevold
 */
public class Frame extends JFrame {

	JPanel videoPanel, controllerPanel;

	JButton flipOrig, detectedButton, thresholdButton, maxvalueButton;
	JLabel thresholdLabel, maxvalueLabel, emptylabel;
	JTextField thresholdField, maxvalueField;

	ButtonListener listener;

	ImageAnalyzer analyzer;

	boolean detected;

	public Frame(int w, int h, ImageAnalyzer analyzer) {
		this.analyzer = analyzer;
		setVisible(true);
		setSize(w, h*2);
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		videoPanel = new JPanel();
		getContentPane().setLayout(new GridLayout());
		((GridLayout)getContentPane().getLayout()).setRows(2);
		getContentPane().add(videoPanel);

		populateControllerPanel();
		getContentPane().add(controllerPanel);
	}

	private void populateControllerPanel() {


		controllerPanel = new JPanel();
		listener = new ButtonListener();

		flipOrig = new JButton("flip image");
		flipOrig.setActionCommand("flip");
		flipOrig.addActionListener(listener);
		controllerPanel.add(flipOrig);

		detectedButton = new JButton();
		controllerPanel.add(detectedButton);

		controllerPanel.add(new JLabel("                                                                                                                                                    "));

		thresholdLabel = new JLabel("Threshold");
		controllerPanel.add(thresholdLabel);

		thresholdField = new JTextField(10);
		thresholdField.setText(Double.toString(analyzer.getThreshold()));
		thresholdField.addActionListener(listener);
		thresholdField.setActionCommand("setThreshold");
		controllerPanel.add(thresholdField);

		thresholdButton = new JButton("set threshold");
		thresholdButton.addActionListener(listener);
		thresholdButton.setActionCommand("setThreshold");
		controllerPanel.add(thresholdButton);

		controllerPanel.add(new JLabel("                                                                                                                                                    "));

		maxvalueLabel = new JLabel("Max value");
		controllerPanel.add(maxvalueLabel);

		maxvalueField = new JTextField(10);
		maxvalueField.setText(Double.toString(analyzer.getMaxval()));
		maxvalueField.addActionListener(listener);
		maxvalueField.setActionCommand("setMaxValue");
		controllerPanel.add(maxvalueField);

		maxvalueButton = new JButton("set max value");
		maxvalueButton.addActionListener(listener);
		maxvalueButton.setActionCommand("setMaxValue");
		controllerPanel.add(maxvalueButton);
	}

	public void draw(BufferedImage image) {
		videoPanel.getGraphics().drawImage(image, 0, 0, videoPanel);
	}

	public void setDetected(boolean detected) {
		if (detected != this.detected) {
			this.detected = detected;
			if (detected) {
				detectedButton.setBackground(Color.RED);
				detectedButton.setText("INTRUDER ALERT");
			} else {
				detectedButton.setBackground(Color.GREEN);
				detectedButton.setText("DO NOT PANIC!");
			}
		}
	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {
				case "flip":
					analyzer.flipOrigImage();
					break;
				case "setThreshold":
					analyzer.setThreshold(Double.parseDouble(thresholdField.getText()));
					break;
				case "setMaxValue":
					analyzer.setMaxval(Double.parseDouble(maxvalueField.getText()));
			}
		}
	}
}

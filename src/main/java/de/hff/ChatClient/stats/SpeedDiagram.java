package de.hff.ChatClient.stats;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

public class SpeedDiagram extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2309638455426352581L;
	private int secondCounter = 1;

	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	public SpeedDiagram() {
		super("Geschwindigkeit");
		JFreeChart lineChart = ChartFactory.createLineChart("�bertragunsrate", "Zeit", "Geschwindigkeit MB/s", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new Dimension(650, 400));
		setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setVisible(true);

	}

	public void addvalue(double MbPerSecound, int secound) {

		if (dataset.getColumnCount() >= 20) {
			dataset.removeColumn("" + (secondCounter++));
		}

		dataset.addValue(MbPerSecound, "MB/s", "" + secound);
	}

}
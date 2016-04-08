package view;

import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/************************************************************************************************
 * Developer: Anton 																			*
 * 																								*
 * Date: 05 April 2016  																		*
 * 																								*
 * Description: This class is to construct pie chart to display data virtually					*
 ************************************************************************************************/
public class PieChart {
	
	private Map<String, Object> result;
	private String title;
	
	public PieChart(Map<String,Object> result, String title)
	{
		this.title = title;
		this.result = result;
	}
	
	/**
	 * To open another frame window to dispay the chart created
	 */
	public void View()
	{
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		for(String key : result.keySet())
		{
			pieDataset.setValue(key, Double.parseDouble(result.get(key).toString()));
		}
		
		JFreeChart chart = ChartFactory.createPieChart(title, pieDataset, true, true, true);
		
		ChartFrame frame = new ChartFrame("Pie Chart", chart);
		
		frame.setVisible(true);
		frame.setSize(450,500);
	}
}

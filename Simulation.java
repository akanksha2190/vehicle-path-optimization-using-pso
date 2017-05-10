package algoProject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DirectedMultigraph;

public class Simulation  extends JApplet {

	private static final long serialVersionUID = 1L;
	private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
	private static final Dimension DEFAULT_SIZE = new Dimension(800, 800);
	private JGraphModelAdapter<String, DefaultEdge> jgAdapter;
	

	@Override
	public void init() {
	}
	
	private void adjustDisplaySettings(JGraph jg) {
		jg.setPreferredSize(DEFAULT_SIZE);

		Color c = DEFAULT_BG_COLOR;
		String colorStr = null;

		try {
			colorStr = getParameter("bgcolor");
		} catch (Exception e) {
		}

		if (colorStr != null) {
			c = Color.decode(colorStr);
		}

		jg.setBackground(c);
	}

	@SuppressWarnings("unchecked")
	private void positionVertexAt(Object vertex, int x, int y) {
		DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
		AttributeMap attr = cell.getAttributes();
		Rectangle2D bounds = GraphConstants.getBounds(attr);

		Rectangle2D newBounds = new Rectangle2D.Double(x, y, bounds.getWidth(), bounds.getHeight());

		GraphConstants.setBounds(attr, newBounds);

		AttributeMap cellAttr = new AttributeMap();
		cellAttr.put(cell, attr);
		jgAdapter.edit(cellAttr, null, null, null);
	}

	private static class ListenableDirectedMultigraph<V, E> extends DefaultListenableGraph<V, E> {
		private static final long serialVersionUID = 1L;

		ListenableDirectedMultigraph(Class<E> edgeClass) {
			super(new DirectedMultigraph<>(edgeClass));
		}
	}

	public static void main(String[] args) throws InterruptedException {
		PSOSolution psoSimulizer = new PSOSolution();
		psoSimulizer.initializePath();
		psoSimulizer.executePSO();
		psoSimulizer.printBestSolution();
		
		
		int count = PSOSolution.STATION_COUNT;
		for(List<Integer> list : psoSimulizer.pathList){
			count--;
			Simulation v1 = new Simulation();
			ListenableGraph<String, DefaultEdge> g1 = new ListenableDirectedMultigraph<>(DefaultEdge.class);
			v1.jgAdapter = new JGraphModelAdapter<>(g1);
			JGraph jgraph1 = new JGraph(v1.jgAdapter);
			jgraph1.removeAll();
			v1.adjustDisplaySettings(jgraph1);
			v1.getContentPane().add(jgraph1);
			v1.resize(DEFAULT_SIZE);
			for (int i = 0; i<PSOSolution.STATION_COUNT; i++) {
				g1.addVertex(Integer.toString(i));
				v1.positionVertexAt(Integer.toString(i), PSOSolution.X[i] * 15, PSOSolution.Y[i] * 15);
			}
			for (int j = 0; j < PSOSolution.STATION_COUNT; j++) {
				if (j + 1 < PSOSolution.STATION_COUNT)
					g1.addEdge(Integer.toString(list.get(j)), Integer.toString(list.get(j+1)));
			}
			JFrame frame = new JFrame();
			frame.getContentPane().add(v1);
			frame.setTitle("Routing of packets");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
			Thread.sleep(100);
			if(count == 0)
				break;
		}
		
		Simulation v = new Simulation();
		ListenableGraph<String, DefaultEdge> g = new ListenableDirectedMultigraph<>(DefaultEdge.class);
		v.jgAdapter = new JGraphModelAdapter<>(g);
		JGraph jgraph = new JGraph(v.jgAdapter);
		jgraph.setDragEnabled(false);
		v.adjustDisplaySettings(jgraph);
		v.getContentPane().add(jgraph);
		v.resize(DEFAULT_SIZE);
		for (int i = 0; i<PSOSolution.STATION_COUNT; i++) {
			g.addVertex(Integer.toString(i));
			v.positionVertexAt(Integer.toString(i), PSOSolution.X[i] * 15, PSOSolution.Y[i] * 15);
		}
		for (int j = 0; j < PSOSolution.STATION_COUNT; j++) {
			if (j + 1 < PSOSolution.STATION_COUNT)
				g.addEdge(Integer.toString(psoSimulizer.finalList.get(j)), Integer.toString(psoSimulizer.finalList.get(j+1)));
		}
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(v);
		frame.setTitle("Vehicle Travel Path");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		Thread.sleep(50);
		
	}

}


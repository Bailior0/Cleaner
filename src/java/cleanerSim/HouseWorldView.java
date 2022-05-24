package cleanerSim;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.infra.local.BaseLocalMAS;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class HouseWorldView extends JFrame {

    private static final long serialVersionUID = 1L;

    protected int cellSizeW = 0;
    protected int cellSizeH = 0;

    protected PaintPanel panel;
    protected HouseWorldModel model;

    protected Font defaultFont = new Font("Arial", Font.BOLD, 10);
    private JTable table;
    private AbstractTableModel tableModel;

    public HouseWorldView(HouseWorldModel model) {
        this(model, "Cleaner World", 1200);
        defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
        setVisible(true);
        repaint();
    }

    public HouseWorldView(HouseWorldModel model, String title, int windowSize) {
        super(title);

        this.model = model;
        initComponents(windowSize);
        model.setView(this);
    }

    /** sets the size of the frame and adds the components */
    public void initComponents(int width) {
        setSize(width, width);
        getContentPane().setLayout(new BorderLayout());

        JPanel center = new JPanel();
        //FlowLayout experimentLayout = new FlowLayout();
        //center.setLayout(experimentLayout);
        center.setLayout(new BoxLayout(center, BoxLayout.LINE_AXIS));

        panel = new PaintPanel(model);
        panel.setPreferredSize(new Dimension(800,800));
        center.add(panel);

        JPanel container = new JPanel();

        tableModel = new AbstractTableModel() {

            @Override
            public int getRowCount() {
                return model.getAgs().size();
            }

            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                var a = model.getAgs().get(rowIndex);

                switch (columnIndex) {
                    case 0:
                        return a.getKey();
                    case 1:
                        return "X: " + a.getValue().loc.x + " Y: "+ a.getValue().loc.y;
                    case 2:
                        return BaseLocalMAS.getRunner().getAg(a.getKey()).getTS().getSettings().getUserParameter("size");
                    case 3:
                        return a.getValue().currentAmount;
                }

                return null;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        table = new JTable(tableModel);


        container.setLayout(new BorderLayout());
        container.add(table.getTableHeader(), BorderLayout.PAGE_START);
        container.add(table, BorderLayout.CENTER);

        center.add(container);

        getContentPane().add(BorderLayout.CENTER, center);
    }

    @Override
    public void repaint() {

        tableModel.fireTableDataChanged();
        panel.repaint();
    }

    /** updates all the frame */
    public void update() {
        repaint();
    }


    public Object[][] updateAgentDisplay(){

        Object[][] data = new Object[model.agLocation.entrySet().size()][];

        int i = 0;
        for (var ag : model.agLocation.entrySet()) {
            data[i] = new Object[]{
                ag.getKey()
            };
            i++;
        }
        return data;
    }

}

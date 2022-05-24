package cleanerSim;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HouseWorldView extends JFrame {

    private static final long serialVersionUID = 1L;

    protected int cellSizeW = 0;
    protected int cellSizeH = 0;

    protected GridCanvas drawArea;
    protected HouseWorldModel model;

    protected Font defaultFont = new Font("Arial", Font.BOLD, 10);

    public HouseWorldView(HouseWorldModel model) {
        this(model, "Cleaner World", 600);
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
        drawArea = new GridCanvas();
        getContentPane().add(BorderLayout.CENTER, drawArea);
    }

    @Override
    public void repaint() {
        cellSizeW = drawArea.getWidth() / model.getWidth();
        cellSizeH = drawArea.getHeight() / model.getHeight();
        super.repaint();
        drawArea.repaint();
    }

    /** updates all the frame */
    public void update() {
        repaint();
    }

    /** updates only one position of the grid */
    public void update(int x, int y) {
        Graphics g = drawArea.getGraphics();
        if (g == null) return;
        drawEmpty(g, x, y);
        draw(g, x, y);
    }

    public void drawObstacle(Graphics g, int x, int y) {
        g.setColor(Color.darkGray);
        g.fillRect(x * cellSizeW + 1, y * cellSizeH+1, cellSizeW-1, cellSizeH-1);
        g.setColor(Color.black);
        g.drawRect(x * cellSizeW + 2, y * cellSizeH+2, cellSizeW-4, cellSizeH-4);
    }

    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        g.setColor(c);
        g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
        if (id >= 0) {
            g.setColor(Color.black);
            drawString(g, x, y, defaultFont, String.valueOf(id+1));
        }
    }

    public void drawString(Graphics g, int x, int y, Font f, String s) {
        g.setFont(f);
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth( s );
        int height = metrics.getHeight();
        g.drawString( s, x*cellSizeW+(cellSizeW/2-width/2), y*cellSizeH+(cellSizeH/2+height/2));
    }

    public void drawEmpty(Graphics g, int x, int y) {
        g.setColor(Color.white);
        g.fillRect(x * cellSizeW + 1, y * cellSizeH+1, cellSizeW-1, cellSizeH-1);
        g.setColor(Color.lightGray);
        g.drawRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
    }


    private static int limit = (int)Math.pow(2,14);

    private void draw(Graphics g, int x, int y) {

        var location = new Location(x,y);
        var ag =
                this.model.agLocation
                        .entrySet().stream()
                        .filter(p->p.getValue().loc.equals(location))
                        .findFirst();
        if(ag.isPresent()){
            var agName = ag.get().getKey();
            if(agName.contains("sensor")) {
                drawSensor(g, x, y);
            }
            else if(agName.contains("cleaner")) {
                drawCleaner(g,x,y,agName.replace("cleaner","c"));
            }
        }

        if(model.dirtMap[x][y] > 0){
            g.setColor(Color.black);
            drawString(g,x,y,defaultFont,String.valueOf(model.dirtMap[x][y]));
        }
    }


    private void drawSensor(Graphics g, int x, int y){
        drawAgent(g,x,y,new Color(86, 127, 137),0);
    }

    private void drawCleaner(Graphics g, int x, int y, String name){
        String label = name ;
        drawAgent(g, x, y, new Color(27, 155, 48), -1);
        g.setColor(Color.black);
        drawString(g, x, y, defaultFont, label);
    }

    public Canvas getCanvas() {
        return drawArea;
    }

    public HouseWorldModel getModel() {
        return model;
    }

    class GridCanvas extends Canvas {

        private static final long serialVersionUID = 1L;

        BufferedImage backImage = null;

        int height;
        int width;

        public void paint(Graphics g) {
            cellSizeW = drawArea.getWidth() / model.getWidth();
            cellSizeH = drawArea.getHeight() / model.getHeight();
            int mwidth = model.getWidth();
            int mheight = model.getHeight();

            if (backImage == null || height != drawArea.getHeight() || width != drawArea.getWidth()) {
                backImage = (BufferedImage) this.createImage(drawArea.getWidth(), drawArea.getHeight());
                var g2 = backImage.createGraphics();


                g2.setColor(Color.lightGray);
                for (int l = 1; l <= mheight; l++) {
                    g2.drawLine(0, l * cellSizeH, mwidth * cellSizeW, l * cellSizeH);
                }
                for (int c = 1; c <= mwidth; c++) {
                    g2.drawLine(c * cellSizeW, 0, c * cellSizeW, mheight * cellSizeH);
                }

                g2.dispose();
            }
            width = drawArea.getWidth();
            height = drawArea.getHeight();

            g.drawImage(backImage, 0, 0, null);

            for (int x = 0; x < mwidth; x++) {
                for (int y = 0; y < mheight; y++) {
                    draw(g,x,y);
                }
            }

            g.dispose();
        }
    }
}

package cleanerSim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Display panel of the map
 */
public class PaintPanel extends JPanel{  
    // Array for simple number indexing the colors
    private Color indexedColors[] = new Color[9];
    
    // Calculated cell size based on the map size and the actual panel size
    private int cellSize = 0;
    
    // Indicate if painting is already in progress
    public static boolean paintingInProgress = false;
    private HouseWorldModel model;
    private BufferedImage trash_icon;

    /**
     * Constructor of the pain panel
     */
    public PaintPanel(HouseWorldModel model) {
        // Init the parent class, set double buffering and background color
        super();
        this.model = model;
        setDoubleBuffered(true);
        setBackground(new Color(219, 219, 219));
        
        // Set fixed indexed colors
        indexedColors[0] = new Color(255, 244, 229);
        indexedColors[1] = new Color(184,184,184);
        indexedColors[2] = Color.blue;
        indexedColors[3] = Color.yellow;
        indexedColors[4] = Color.cyan;
        indexedColors[5] = Color.magenta;
        indexedColors[6] = Color.orange;
        indexedColors[7] = Color.gray;
        indexedColors[8] = Color.white;

        try {
            File fileFromResources = Resources.getInstance().getFileFromResources("delete-circle.png");
            if(fileFromResources != null)
                trash_icon = ImageIO.read(fileFromResources);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Override the paintComponent method to display map content
     * 
     * @param g     The Graphics object to draw to
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Get the map from the RescueFramework
        if (model == null) {
            return;
        }
        
        // Calculate cellSize
        int cellWidth = (int)Math.floor(getWidth()/(float)model.getWidth());
        int cellHeight = (int)Math.floor(getHeight()/(float)model.getHeight());

        int mwidth = model.getWidth();
        int mheight = model.getHeight();

        cellSize = Math.min(cellWidth, cellHeight);
        
        // Convert Graphics to Graphics2D
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.lightGray);
        for (int l = 1; l <= mheight; l++) {
            g2.drawLine(0, l * cellSize, cellSize*mwidth, l * cellSize);
        }
        for (int c = 1; c <= mwidth; c++) {
            g2.drawLine(c * cellSize, 0, c * cellSize, cellSize * mheight);
        }

        g2.setColor(Color.darkGray);

        // Paint cells one by one
        for (int x = 0; x<model.getWidth(); x++) {
            for (int y = 0; y<model.getHeight(); y++) {

                Rectangle rect = new Rectangle(x * cellSize + 1, y * cellSize + 1, cellSize - 1, cellSize - 1);

                if(model.dirtMap[x][y] > 0) {
                    g2.setColor(calculateGray(1-(float) model.dirtMap[x][y] / 5));
                    g2.fillRect(x * cellSize + 1, y * cellSize + 1, cellSize - 1, cellSize - 1);
                }
                g2.setColor(Color.BLACK);

                drawCenteredString(g2,
                        String.valueOf(model.dirtMap[x][y]),
                        rect,
                        g2.getFont()
                );
            }
        }

        // Draw agents
        for (var ag : model.agLocation.entrySet()) {

            var agName = ag.getKey();
            var loc = ag.getValue().loc;
            Rectangle rect = new Rectangle(loc.x * cellSize + 1, loc.y * cellSize + 1, cellSize - 1, cellSize - 1);

            if(agName.contains("dumpster")) {
                drawDumpster(g,rect,loc.x, loc.y);
            }
            else if(agName.contains("cleaner")) {
                drawCleaner(g,rect,loc.x, loc.y,agName.replace("cleaner","c"));
            }
            else if(agName.contains("sensor")) {
                drawSensor(g,rect, loc.x, loc.y);
            }

        }

        paintingInProgress = false;
    }

    private void drawDumpster(Graphics g, Rectangle rect, int x, int y) {
        if(trash_icon == null){
            g.setColor(Color.red);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
            g.setColor(Color.BLACK);
            return;
        }

        g.drawImage(trash_icon,x,y,cellSize,cellSize,null);
    }

    private void drawSensor(Graphics g,Rectangle rect, int x, int y){
        drawAgent(g,rect,x,y,new Color(86, 127, 137),0);
    }

    private void drawCleaner(Graphics g,Rectangle rect, int x, int y, String name){
        String label = name ;
        drawAgent(g,rect, x, y, new Color(27, 155, 48), -1);
        g.setColor(Color.black);
        drawCenteredString(g,label, rect, g.getFont());
    }

    public void drawAgent(Graphics g,Rectangle rect, int x, int y, Color c, int id) {
        g.setColor(c);
        g.fillOval(x * cellSize + 5, y * cellSize + 5, cellSize - 10, cellSize - 10);
        if (id >= 0) {
            g.setColor(Color.black);
            drawCenteredString(g,String.valueOf(id+1), rect, g.getFont());
        }
    }

    /**
     * Display a string centered
     * @param g     The Graphics2D to draw to
     * @param text  The string to display
     * @param rect  The rect to center the text in
     * @param font  The font to be used
     */
    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }
    
    /**
     * Handle mouse clicks on the panel
     * 
     * @param x     X coordinate of the click
     * @param y     Y coordinate of the click
     */
    /*
    public void mouseClicked(int x, int y) {
        RescueFramework.log("Click at "+x+"x"+y);
        if (cellSize == 0) return;
        
        // Determine cell
        int cellX = x/cellSize;
        int cellY = y/cellSize;
        
        // Move the first robot if there is one
        Robot r = RescueFramework.map.getRobots().get(0);
        if (r != null) {
            RescueFramework.map.moveRobot(r, RescueFramework.map.getCell(cellX, cellY));
        }
    }
    */

    /**
     * Calculate color based on percent value
     * 
     * @param value     Percent value
     * @return          Color belonging to the value
     */
    public Color calculateColor(float value) {
        double red = 0, green = 0, blue = 0;

        // First, green stays at 100%, red raises to 100%
        if (value<0.5) {        
            green = 1.0;
            red = 2 * value;
        }
        
        // Then red stays at 100%, green decays
        if (0.5<=value) {       
            red = 1.0f;
            green = 1.0 - 2 * (value-0.5);
        }
       
        return new Color((int)(255*red), (int)(255*green), (int)(255*blue));
    }

    public Color calculateGray(float value) {
        Color base = new Color(252, 186, 3);
        double gray = value;

        return new Color((int)(base.getRed()*gray), (int)(base.getGreen()*gray), (int)(base.getBlue()*gray));
    }
}

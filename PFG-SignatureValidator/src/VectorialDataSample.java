
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VectorialDataSample {

    private ArrayList<Point> points;
    private int[] xs;
    private int[] ys;
    private int period;
    private String label = "";
    private int dim1 = 0;
    private int dim2 = 0;

    public VectorialDataSample(int period, String label) {
        this.period = period;
        this.points = new ArrayList<Point>();
        this.xs = new int[]{0};
        this.ys = new int[]{0};
        this.label = label;
    }

    void addPoint(Point p) {
        points.add(p);
    }

    public int size() {
        return points.size();
    }

    public int[] getXs() {
        if (this.xs.length == points.size()) {
            return xs;
        }
        refreshXYVectors();
        return xs;
    }

    public int[] getYs() {
        if (this.ys.length == points.size()) {
            return ys;
        }
        refreshXYVectors();
        return this.ys;
    }

    private void refreshXYVectors() {
        int[] xs = new int[points.size()];;
        int[] ys = new int[points.size()];
        for (int i = 0; i < xs.length; i++) {
            xs[i] = (int) points.get(i).getX();
            ys[i] = (int) points.get(i).getY();
        }
        this.ys = ys;
        this.xs = xs;
    }

    void saveFigure(String label) {
        this.setLabel(label);
        saveFigure();
    }

    void saveFigure(String label, int dim1, int dim2) {
        this.setLabel(label);
        this.setDimensions(dim1, dim2);
        saveFigure();

    }

    void saveFigure() {
        try {
            File dir = new File("./generatedfigures");
            dir.mkdir();
            File file = File.createTempFile("fig" + label + "-" + period + "ms" + "-" + dim1
                    + "x" + dim2 + "-",
                    ".vdsf", dir);
            PrintWriter writer = new PrintWriter(dir + "/" + file.getName(), "UTF-8");
            writer.println(period);
            writer.println(dim1 + " " + dim2);
            writer.println(label);
            for (int i = 0; i < xs.length; i++) {
                writer.println(xs[i] + " " + ys[i]);
            }
            writer.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(VectorialDataSample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(VectorialDataSample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VectorialDataSample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public String getLabel(){
        return this.label;
    }
    private void setLabel(String label) {
        this.label = label;
    }

    private void setDimensions(int dim1, int dim2) {
        this.dim1 = dim1;
        this.dim2 = dim2;
    }

    public static VectorialDataSample readVDS(File file) {
        VectorialDataSample vds = new VectorialDataSample(0, file.getName());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String period = br.readLine();
            String xydims = br.readLine();
            String className = br.readLine();
            vds = new VectorialDataSample(Integer.parseInt(period), className);
            for (String line; (line = br.readLine()) != null;) {
                String[] pS = br.readLine().split(" ");
                Point p = new Point(Integer.parseInt(pS[0]),
                        Integer.parseInt(pS[1]));
                vds.addPoint(p);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()+"   ERROR WHILE READING SAMPLE" + file.getName());
        }
        return vds;
    }

}

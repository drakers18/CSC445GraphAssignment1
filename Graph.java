import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class Point {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

public class Graph extends JFrame {

    public Graph(ArrayList<Point> points) {
        setTitle("Point Display");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Find the bounds of the points
                double minX = Double.MAX_VALUE;
                double minY = Double.MAX_VALUE;
                double maxX = Double.MIN_VALUE;
                double maxY = Double.MIN_VALUE;
                for (Point point : points) {
                    minX = Math.min(minX, point.x);
                    minY = Math.min(minY, point.y);
                    maxX = Math.max(maxX, point.x);
                    maxY = Math.max(maxY, point.y);
                }

                // Calculate scaling factors
                double scale = Math.min((getWidth() - 150) / (maxX - minX), (getHeight() - 300) / (maxY - minY));
                

                System.out.println("scale: "+ scale);
                

                // Draw x-axis
                g.drawLine(50, getHeight() / 2, getWidth() - 50, getHeight() / 2);

                // Draw y-axis
                g.drawLine(getWidth() / 2, getHeight() - 50, getWidth() / 2, 50);

                // Draw points and lines
                Point prevPoint = null;
                for (Point point : points) {
                    int scaledX = (int) (getWidth() / 2 + (point.x - minX) * scale);
                    int scaledY = (int) (getHeight() / 2 - (point.y - minY) * scale);
                    g.setColor(Color.RED);
                    g.fillOval(scaledX - 2, scaledY - 2, 5, 5);
                    if (prevPoint != null) {
                        g.drawLine((int) (getWidth() / 2 + (prevPoint.x - minX) * scale),
                                   (int) (getHeight() / 2 - (prevPoint.y - minY) * scale),
                                   scaledX,
                                   scaledY);
                    }
                    prevPoint = point;
                }
            }
        };

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArrayList<Point> points = new ArrayList<>();
            
            // Check if file path is provided as command-line argument
            if (args.length > 0) {
                try {
                    points = readPointsFromFile(args[0]);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found: " + e.getMessage());
                    return;
                }
            } else {
                // If no command-line argument, use file open dialog box
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        points = readPointsFromFile(selectedFile.getAbsolutePath());
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found: " + e.getMessage());
                        return;
                    }
                } else {
                    System.out.println("No file selected.");
                    return;
                }
            }
            
            Graph graph = new Graph(points);
            graph.setVisible(true);
        });
    }

    private static ArrayList<Point> readPointsFromFile(String filePath) throws FileNotFoundException {
        ArrayList<Point> points = new ArrayList<>();
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        int numPoints = scanner.nextInt();
        scanner.nextLine(); // Consume newline after integer
        for (int i = 0; i < numPoints; i++) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s*,\\s*");
            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            points.add(new Point(x, y));
        }

        scanner.close();
        return points;
    }
}

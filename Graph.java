import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graph extends JFrame {

    public Graph() {
        setTitle("Point Display");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw x-axis
                g.drawLine(50, getHeight() / 2, getWidth() - 50, getHeight() / 2);
                g.drawString("X", getWidth() - 20, getHeight() / 2 - 10);

                // Draw y-axis
                g.drawLine(getWidth() / 2, getHeight() - 50, getWidth() / 2, 50);
                g.drawString("Y", getWidth() / 2 - 20, 20);

                try {
                    Scanner scanner = new Scanner(new File("Input.txt"));

                    while (scanner.hasNext()) {
                        String line = scanner.nextLine().trim();
                        if (!line.isEmpty()) {
                            String[] parts = line.split("\\s*,\\s*");
                            if (parts.length == 2) {
                                double x = Double.parseDouble(parts[0]);
                                double y = Double.parseDouble(parts[1]);

                                // Adjust coordinates to center the origin
                                int scaledX = (int) (getWidth() / 2 + x * (getWidth() - 100) / 2);
                                int scaledY = (int) (getHeight() / 2 - y * (getHeight() - 100) / 2);

                                g.setColor(Color.RED);
                                g.fillOval(scaledX - 2, scaledY - 2, 5, 5);
                            }
                        }
                    }
                    scanner.close();

                } catch (FileNotFoundException e) {
                    System.out.println("File not found: " + e.getMessage());
                }
            }
        };

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Graph graph = new Graph();
            graph.setVisible(true);
        });
    }
}

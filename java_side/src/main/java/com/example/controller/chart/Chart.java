package com.example.controller.chart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import com.example.API_clients.ApiClients;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class Chart extends JPanel {
    private JPanel header, content;
    private JLabel statsLabel;

    // Statistics values
    private int minPres = Integer.MAX_VALUE;
    private int maxPres = Integer.MIN_VALUE;
    private int totalPres = 0;
    private int count = 0;

    public Chart() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Set up header
        header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 8, 0, 8));

        JLabel headerLabel = new JLabel("Statistiques des Présentations");
        headerLabel.setBorder(new EmptyBorder(22, 12, 22, 22));
        headerLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        header.add(headerLabel, BorderLayout.NORTH);

        // Stats label on the east side
        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        eastPanel.setBackground(Color.WHITE);

        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        eastPanel.add(statsLabel);

        header.add(eastPanel, BorderLayout.EAST);

        // Create histogram panel for the content
        content = new HistogramPanel();
        content.setBorder(new EmptyBorder(12, 2, 0, 2));

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        // Load data
        loadData();
    }

    public void loadData() {
        try {
            // Reset values
            minPres = Integer.MAX_VALUE;
            maxPres = Integer.MIN_VALUE;
            totalPres = 0;
            count = 0;

            // Get enseignants data from API
            JSONArray enseignants = ApiClients.getAll("enseignants");
            count = enseignants.length();

            for (int i = 0; i < enseignants.length(); i++) {
                JSONObject prof = enseignants.getJSONObject(i);
                int presentation = prof.optInt("taux_horaire") * prof.optInt("nombre_heure");
                totalPres += presentation;

                // Update max and min values
                if (presentation > maxPres) {
                    maxPres = presentation;
                }
                if (presentation < minPres) {
                    minPres = presentation;
                }
            }

            // If no data was loaded, reset min/max to zero
            if (count == 0) {
                minPres = 0;
                maxPres = 0;
            }

            // Update the UI with the statistics
            updateStats();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateStats() {
        int average = count > 0 ? totalPres / count : 0;
        statsLabel.setText(String.format("Total: %d  |  Min: %d  |  Max: %d  |  Moyenne: %d",
                totalPres, count == 0 ? 0 : minPres, count == 0 ? 0 : maxPres, average));

        // Update chart
        ((HistogramPanel)content).updateValues(minPres, maxPres, totalPres, average);
        repaint();
    }

    // Inner class for the histogram chart
    class HistogramPanel extends JPanel {
        private int minValue = 0;
        private int maxValue = 0;
        private int totalValue = 0;
        private int averageValue = 0;

        // Colors for the bars
        private Color minColor = new Color(64, 224, 208);   // Turquoise
        private Color maxColor = new Color(255, 99, 71);    // Tomato
        private Color totalColor = new Color(65, 105, 225); // RoyalBlue
        private Color avgColor = new Color(255, 215, 0);    // Gold

        public HistogramPanel() {
            setBackground(Color.WHITE);
        }

        public void updateValues(int min, int max, int total, int avg) {
            this.minValue = min;
            this.maxValue = max;
            this.totalValue = total;
            this.averageValue = avg;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - 100;
            int height = getHeight() - 100;
            int bottom = getHeight() - 50;
            int left = 50;
            int barWidth = width / 4 - 20;

            // Find the maximum value for scaling (ensure it's at least 1 to avoid division by zero)
            int highestValue = Math.max(Math.max(minValue, maxValue),
                    Math.max(totalValue, averageValue));
            if (highestValue == 0) highestValue = 1;

            // Draw coordinate axes
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(left, bottom, left + width + 10, bottom); // X-axis
            g2.drawLine(left, bottom, left, bottom - height - 10); // Y-axis

            // Draw axis labels
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString("Valeur", 10, bottom - height/2);
            g2.drawString("Statistiques", left + width/2 - 30, bottom + 40);

            // Draw scale marks on Y-axis
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            int numMarks = 5;
            for (int i = 0; i <= numMarks; i++) {
                int y = bottom - (i * height / numMarks);
                int value = i * highestValue / numMarks;
                g2.drawLine(left - 5, y, left, y);
                g2.drawString(String.valueOf(value), left - 35, y + 4);
            }

            // Draw bars
            int spacing = 20;
            int startX = left + spacing;

            drawBar(g2, startX, bottom, barWidth, height, minValue, highestValue, minColor, "Min");
            drawBar(g2, startX + barWidth + spacing, bottom, barWidth, height, maxValue, highestValue, maxColor, "Max");
            drawBar(g2, startX + 2 * (barWidth + spacing), bottom, barWidth, height, averageValue, highestValue, avgColor, "Moyenne");
            drawBar(g2, startX + 3 * (barWidth + spacing), bottom, barWidth, height, totalValue, highestValue, totalColor, "Total");

            // Draw title
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Statistiques des Présentations", width/2 - 100, 30);
        }

        private void drawBar(Graphics2D g2, int x, int bottom, int width, int maxHeight,
                             int value, int highestValue, Color color, String label) {
            // Calculate bar height based on value relative to highest value
            int barHeight = value > 0 ? (int)(((double)value / highestValue) * maxHeight) : 0;

            // Draw bar
            g2.setColor(color);
            Rectangle2D.Double bar = new Rectangle2D.Double(x, bottom - barHeight, width, barHeight);
            g2.fill(bar);
            g2.setColor(color.darker());
            g2.draw(bar);

            // Draw label
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            g2.drawString(label, x + width/2 - textWidth/2, bottom + 20);

            // Draw value
            String valueText = String.valueOf(value);
            textWidth = fm.stringWidth(valueText);
            g2.setColor(Color.BLACK);
            g2.drawString(valueText, x + width/2 - textWidth/2, bottom - barHeight - 5);
        }
    }

    // Add a refresh button to allow manual data refresh
    public void addRefreshButton() {
        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.addActionListener(e -> loadData());

        JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        westPanel.setBackground(Color.WHITE);
        westPanel.add(refreshButton);

        header.add(westPanel, BorderLayout.WEST);
    }
}
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSearcherGUI extends JFrame {
    private JTextArea originalArea;
    private JTextArea filteredArea;
    private JTextField searchField;
    private JButton loadButton, searchButton, quitButton;
    private Path loadedFile;

    public FileSearcherGUI() {
        super("Lab_09_FileSearcher");

        originalArea = new JTextArea();
        filteredArea = new JTextArea();
        searchField = new JTextField(20);

        loadButton = new JButton("Load File");
        searchButton = new JButton("Search");
        quitButton = new JButton("Quit");

        originalArea.setEditable(false);
        filteredArea.setEditable(false);

        setLayout(new BorderLayout());

        // Top panel for input and buttons
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(loadButton);
        topPanel.add(searchButton);
        topPanel.add(quitButton);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for text areas
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(new JScrollPane(originalArea));
        centerPanel.add(new JScrollPane(filteredArea));
        add(centerPanel, BorderLayout.CENTER);

        // Button actions
        loadButton.addActionListener(e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));

        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            loadedFile = chooser.getSelectedFile().toPath();
            try (Stream<String> lines = Files.lines(loadedFile)) {
                List<String> allLines = lines.toList();
                originalArea.setText(String.join("\n", allLines));
                filteredArea.setText(""); // Clear previous search
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
            }
        }
    }

    private void searchFile() {
        if (loadedFile == null) {
            JOptionPane.showMessageDialog(this, "Load a file first!");
            return;
        }

        String keyword = searchField.getText().trim().toLowerCase();

        try (Stream<String> lines = Files.lines(loadedFile)) {
            List<String> filtered = lines
                    .filter(line -> line.toLowerCase().contains(keyword))
                    .collect(Collectors.toList());

            filteredArea.setText(String.join("\n", filtered));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error searching file: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileSearcherGUI::new);
    }
}

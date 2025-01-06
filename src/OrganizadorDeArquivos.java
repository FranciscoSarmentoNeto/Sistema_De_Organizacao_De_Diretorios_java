import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrganizadorDeArquivos extends JFrame {
    private JTextField directoryField, nameField, extensionField;
    private JTable fileTable;
    private DefaultTableModel tableModel;
    private List<File> foundFiles;

    public OrganizadorDeArquivos() {
        setTitle("Organizador de Arquivos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        Color backgroundColor = new Color(50, 50, 50);
        Color panelColor = new Color(70, 70, 70);
        Color textColor = new Color(220, 220, 220);
        Color buttonColor = new Color(100, 100, 100);
        Color buttonHoverColor = new Color(150, 150, 150);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        JPanel formPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        formPanel.setBackground(panelColor);

        directoryField = new JTextField();
        UtilitariosDeArquivos.styleTextField(directoryField, textColor, buttonColor);

        JButton browseButton = UtilitariosDeArquivos.createIconButton("Procurar", "Selecione o diretório", "📂",
                buttonColor, buttonHoverColor, textColor);
        browseButton.addActionListener(e -> chooseDirectory());

        nameField = new JTextField();
        UtilitariosDeArquivos.styleTextField(nameField, textColor, buttonColor);

        extensionField = new JTextField();
        UtilitariosDeArquivos.styleTextField(extensionField, textColor, buttonColor);

        JButton searchButton = UtilitariosDeArquivos.createIconButton("Buscar", "Buscar arquivos no diretório", "🔍",
                buttonColor, buttonHoverColor, textColor);
        searchButton.addActionListener(e -> searchFiles());

        JButton organizeButton = UtilitariosDeArquivos.createIconButton("Organizar", "Organize os arquivos encontrados",
                "🗂️", buttonColor, buttonHoverColor, textColor);
        organizeButton.addActionListener(e -> organizeFiles());

        formPanel.add(UtilitariosDeArquivos.createLabeledField("Diretório:", directoryField, browseButton, textColor,
                panelColor));
        formPanel.add(UtilitariosDeArquivos.createLabeledField("Nome do Arquivo (opcional):", nameField, null,
                textColor, panelColor));
        formPanel.add(UtilitariosDeArquivos.createLabeledField("Extensão do Arquivo (ex: .jpg):", extensionField, null,
                textColor, panelColor));
        formPanel.add(searchButton);
        formPanel.add(organizeButton);

        tableModel = new DefaultTableModel(
                new Object[] { "Nome", "Tamanho", "Data de Modificação", "Caminho Completo" }, 0);
        fileTable = new JTable(tableModel);
        fileTable.setBackground(new Color(30, 30, 30));
        fileTable.setForeground(textColor);
        fileTable.setGridColor(new Color(100, 100, 100));
        JScrollPane scrollPane = new JScrollPane(fileTable);

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void chooseDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            directoryField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void searchFiles() {
        String directoryPath = directoryField.getText();
        String nameFilter = nameField.getText();
        String extensionFilter = extensionField.getText();

        if (directoryPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um diretório.");
            return;
        }

        foundFiles = new ArrayList<>();
        tableModel.setRowCount(0);

        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            UtilitariosDeArquivos.searchDirectory(directory, nameFilter, extensionFilter, foundFiles);
            for (File file : foundFiles) {
                tableModel.addRow(new Object[] {
                        file.getName(),
                        file.length() + " bytes",
                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(file.lastModified()),
                        file.getAbsolutePath()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Diretório inválido.");
        }
    }

    private void organizeFiles() {
        if (foundFiles == null || foundFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum arquivo encontrado para organizar.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File targetDirectory = chooser.getSelectedFile();
            UtilitariosDeArquivos.organizeFiles(foundFiles, targetDirectory);
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class UtilitariosDeArquivos {

    public static void styleTextField(JTextField textField, Color textColor, Color bgColor) {
        textField.setForeground(textColor);
        textField.setBackground(bgColor);
        textField.setCaretColor(textColor);
        textField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
    }

    public static JButton createIconButton(String text, String tooltip, String icon, Color bgColor, Color hoverColor,
            Color textColor) {
        JButton button = new JButton(icon + " " + text);
        button.setToolTipText(tooltip);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    public static JPanel createLabeledField(String labelText, JTextField textField, JButton button, Color textColor,
            Color panelColor) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(panelColor);
        JLabel label = new JLabel(labelText);
        label.setForeground(textColor);
        panel.add(label, BorderLayout.NORTH);
        if (button != null) {
            JPanel row = new JPanel(new BorderLayout(5, 5));
            row.setBackground(panelColor);
            row.add(textField, BorderLayout.CENTER);
            row.add(button, BorderLayout.EAST);
            panel.add(row, BorderLayout.CENTER);
        } else {
            panel.add(textField, BorderLayout.CENTER);
        }
        return panel;
    }

    public static void searchDirectory(File directory, String nameFilter, String extensionFilter,
            List<File> foundFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchDirectory(file, nameFilter, extensionFilter, foundFiles);
                } else {
                    boolean matches = true;
                    if (!nameFilter.isEmpty() && !file.getName().contains(nameFilter)) {
                        matches = false;
                    }
                    if (!extensionFilter.isEmpty() && !file.getName().endsWith(extensionFilter)) {
                        matches = false;
                    }
                    if (matches) {
                        foundFiles.add(file);
                    }
                }
            }
        }
    }

    public static void organizeFiles(List<File> files, File targetDirectory) {
        for (File file : files) {
            try {
                Files.copy(file.toPath(), new File(targetDirectory, file.getName()).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao organizar arquivo: " + file.getName());
            }
        }
        JOptionPane.showMessageDialog(null, "Arquivos organizados com sucesso!");
    }
}

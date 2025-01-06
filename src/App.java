import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrganizadorDeArquivos organizador = new OrganizadorDeArquivos();
            organizador.setVisible(true);
        });
    }
}

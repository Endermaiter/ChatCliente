import javax.swing.*;

public class Cliente extends JFrame{

    private JTextField textField;
    private JButton button1;
    private JTextArea textArea;
    private JPanel jPanel;

    public Cliente(){
        super("CHAT - CLIENTE");
        setContentPane(jPanel);
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new Cliente();
            frame.setSize(400,300);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
        });
    }
}

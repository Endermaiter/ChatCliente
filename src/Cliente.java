import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static java.awt.event.KeyEvent.VK_ENTER;

public class Cliente extends JFrame {

    private JTextField textField;
    private JButton buttonSend;
    private JTextArea textArea;
    private JPanel jPanel;
    private JButton cerrarClienteYServidorButton;

    public Cliente() {
        super("CHAT - CLIENTE");
        setContentPane(jPanel);
        buttonSend.addActionListener(e -> {
            try {
                actionEvent(1);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getExtendedKeyCode() == VK_ENTER) buttonSend.doClick();
            }
        });
        cerrarClienteYServidorButton.addActionListener(e -> {
            try {
                actionEvent(0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new Cliente();
            frame.setSize(610, 500);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
        });
    }

    public void actionEvent(int exit) throws IOException {

        //Creamos un nuevo socket (DEVOLUCION)
        ServerSocket clienteSocketDev = new ServerSocket();
        //Asignamos ip y puerto al socket para recibir los datos del servidor
        InetSocketAddress addr = new InetSocketAddress("localhost", 6666);
        clienteSocketDev.bind(addr);

        //**ENVIAR DATO**

        //Recogida de dato en variable
        String mensaje = textField.getText();
        textField.setText("");
        //Recogida del temaño del dato en variable
        int longitudCadea = mensaje.length();

        //Creando socket de cliente
        Socket socketCliente = new Socket();
        System.out.println("Estableciendo la conexión con el servidor...\n");

        //Asignando direccion ip y puerto
        InetSocketAddress addrRecibir = new InetSocketAddress("localhost", 5555);
        socketCliente.connect(addrRecibir);

        //Instancia del objeto de salida
        OutputStream os = socketCliente.getOutputStream();
        DataOutputStream daOu = new DataOutputStream(os);
        System.out.println("¡¡Conexión aceptada!!\n");

        //Condicionante que determina si quiero enviar un dato al servidor o cerrarlo

        if (exit == 1) {

            //Enviando datos
            System.out.println("Enviando datos...\n");
            daOu.writeUTF(mensaje);
            System.out.println("¡¡Datos enviados!!\n");

        } else {
            daOu.writeUTF("Exit");
            System.exit(0);
        }

        //Cerrando socket del cliente (port:5555)
        socketCliente.close();

        //**DEVOLUCION DEL DATO**

        //Esperamos a que llegue la conexion y la acepte
        System.out.println("Esperando respuesta del servidor...\n");
        Socket newSocketRecibir = clienteSocketDev.accept();
        System.out.println("Respuesta recibida...\n");
        System.out.println("Mostrando respuesta al cliente...\n");

        //Instanciamos el objeto de entrada
        InputStream is = newSocketRecibir.getInputStream();
        DataInputStream daIn = new DataInputStream(is);
        //Metemos el dato recibido en una variable
        String mensajeDev = daIn.readUTF();
        textArea.setText(textArea.getText() + "[SERVER]> " +mensajeDev + "\n");

        //Una vez mostrado el dato junto al mensaje, ceramos los sockets
        socketCliente.close();
        clienteSocketDev.close();
    }
}

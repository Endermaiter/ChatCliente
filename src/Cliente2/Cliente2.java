package Cliente2;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static java.awt.event.KeyEvent.VK_ENTER;

public class Cliente2 extends JFrame {

    //Variables referentes a los componentes
    private JTextField textField;
    private JButton buttonSend;
    private JTextArea textArea;
    private JPanel jPanel;
    private JButton cerrarClienteYServidorButton;

    //Constructor de la interfaz
    public Cliente2() {
        super("CHAT - CLIENTE 2");
        setContentPane(jPanel);
        //Evento del boton de enviar el dato
        buttonSend.addActionListener(e -> {
            try {
                actionEvent(1);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        //KeyEvent que permite mandar el mensaje con la tecla Enter tambien(comodidad)
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getExtendedKeyCode() == VK_ENTER) buttonSend.doClick();
            }
        });
        //Boton para cerrar ambos programas
        cerrarClienteYServidorButton.addActionListener(e -> {
            try {
                actionEvent(0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public static void main(String[] args) {
        //Estructuracion de la interfaz
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new Cliente2();
            frame.setSize(610, 500);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
        });
    }

    public void actionEvent(int exit) throws IOException {

        //**RECIBIR MENSAJE**

        //Creamos un nuevo socket
        ServerSocket clienteSocketDev = new ServerSocket();
        //Asignamos ip y puerto al socket para recibir los datos del servidor
        InetSocketAddress addr = new InetSocketAddress("localhost", 6666);
        //Realizamos el bind
        clienteSocketDev.bind(addr);

        //**ENVIAR MENSAJE**

        //Recogida de mensaje en variable
        String mensaje = textField.getText();
        //Limpieza del campo de texto
        textField.setText("");

        //Creando socket de cliente
        Socket socketCliente = new Socket();
        System.out.println("Estableciendo la conexión con el servidor...\n");

        //Asignando direccion ip y puerto
        InetSocketAddress addrRecibir = new InetSocketAddress("localhost", 5555);
        socketCliente.connect(addrRecibir);

        //Instancia del objeto de salida
        OutputStream os = socketCliente.getOutputStream();
        //Instanciamos un DataOutputStream para facilitar la escritura
        DataOutputStream daOu = new DataOutputStream(os);
        System.out.println("¡¡Conexión aceptada!!\n");

        //Condicionante que determina si quiero enviar un mensaje al servidor o cerrarlo

        if (exit == 1) {

            //Enviando mensaje
            System.out.println("Enviando datos...\n");
            daOu.writeUTF(mensaje);
            System.out.println("¡¡Datos enviados!!\n");

        } else {
            //Enviando mensaje de que se cierre
            daOu.writeUTF("Exit");
            System.exit(0);
        }

        //Cerrando socket del cliente (port:5555)
        socketCliente.close();

        //**RECIBIR MENSAJE**

        //Esperamos a que llegue la conexion y la acepte
        System.out.println("Esperando respuesta del servidor...\n");
        Socket newSocketRecibir = clienteSocketDev.accept();
        System.out.println("Respuesta recibida...\n");
        System.out.println("Mostrando respuesta al cliente...\n");

        //Instanciamos el objeto de entrada
        InputStream is = newSocketRecibir.getInputStream();
        //Instanciamos un DataInputStream para facilitar la lectura
        DataInputStream daIn = new DataInputStream(is);
        //Metemos el mensaje recibido en una variable
        String mensajeDev = daIn.readUTF();
        //Lo mostramos al cliente
        textArea.setText(textArea.getText() + "[SERVER]> " + mensajeDev + "\n");

        //Una vez mostrado el mensaje, cerramos los sockets
        socketCliente.close();
        clienteSocketDev.close();
    }
}

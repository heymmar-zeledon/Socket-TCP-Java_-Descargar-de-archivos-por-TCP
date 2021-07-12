/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eliazith-zeledon
 */
import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Server_TCP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket Connect = null;

        File[] Lista = null;

        String NameFileRecive;

        String User = System.getProperty("user.name");
        String Ruta = "/home/" + User + "/Documentos";

        File Directory = new File(Ruta);

        ServerSocket Server = null;
        BufferedOutputStream EnviarClienteFile;
        try {
            Server = new ServerSocket(1099);
        } catch (Exception e) {
            System.out.println("Error en el Server Socket: ");
        }
        if (!Directory.exists()) {
            JOptionPane.showMessageDialog(null, "Creando el directorio: " + Directory.exists());

            try {
                Directory.mkdir();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al crear directorio " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Servidor listo");

            /*Se sacan los ficheros del directorio*/
            try {
                Lista = Directory.listFiles();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error  en la lista" + e.getMessage());
            }

        }

        try {

            while (true) {
                byte[] Bufer;
                Connect = Server.accept();
                JOptionPane.showMessageDialog(null, "Se hizo una conexion con : " + Connect.getInetAddress());

                EnviarClienteFile = new BufferedOutputStream(Connect.getOutputStream());

                DataOutputStream EscribirEncliente = new DataOutputStream(Connect.getOutputStream());
                DataInputStream LeerCliente = new DataInputStream(Connect.getInputStream());

                EscribirEncliente.writeUTF("Conexion Aceptada");

                NameFileRecive = LeerCliente.readUTF();
                File ArchivoEnviar = null;
                if (Lista != null) {
                    for (File file : Lista) {

                        if (NameFileRecive.equals(file.getName())) {
                            ArchivoEnviar = file;
                        }

                    }
                }

                if (!ArchivoEnviar.exists() || ArchivoEnviar == null) {
                    EscribirEncliente.writeUTF("El archivo no existe");
                } else {

                    FileInputStream LeeFile = new FileInputStream(ArchivoEnviar);

                    int TamByte = (int) ArchivoEnviar.length();
                    Bufer = new byte[TamByte];

                    LeeFile.read(Bufer);

                    EscribirEncliente.writeUTF("Descargando...");

                    EscribirEncliente.writeInt(TamByte);
                    EscribirEncliente.writeInt(Bufer.length);
                    EscribirEncliente.write(Bufer);
                    EscribirEncliente.writeUTF(ArchivoEnviar.getName());

                }
                System.out.println("En espera de respuesta");
                //Se espera que el cliente envie una cadena diciendo s desea terminar la conexion
                String CadenaCerrar = LeerCliente.readUTF();

                if (CadenaCerrar.equals("close")) {
                    EscribirEncliente.writeUTF("Conexion cerrada");

                    /* Cerrando la conexion */
                    Connect.close();
                    Server.close();
                    System.out.println("Cerrado");
                    break;
                }
                else
                {
                    EscribirEncliente.writeUTF("Continuacion");
                    EnviarClienteFile.close();
                    EscribirEncliente.close();
                    LeerCliente.close();
                    Connect.close();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error server : " + e.getMessage());
        }

    }

    
}

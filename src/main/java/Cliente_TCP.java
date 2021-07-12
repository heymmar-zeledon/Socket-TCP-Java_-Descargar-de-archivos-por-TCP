/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eliazith-zeledon
 */
import java.net.Socket;
import java.io.*;
import javax.swing.JOptionPane;
public class Cliente_TCP {

    /**
     * @param args the command line arguments
     */
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String IpServe;
        String NameFile;
        String MensajeServe;
        String MensajeClient;
        int a = 0;

        IpServe = JOptionPane.showInputDialog("Direccion Ip del servidor: ");

        try {
            while (true) {
                Socket Connect = new Socket(IpServe, 1099);
                DataInputStream InputCliente = new DataInputStream(Connect.getInputStream());
                DataOutputStream OupoutServe = new DataOutputStream(Connect.getOutputStream());
                MensajeClient = InputCliente.readUTF();
                JOptionPane.showMessageDialog(null,MensajeClient);

                NameFile = JOptionPane.showInputDialog("Ingresa el nombre del archivo a descargar");
                MensajeServe = NameFile;
                OupoutServe.writeUTF(MensajeServe);

                MensajeClient = InputCliente.readUTF();
                byte[] Bufer;

                if (MensajeClient.equals("El archivo no existe")) {
                    JOptionPane.showMessageDialog(null, "El archivo ya no existe en el server");
                } else {
                    JOptionPane.showMessageDialog(null, MensajeClient);
                
                    int TamByte = InputCliente.readInt();
                    int CantByte = InputCliente.readInt();
                    JOptionPane.showMessageDialog(null,"Tamaño archivo: "+TamByte);
                    Bufer = new byte[TamByte];
                    
                    InputCliente.readFully(Bufer,0, Bufer.length);
                    
                    NameFile = InputCliente.readUTF();
                    
                    FileOutputStream SaveFile = new FileOutputStream(NameFile);
                    
                    SaveFile.write(Bufer);
                    SaveFile.close();
                    System.out.println("...");
                    JOptionPane.showMessageDialog(null, "Archivo descargado con exito");
                }
                try {
                         a = Integer.parseInt(JOptionPane.showInputDialog("Desea detener la conexion?\n1.:Cerrar\n2.:continuar"));
                    } 
                    catch (Exception e) 
                    {
                        a = 0;
                        JOptionPane.showMessageDialog(null,"variable no asiginada "+e.getMessage());
                    }
                    if (a == 2) {
                        MensajeServe = "continuar";
                        OupoutServe.writeUTF(MensajeServe);
                        InputCliente.close();
                        OupoutServe.close();
                    } 
                    else
                    {
                        MensajeServe = "close";
                        OupoutServe.writeUTF(MensajeServe);
                        InputCliente.close();
                        OupoutServe.close();
                        break;
                    }
                    Connect.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Cliente: " + e.getMessage());
        }

    }
    
}

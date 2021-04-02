import nhapp.Student;

import java.io.*;
import java.net.*;

public class ServerUDP {
    public static void main(String[] args) {
        Student student1;

        try{
            DatagramSocket serverSocket = new DatagramSocket(4127);
            byte[] receiveData = new byte[1024];

            do {
                // Receiving...
                DatagramPacket receivePacket =
                        new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                byte[] data = receivePacket.getData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);

                student1 = (Student) ois.readObject();
                ois.close();

                // Getting IP and port...
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                // Handling request...
                Student student2 = new Student(student1.getName() + " dep trai",
                        student1.getAge());

                // Sending...
                ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));

                oos.flush();
                oos.writeObject(student2);
                oos.flush();

                byte[] sendData = baos.toByteArray();
                DatagramPacket sendPacket =
                        new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
                oos.close();

            } while (true);
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
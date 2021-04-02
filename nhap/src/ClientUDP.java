import nhapp.Student;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientUDP {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

//        InetAddress ip = InetAddress.getByName("localhost");
//        System.out.println(ip);

        Student student1 = new Student(name, 21);
        Student student2;

        try {
            InetAddress IPAddress = InetAddress.getByName("localhost");
            System.out.println(IPAddress);
            DatagramSocket clientSocket = new DatagramSocket(7777);
            do {
                // Sending...
                ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));

                oos.flush();
                oos.writeObject(student1);
                oos.flush();

                byte[] sendData = baos.toByteArray();

                DatagramPacket sendPacket =
                        new DatagramPacket(sendData, sendData.length, IPAddress, 4127);
                clientSocket.send(sendPacket);
                oos.close();

                // Receiving...
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket =
                        new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                byte[] data = receivePacket.getData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);

                student2 = (Student) ois.readObject();
                ois.close();

                // Display response
                System.out.println(student2.toString());
            } while (true);
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
package ch.sven1222225.RFID;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;
import java.util.Scanner;

public class App
{
    public static void main( String[] args ) {
        // List all serial ports
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Available Ports:");
        for (int i = 0; i < ports.length; i++) {
            System.out.println(i + ": " + ports[i].getSystemPortName());
        }

        // Choose the correct port (update index if needed)
        SerialPort comPort = ports[0]; // Change if needed

        // Configure the port
        comPort.setBaudRate(115200); // Match Arduino's Serial.begin(115200)
        if (!comPort.openPort()) {
            System.out.println("Failed to open port!");
            return;
        }

        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        System.out.println("Port opened. Reading data...");
        Scanner rawData = new Scanner(comPort.getInputStream());

        boolean running = true;
        boolean isInDataBlock = false;
        RFID rfid = new RFID();

        while (running) {
            if (rawData.hasNextLine()) {
                String line = rawData.nextLine();
                System.out.println("Arduino: " + line);

                // End Data
                if(isInDataBlock && line.startsWith("#End Data")){
                    isInDataBlock = false;
                    displayData(rfid);
                }

                // Save Data
                if(isInDataBlock) {
                    rfid.addRawData(line);
                }

                // Start Data
                if(line.startsWith("#Start Data")){
                    isInDataBlock = true;
                    rfid.clear();
                }

            }

            if (!comPort.isOpen()) {
                System.out.println("Port closed unexpectedly.");
                running = false;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }

        rawData.close();
        comPort.closePort();
        System.out.println("Port closed.");
    }

    private static void displayData(RFID rfid){
        System.out.println("Begin Debug Output");
        System.out.println("Card UID: " + rfid.getCradUID());
        System.out.println("Card SAK: " + rfid.getCradSAK());
        System.out.println("PICC type: " + rfid.getPICCType());
        System.out.println("Sector Block");
    }
}

/*

#Start Data
Card UID: A9 CF 24 C2
Card SAK: 08
PICC type: MIFARE 1KB
Sector Block   0  1  2  3   4  5  6  7   8  9 10 11  12 13 14 15  AccessBits
  15     63   00 00 00 00  00 00 FF 07  80 69 FF FF  FF FF FF FF  [ 0 0 1 ]
         62   00 00 00 00  00 00 00 00  00 00 00 00  00 00 00 00  [ 0 0 0 ]
         61   00 00 00 00  00 00 00 00  00 00 00 00  00 00 00 00  [ 0 0 0 ]
         60   00 00 00 00  00 00 00 00  00 00 00 00  00 00 00 00  [ 0 0 0 ]
  14     59   00 00 00 00  00 00 FF 07  80 69 FF FF  FF FF FF FF  [ 0 0 1 ]
         58   00 00 00 00  00 00 00 00  00 00 00 00  00 00 00 00  [ 0 0 0 ]
         57   00 00 00 00  00 00 00 00  00 00 00 00  00 00 00 00  [ 0 0 0 ]
         56   00 00 00 00  00 00 00 00  00 00 00 00  00 00 00 00  [ 0 0 0 ]
  13     55   00 00 00 00  00 00 FF 07  80 69 FF FF  FF FF FF FF  [ 0 0 1 ]
         54   00 00 00 00  00 00 00 00  00 00 00 00  00 00 00 00  [ 0 0 0 ]
         53   00 00 00 00  00 00 00 00  00 00 00 00  00 00 00 00  [ 0 0 0 ]
         52   00 00 00 00  00 00 00 00  00 00 00 00  00 00 00 00  [ 0 0 0 ]
  12     51   00 00 00 00  00 00 FF 07  80 69 FF FF  FF FF FF FF  [ 0 0 1 ]
         50  MIFARE_Read() failed: Timeout in communication.
         49  MIFARE_Read() failed: Timeout in communication.
         48  MIFARE_Read() failed: Timeout in communication.
  11     47  PCD_Authenticate() failed: Timeout in communication.
  10     43  PCD_Authenticate() failed: Timeout in communication.
   9     39  PCD_Authenticate() failed: Timeout in communication.
   8     35  PCD_Authenticate() failed: Timeout in communication.
   7     31  PCD_Authenticate() failed: Timeout in communication.
   6     27  PCD_Authenticate() failed: Timeout in communication.
   5     23  PCD_Authenticate() failed: Timeout in communication.
   4     19  PCD_Authenticate() failed: Timeout in communication.
   3     15  PCD_Authenticate() failed: Timeout in communication.
   2     11  PCD_Authenticate() failed: Timeout in communication.
   1      7  PCD_Authenticate() failed: Timeout in communication.
   0      3  PCD_Authenticate() failed: Timeout in communication.

#End Data

 */
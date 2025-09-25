package ch.sven1222225.RFID;

import java.util.ArrayList;
import java.util.HashMap;

public class RFID {
    private HashMap<String, String> rawHead;
    private HashMap<Integer, String> rawData;
    private static String[] HEAD_SIGNATURE = {"Card UID", "Card SAK", "PICC type", "Sector Block"};

    public RFID(){
        this.rawHead = new HashMap<String, String>();
        this.rawData = new HashMap<Integer, String>();
    }

    public HashMap<String, String> getRawHead() {
        return rawHead;
    }

    public HashMap<Integer, String> getRawData() {
        return rawData;
    }

    public String getCradUID(){
        return this.rawHead.get("Card UID");
    }

    public String getCradSAK(){
        return this.rawHead.get("Card SAK");
    }

    public String getPICCType(){
        return this.rawHead.get("PICC type");
    }

    public String getSectorBlock(){
        return this.rawHead.get("Sector Block");
    }

    public String getSectorData(int dataIndex){
        String ret = null;

        if(this.rawData.containsKey(dataIndex)){
            ret = this.rawData.get(dataIndex);
        }

        return ret;
    }

    public void addRawData(String data) {
        data = data.trim();

        for(String headSig : HEAD_SIGNATURE){
            if(data.startsWith(headSig)){
                this.rawHead.put(headSig, data.replace(headSig, "").trim());
                return;
            }
        }

        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("^\\s*(\\d+)").matcher(data);

        if (matcher.find()) {
            Integer sector = Integer.parseInt(matcher.group(1));
            this.rawData.put(sector, data.replace("^\\s*\\d+\\s*\\d+", "").trim());
        }
    }

    public void clear(){
        this.rawHead.clear();
        this.rawData.clear();
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
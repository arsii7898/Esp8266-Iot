#include <ESP8266WiFi.h>
#include "AES.h"
#include "Base64.h"
//these are server side variables old one//
const char* ssid     = "HomeAutomationSystem";
const char* password = "home12345678";
WiFiServer server(80); 
IPAddress ip(192,168,4,1);
IPAddress subnet(255,255,255,0);  
///////done here 7898

AES aes;

// Our AES key. Note that is the same that is used on the Node-Js side but as hex bytes.
byte key[] = {0x7e, 0x4e, 0x42, 0x38, 0x43, 0x63, 0x4f, 0x4c, 0x23, 0x4a, 0x21, 0x48, 0x3f, 0x7c, 0x59, 0x72};

// The unitialized Initialization vector
byte iv[N_BLOCK] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

int size1;
String   IV_base64;
String msg = "";
String encmsg="";
String dcmsg="";
String ivmsg="";

uint8_t generate_random_unit8()
{
  uint8_t really_random = *(volatile uint8_t *)0x3FF20E44;
  return really_random;
}



void setup() {
WiFi.mode(WIFI_AP);
  WiFi.softAP(ssid, password);
  delay(2000);
WiFi.config(192,168,4,1);
server.begin();
Serial.begin(115200);

}

void loop() {
  
  WiFiClient client = server.available();
    
 if (client) {
   while (client.connected()) {
      if (client.available()) {
     String req = client.readStringUntil('\r');
    Serial.println(req);
     String extra = getValue(req, '?', 0);
    String ip = getValue(req, '?', 1);
    String status1 = getValue(req, '?', 2);
    Serial.println("ip="+ip);
   Serial.println("req="+status1);
   
       String text;
     
        msg=status1;
        encrypt();
        String sendenc=""+encmsg+":"+ivmsg+":"+size1;


//////

int Parts[4] = {0,0,0,0};
int Part = 0;
for ( int i=0; i<ip.length(); i++ )
{
  char c = ip[i];
  if ( c == '.' )
  {
    Part++;
    continue;
  }
  Parts[Part] *= 10;
  Parts[Part] += c - '0';
}
IPAddress ip2( Parts[0], Parts[1], Parts[2], Parts[3] );
//////


 


     
        
        WiFiClient client2;
     if (client2.connect(ip2, 80)) { client2.println(sendenc); text="Request Proccess Successfully"; client2.stop();  }else{ Serial.println("Unavalible");text="unavaible"; }
       

        
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");
          client.println("Connection: close");  
          client.println("Refresh: 5");  
          client.println();
          client.println(text);
           break;
       
       
      }
    }
  
    delay(1);

    client.stop();
   
  }  
}





/////my functions/////

// Generate a random initialization vector
void generate_iv(byte *vector)
{
  for (int i = 0; i < N_BLOCK; i++)
  {
    vector[i] = (byte)generate_random_unit8();
  }
}


void encrypt()
{
  char b64data[200];
  byte cipher[1000];
  byte iv[N_BLOCK];

  generate_iv(iv);

  base64_encode(b64data, (char *)iv, N_BLOCK);
  IV_base64 = String(b64data);


  int b64len = base64_encode(b64data, (char *)msg.c_str(), msg.length());


  // Encrypt! With AES128, our key and IV, CBC and pkcs7 padding
  aes.do_aes_encrypt((byte *)b64data, b64len, cipher, key, 128, iv);

 

  base64_encode(b64data, (char *)cipher, aes.get_size());
 
 encmsg= String(b64data);
 ivmsg=IV_base64;
 size1=aes.get_size();
 
 
}




void setup_aes()
{
  aes.set_key(key, sizeof(key)); // Get the globally defined key
}

////////

String getValue(String data, char separator, int index)
{
    int found = 0;
    int strIndex[] = { 0, -1 };
    int maxIndex = data.length() - 1;

    for (int i = 0; i <= maxIndex && found <= index; i++) {
        if (data.charAt(i) == separator || i == maxIndex) {
            found++;
            strIndex[0] = strIndex[1] + 1;
            strIndex[1] = (i == maxIndex) ? i+1 : i;
        }
    }
    return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}


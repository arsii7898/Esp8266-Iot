
#include <ESP8266WiFi.h>
#include "AES.h"
#include "Base64.h"
AES aes;
byte key[] = {0x7e, 0x4e, 0x42, 0x38, 0x43, 0x63, 0x4f, 0x4c, 0x23, 0x4a, 0x21, 0x48, 0x3f, 0x7c, 0x59, 0x72};
String msg="";
  
const char* ssid     = "HomeAutomationSystem";
const char* password = "home12345678";
         
                          //d1,d2,d5.d0
WiFiServer server(80); 

IPAddress ip(192,168,4,123);
IPAddress gateway(192,168,4,1);
IPAddress subnet(255,255,255,0);   
int relay1=0;
int relay2=2;

              
void setup()
{

  
  
  ESP.eraseConfig();
  WiFi.mode(WIFI_STA);
  WiFi.config(ip, gateway, subnet);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    }
  server.begin();
  pinMode(relay1,OUTPUT);
  digitalWrite(relay1,LOW);
  pinMode(relay2,OUTPUT);
  digitalWrite(relay2,LOW);
  Serial.begin(115200);
 }

 
void loop()
{
 if (WiFi.status() != WL_CONNECTED) { 
  ESP.restart();
 }
  WiFiClient client = server.available();
    
 if (client) {
   while (client.connected()) {
      if (client.available()) {
     String req = client.readStringUntil('\r');
Serial.println("message="+req);
 String xval = getValue(req, ':', 0);
 String yval = getValue(req, ':', 1);
 String zval = getValue(req, ':', 2);
        

     decrypt(xval, yval, zval.toInt());
     Serial.println("message="+msg);
   if(msg.indexOf("1=on")>=0){digitalWrite(relay1,HIGH);}
   if(msg.indexOf("1=off")>=0){digitalWrite(relay1,LOW);}
   if(msg.indexOf("2=on")>=0){digitalWrite(relay2,HIGH);}
   if(msg.indexOf("2=off")>=0){digitalWrite(relay2,LOW);}
    



            client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");
          client.println("Connection: close");  
          client.println("Refresh: 5"); 
          client.println();
        
           break;
       
       
      }
    }
    
    delay(1);
    
   
    client.stop();
   


 }}





 void decrypt(String b64data, String IV_base64, int size)
{
  char data_decoded[200];
  char iv_decoded[200];
  byte out[200];
  char temp[200];
  b64data.toCharArray(temp, 200);
  base64_decode(data_decoded, temp, b64data.length());
  IV_base64.toCharArray(temp, 200);
  base64_decode(iv_decoded, temp, IV_base64.length());
  aes.do_aes_decrypt((byte *)data_decoded, size, out, key, 128, (byte *)iv_decoded);
  char message[msg.length()];
  base64_decode(message, (char *)out, 75);
   
msg=message;
}

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

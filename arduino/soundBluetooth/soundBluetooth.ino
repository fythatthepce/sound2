//HC-O5 Bluetooth
#include <SoftwareSerial.h>


//PIN SET NODEMCU V2
//pwm D1~D12
#define D1 5  
#define D2 4  
#define D3 0  
#define D4 2 
#define D5 14  
#define D6 12
#define D7 13
#define D8 15


//set BTSerial D4 =  RX , D8 = TX 
SoftwareSerial BTSerial(D4, D8); // RX | TX

char command; //command = string from android studio
String string;  //string of arduno




  void setup()
  {
    
    pinMode(D2, OUTPUT); //RIGHT
    pinMode(D3, OUTPUT); //LEFT
    pinMode(D5, OUTPUT); //GO
    pinMode(D6, OUTPUT); //DOWN
    
    Serial.begin(9600);
    BTSerial.begin(9600); 
  }

  void loop()
  {
    
    if (BTSerial.available() > 0) 
    {string = "";}    //init string = NULL
    
    while(BTSerial.available() > 0)
    {
      command = ((byte)BTSerial.read());  //get string from android studio
      
      if(command == ':')
      {
        break;
      }
      
      else
      {
        string += command;  //move command(string from android studio) to string of arduino
      }
      
      delay(1);
    }
    
    if(string == "RIGHT") //if string of arduino == TO
    {
        ledOff();
        RIGHT_On();

    }

     if(string == "LEFT") //if string of arduino == TO
    {
        ledOff();
        LEFT_On();

    }

    if(string == "GO") //if string of arduino == TO
    {
        ledOff();
        GO_On();

    }

     if(string == "DOWN") //if string of arduino == TO
    {
        ledOff();
        DOWN_On();
    }

    
    
    if(string =="STOP") //if string of arduino == TF
    {
        ledOff();

    }
    Serial.println(string);  //show string of arduino in serial monitor
    
 }


//fuct to do
void RIGHT_On()
{
      digitalWrite(D2,HIGH);
      delay(10);
}

void LEFT_On()
{
      digitalWrite(D3,HIGH);
      delay(10);
}

void GO_On()
{
      digitalWrite(D5,HIGH);
      delay(10);
}

void DOWN_On()
{
      digitalWrite(D6,HIGH);
      delay(10);
}



 
 void ledOff()
 {
      digitalWrite(D2, LOW);
      digitalWrite(D3, LOW);
      digitalWrite(D5, LOW);
      digitalWrite(D6, LOW);
      delay(10);
 }
 

  

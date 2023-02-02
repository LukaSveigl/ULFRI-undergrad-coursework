#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include <LiquidCrystal_I2C.h>

/** The approximate pressure at sea level */
#define SEALEVELPRESSURE_HPA (1013.25)

Adafruit_BME280 bme;
LiquidCrystal_I2C lcd(0x27, 16, 2);

/** 
 *  Flag that signifies if we are currently debugging. Note that this enables 
 *  certain parts of code that were previously not used.
 */ 
bool debug = false;

void setup() {
  Serial.begin(9600);

  // Sensor code here:

  bool status;

  // Check if sensor is connected, if not, stop execution.
  status = bme.begin(0x76);
  if (!status) {
    Serial.println("Could not find a valid BME280 sensor, check wiring!");
    while(1);
  }

  // Display code here:
  
  // When debugging display, find it's I2C address which is displayed in the serial monitor.
  if (debug) {
    findAddr();
  }

  // Initialize the display.
  lcd.init();
  lcd.clear();
  lcd.backlight();

  // Display initial message.
  lcd.setCursor(2,0);  
  lcd.print("Initializing");
  lcd.setCursor(2,1); 
  lcd.print("station");
}

void loop() {
  // Sensor code here:
  printValues();
  delay(1000);

  // Display code here:
  lcd.clear();
  displayTemp();
  displayHum();
  delay(2000);
  
  lcd.clear();
  displayPress();
  displayAlt();
  delay(2000);
}


/**
 * LCD display functions.
 */

void displayTemp() {
  // Assemble the message of form: "Temp = val.val*C"
  lcd.setCursor(0,0);
  lcd.print("Temp = ");   
  lcd.setCursor(7, 0);
  lcd.print(bme.readTemperature());
  lcd.setCursor(12, 0);
  lcd.print("*C");
}

void displayHum() {
  // Assemble the message of form: "Hum = val.val%"
  lcd.setCursor(0,1);
  lcd.print("Hum = ");   
  lcd.setCursor(6, 1);
  lcd.print(bme.readHumidity());
  lcd.setCursor(11, 1);
  lcd.print("%");
}

void displayPress() {
  // Assemble the message of form: "Prs = val.valhPa"
  lcd.setCursor(0,0);
  lcd.print("Prs = ");   
  lcd.setCursor(6, 0);
  lcd.print(bme.readPressure() / 100.0F);
  lcd.setCursor(13, 0);
  lcd.print("hPa");
}

void displayAlt() {
  // Assemble the message of form: "Alt = val.valm"
  lcd.setCursor(0,1);
  lcd.print("Alt = ");   
  lcd.setCursor(6, 1);
  lcd.print(bme.readAltitude(SEALEVELPRESSURE_HPA));
  lcd.setCursor(12, 1);
  lcd.print("m");
}


/**
 * Sensor display functions.
 */

void printValues() {
  Serial.print("Temperature = ");
  Serial.print(bme.readTemperature());
  Serial.println(" *C");
  
  Serial.print("Pressure = ");
  Serial.print(bme.readPressure() / 100.0F);
  Serial.println(" hPa");
  
  Serial.print("Approx. Altitude = ");
  Serial.print(bme.readAltitude(SEALEVELPRESSURE_HPA));
  Serial.println(" m");
  
  Serial.print("Humidity = ");
  Serial.print(bme.readHumidity());
  Serial.println(" %");
  
  Serial.println();
}


/**
 * Debug functions.
 */

void findAddr() {
  // Wait until serial is initialized.
  while (!Serial) 
    {
    }

  Serial.println ();
  Serial.println ("I2C scanner. Scanning ...");
  byte count = 0;
  
  Wire.begin();
  for (byte i = 8; i < 120; i++)
  {
    Wire.beginTransmission (i);
    if (Wire.endTransmission () == 0)
      {
      Serial.print ("Found address: ");
      Serial.print (i, DEC);
      Serial.print (" (0x");
      Serial.print (i, HEX);
      Serial.println (")");
      count++;
      delay (1);  // maybe unneeded?
      } // end of good response
  } // end of for loop
  Serial.println ("Done.");
  Serial.print ("Found ");
  Serial.print (count, DEC);
  Serial.println (" device(s).");
}

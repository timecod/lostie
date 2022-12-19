#include <MFRC522.h>
#include <SPI.h>
#include <Servo.h>
// пины считывателя
#define SAD 10
#define RST 9
// считыватель
MFRC522 nfc(SAD, RST);
// сервопривод
Servo ms;
// параметры кнопки
bool mode = 0; // режим работы (2): автоматически и ручной
bool old_button = 0; // старое значение кнопки
// параметры сервопривода
int h = 0; // угол сервопривода 
int k = 1; // скорость сервопривода
// параметры считывателя
byte status; // статус (приложена ли карта)
byte data[MAX_LEN]; // данные с карты
byte last = 0; // старов
int leds [2] = {7, 5}; // пины светодиодов
bool leds_status [2] = {}; // включены или выключены светодиоды
// сохранение местоположения карты
byte save [100] = {}; // массив карт (четная позиция - id карты, нечетная - местоположение карты)
int n = 0; // количество карт
// операционные
bool flag; // флаг для операции узнавания есть ли карта в массиве

void setup() {
  // подключение считывателя
  SPI.begin();
  nfc.begin();
  // подключение светодиодов
  for (int i = 0; i < 2; ++i)
    pinMode(leds[i], OUTPUT);
  // поключение кнопки
  pinMode(2, INPUT);
  // подключение сервопривода
  ms.attach(A0);
  // для вывода в терминал
  Serial.begin(115200);
}

void loop() {
  // сервопривод
  if (mode){
    // автоматический режим
    if (h == 0) k = 1;
    if (h == 180) k = -1;
    h += k;
  } else {
    // ручной
    h = map(analogRead(A1),0, 1080, 0, 180);
  }
  // установка угла поворота сервопривода
  ms.write(h);
  // считыватель
  status = nfc.requestTag(MF1_REQIDL, data); // попытка установления соединения
  nfc.antiCollision(data); // считываем данные
  Serial.println(data[0]); // вывод первого байта
  if (last == 2 && status != last) {
    //если в прошлой итерации карта была поднесена, а сейчас нет, то сохраняем или обновляем информацию о её местоположении
    flag = 1;
    for (int i = 0; i < n*2; i+=2)
      if (save[i] == data[0]) {
        // нашли номер карты, обновляем местоположение
        flag = 0;
        save[i+1] = h;
        break;
      }
    if (flag) {
      // не нашли, сохраняем информацию
      save[n*2] = data[0];
      save[n*2+1] = h;
      ++n;
    }
  } 
  // сохраняем прошлое значение статуса и отключаемся от карты когда надо
  if (status == 2) { last = 2; nfc.haltTag(); }
  else {last = 0;}
  
  // кнопка
  if (digitalRead(2) != old_button && digitalRead(2)) {
    // если сейчас статус кнопки поменялся и она нажата, то меняем режим работы
    mode = !mode;
  }
  // сохраняем старое значение кнопки
  old_button = digitalRead(2);

  // светодиоды
  leds_status[0] = leds_status[1] = 0; // обнуляем значения 
  // Если сейчас в интервале последнего местоположения карты изменяем значение светодиода
  for (int i = 0; i < n*2; i+=2){
    if (save[i+1] + 5 > h && h > save[i+1] - 5) {
      leds_status[i/2] = 1;
    }
  }
  // зажигаем нужные светодиоды
  for (int i = 0; i < 2; ++i)
    digitalWrite(leds[i], leds_status[i]);

  // частота работы
  delay(5);
}

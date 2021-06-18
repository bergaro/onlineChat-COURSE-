## Сетевой чат

Server - серверное приложение чата;

Client - клинетское приложение чата;

ClientServerOnly - клиент для теста многопоточности;

### Логи чата 

Логи сервера расположены в файле - server.log;

Логи клиента расположены в файле - client.log

Логи тестового клиента_2 расположены в файле - clientMultyTest.log

### Функции
- Простая авторизация пользователя
- Отправка сообщений
- Закрытие клиента

### Взаимодействие клиент - сервер
* #### Запрос на авторизацию клиента (Клиент)
{"senderName":"UserName","msgType":"connected"}
* #### Ответ на запрос авторизацию клиента (Сервер)
{"msgType":"srvMsg","message":"in processing","msgStatus":"NO error"}
* #### Сообщение (Клиент)
{"senderName":"Denis","msgType":"simpleMsg","message":"Hi users!"}
* #### Ответ после обработки сообщения на сервере (Сервер)
{"msgType":"srvMsg","message":"in processing","msgStatus":"NO error"}
* #### Запрос на отключение клиента (Клиент)
{"senderName":"Denis","msgType":"disconnect"}
* #### Ответ на запрос на отключения клиента (Клиент)
{"senderName":"Denis","msgType":"disconnect","message":"User disconnected from chat.","msgStatus":"NO error"}

# Верхнеуровневая схема приложения в папке проекта: 
* ### chatSheme.vsdx
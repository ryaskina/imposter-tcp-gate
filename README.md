# imposter-tcp-gate
Настраиваемая заглушка для tcp-серверов.


Заглушка работает следующим образом: поднимается spring boot сервис, который принимает на вход rest запросы по настроенным контекстам.
В зависимости от того, на какой контекст отправляется запрос, такое действие и совершается. В фалйе /src/main/java/com.example.impostergate/controller
расположены классы, в которых настроена логика по контекстам.
```text
/addmockresponses
По данному контексту ожидаются ответы, которые будет выдавать заглушка. Логика простая, в заглушку приходит запрос, 
по названию заглушка понимает о каком запросе идёт речь и ищет подходящий Ans. Пример: приходит ReqMyInfo, 'Req' откусывается, 
вместо него подставляется 'Ans', т.о. ищется ответ 'AnsMyInfo' в папке templates.
```
```text
/startsocket
По данному контексту поднимается TCP сокет, который и будет принимать запросы и возвращать ответы.
```
```text
/stopsocket
По данному контексту можно опустить TCP сокет.
```

### Как запустить.
1. Preferences. [Скрин с настройкой](https://git.ftc.ru/rjaskina/imposter-gate/-/blob/master/image/Preferences.png)
2. File -> Project Structure. [Скрин с настройкой](https://git.ftc.ru/rjaskina/imposter-gate/-/blob/master/image/ProjectStructure.png)
3. Открыть файл /src/java/com.example.impostergate/ImposterGateApplication. Запустить метод main().
   ```text
   Либо через терминал вызвать:
   gradlew run
   ```
4. В логах запуска можно увидеть строчку вида, приложение запускается на порту 9001.
    ```text
   Tomcat started on port(s): 9000 (http) with context path ''
   ... 
    Tomcat started on port(s): 9001 (http) with context path ''
    ```
   Но надо иметь ввиду, что для настройки надо отпарвлять запросы на порт 9000.
5. Пока не остановить приложение, будет крутиться на этом порту.
6. Порт можно перенастроить в файле /src/resources/application.properties.

### Немного о логике заглушки.
1. В файле /test/java/com.example.impostergate/ImposterGateApplicationTests в тесте contextLoads() можно посмотреть
   пример создания заглушечных ответов для запросов в адаптер sis_socket. После выполнения теста в папке /src/resources/templates
   формируется список ответов, которыми должна отвечать заглушка.
2. Метод startSisTcpTest() стартует сервис для заглушки адаптера sis_socket.
3. Метод startCiaTcpTest() стартует сервис для заглушки адаптера с протоколом cia_socket.
4. Метод startAllTcpTest() стартует обе заглушки.
   ```text
   Либо через терминал вызвать:
   gradlew mocksRunning
   ```
5. Порты для поднятия заглушечных сервисов настраиваются в файле /src/main/java/com.example.impostergate/constants/SocketProperties.
6. По сути, старт заглушек - отправка json запроса по контексту. Чтобы остановить эти заглушки, необходимо выполнить метод, который отправит запрос для остановки заглушек.
   Таким методом является метод stopTcpTest().
7. Чтобы проверить работу заглушек достаточно отправить запрос, для которого есть настроенный ответ, например, ReqMyInfo отправить на localhost:4006.
   Для проверки, что всё гуд можно отправить запрос из jmeter или воспользоваться тестами из класса SendTcpRequestTest.
   ```text
   Либо через терминал вызвать настройку ответов:
   gradlew mocksSetting
   
   Затем вызвать запуск тестов:
   gradlew runMocksTests
   ```
8. В окне запуска ImposterGateApplication во вкладке Run можно будет посмотреть, в каком виде приходят запросы, и небольшой лог возврата ответов.

   Пример логов:
    ```text
    Start mock in Thread CIA
    QuickPay mock started on port 4006.
    Start mock in Thread SIS
    BROKER mock started on port 4005.
    The client sent request.
    Request name := ReqMyInfo
    Request body from client: <CIA><InfoService><ReqMyInfo>THIS IS REQUEST</ReqMyInfo></InfoService></CIA>
    The client got a response.
    All mocks are stopped.
    ```

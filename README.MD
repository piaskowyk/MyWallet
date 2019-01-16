# MyWallet

Projekt składa się z dwóch części, części serwerowej i aplikacji desktopowej.

Część serwerowa nasłuchuje na http://localhost:8080
Po wykryciu połączenia, uruchamia jest funkcja run, która na postawie danych zawartych w adresie url wywołuje
odpowiednią metodę w kontrolerzy wykorzystując mechanizm refleksji.
pattern urla: /api/[nazwa kontrolera]/[akcja w kontrolerze]
Dane przesyłane są w formacie json.
W nagłówku requestu znajduje się token służący do autoryzacji użytkownika, token generowany jest
podczas procesu logowania . W na początku metody w kontrolerze tworzę obiekt, który będzie zawierać dane zwracane użytkownikowi,
potem próbuję rzutować dane przysłane przez użytkownika w formacie json na oczekiwany obiekt który ma być reprezentacją danych wejściowych dla danej metody.
Następnie przeprowadzam autoryzację użytkownika.  jeśli operacja się powiedzie. Wykonuję dalsze akcja specyficzne dla danej metody, uzupełniając przy tym danymi obiekt  mający być odpowiedzią dla klienta.
Następnie rzutuję obiekt z danymi do formatu json i zwracam go klientowi
Obiekty służące do dialogu (user <-> serwer) znajdują się w pakiecie commonElements




Pliki graifczne pochodzą z: https://www.iconfinder.com
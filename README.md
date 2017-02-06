# BookLibrary

Projekt aplikacji mobilnej na platformę Android.
Aplikacja służyć będzie do zarządzania osobistą biblioteką książek. 
Przewidywane funkcjonalności aplikacji to:
- dodawanie książek na podstawie zeskanowanego kodu ISBN
- manualne dodawanie książek na podstawie tytułu
- proponowanie okładki dla dodawanej ksiązki
- możliwość dodania okładki ze zdjęcia
- lista książek oferująca filtrowanie
- możliwość zapisania oceny i notatki na temat książki
- tagowanie (kategoryzacja) książek


Rozwój:
- rozwinięcie użycia API Google (wyszukiwanie po innych elementach niz ISBN, z wyświetleniem listy znalezionych rozwiązań)
- wyszukiwanie okladek
- integracja z google books (https://developer.android.com/training/id-auth/authenticate.html, https://developers.google.com/books/docs/v1/reference/bookshelves)
- sortowanie książek
- autocomplete przy dodawaniu ksiazki (na podstawie dodanych autorów/wydawnictw)
- prezentacja okladek z googla (?) jakis sposob wyboru okladki przy dodawaniu ksiazki
- alternatywne api przy blednym wyniku wyszukiwania (?) googlowe na 19 ksiazek znalazlo z 10 (zwlaszcza problemy z "innymi wydaniami"
- poprawki ui (niewidoczny guzik menu na itemie w prawym dolnym rogu)

# 🖼️ JavaFX Image Processor

Aplikacja graficzna w języku **Java** z użyciem **JavaFX**, umożliwiająca podstawowe operacje przetwarzania obrazów takie jak konwersja do skali szarości, zmiana rozmiaru i obrót.

---

## 📌 Informacje

- **Autor:** *Patryk Piwnicki*
- **Prowadzący:** mgr inż. Michał Jaroszczuk
- **Grupa:** [SR][17:05]
- **Data:** 28 maja 2025

---

## 🔧 Technologie

- Java 24 (OpenJDK 24.0.1)
- JavaFX SDK 24.0.1
- IntelliJ IDEA

---

## ⚙️ Opis Działania

Aplikacja umożliwia:
- Wczytywanie obrazów w formacie **.jpg**
- Wybór operacji z listy (ComboBox):
  - `Convert to Grayscale` – konwersja do skali szarości
  - `Resize` – zmiana rozmiaru (50%)
  - `Rotate` – obrót o 90°
- Wyświetlanie oryginalnego i przetworzonego obrazu
- Zapisywanie przetworzonego obrazu do pliku `.jpg`
- Walidację formatów pliku oraz wyboru operacji
- Obsługę błędów i komunikatów (Alerty)

---

## 🌲 Drzewo projektu

![image](https://github.com/user-attachments/assets/211ec1aa-6d48-47fa-88b2-252420ae3f10)

---

## 🔍 Kluczowe klasy

- **Main** – klasa startowa aplikacji, ładuje plik FXML i uruchamia GUI.
- **MainController** – kontroler powiązany z widokiem main_view.fxml, odpowiada za:
  - wczytywanie obrazu (handleLoadImage)
  - przetwarzanie obrazu (handleExecute)
  - zapisywanie obrazu (handleSaveImage)
  - zarządzanie komunikatami (metody showError, showWarning, showSuccess)
- **main_view.fxml** – opis widoku aplikacji w XML, zawiera:
  - logo, rozwijaną listę operacji (ComboBox)
  - przyciski do wczytywania, przetwarzania i zapisywania obrazów
  - pola ImageView dla oryginalnego i przetworzonego obraz

---

## 🧪 Obsługa błędów

Aplikacja zawiera walidację dla:
- Braku wczytanego obrazu
- Nieobsługiwanego formatu pliku
- Braku wybranej operacji
- Przetwarzania pliku (np. błąd odczytu/zapisu)

---

## 🖼️ Widok aplikacji

![image](https://github.com/user-attachments/assets/225c949d-359f-4eec-a2da-f1a20e95ca5e)

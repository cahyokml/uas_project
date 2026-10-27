
# Aplikasi Pengelolaan Acara Musik — Android

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![Retrofit](https://img.shields.io/badge/API-Retrofit-48B983)

## Tentang Proyek

Aplikasi Pengelolaan Acara Musik merupakan proyek akademik untuk mata kuliah Perangkat Bergerak. Aplikasi Android ini dikembangkan menggunakan Kotlin dan Jetpack Compose untuk membantu pengguna mengelola informasi acara musik melalui integrasi REST API.

Aplikasi menyediakan antarmuka untuk melihat daftar acara, melihat detail acara, serta melakukan penambahan, pengubahan, dan penghapusan data acara melalui layanan API.

## Fitur Utama

- **Login:** Menyediakan halaman autentikasi pengguna.
- **Daftar Acara:** Menampilkan daftar acara musik yang diperoleh dari REST API.
- **Detail Acara:** Menampilkan informasi detail dari acara yang dipilih.
- **Tambah Acara:** Menyediakan formulir untuk menambahkan data acara.
- **Edit Acara:** Memungkinkan pengguna mengubah informasi acara.
- **Hapus Acara:** Mengirim permintaan penghapusan data acara melalui API.
- **Navigasi Aplikasi:** Menggunakan Navigation Compose untuk berpindah antarhalaman.

## Teknologi yang Digunakan

- **Kotlin:** Bahasa pemrograman untuk pengembangan aplikasi Android.
- **Jetpack Compose:** Toolkit untuk membangun antarmuka pengguna Android.
- **Material 3:** Komponen antarmuka dan sistem desain.
- **Retrofit:** Library untuk komunikasi dengan REST API.
- **Gson:** Untuk proses konversi data JSON.
- **Kotlin Coroutines:** Mendukung operasi asinkron.
- **ViewModel:** Mengelola state dan logika aplikasi.
- **DataStore Preferences:** Mendukung penyimpanan preferensi atau status sesi.
- **Gradle:** Sistem build dan pengelolaan dependensi proyek.

## Integrasi REST API

Aplikasi berkomunikasi dengan layanan REST API untuk mengelola data acara. Implementasi API mencakup operasi berikut:

| Operasi | Metode HTTP | Fungsi |
|---|---|---|
| Mendapatkan daftar acara | GET | Mengambil data acara |
| Mendapatkan detail acara | GET | Mengambil acara berdasarkan ID |
| Menambahkan acara | POST | Mengirim data acara baru |
| Memperbarui acara | PUT | Mengubah data acara |
| Menghapus acara | DELETE | Menghapus acara berdasarkan ID |

Data acara mencakup informasi seperti judul, tanggal, waktu, lokasi, deskripsi, kapasitas, dan status.

## Struktur Proyek

```text
uas_project/
└── app/
    └── src/
        └── main/
            ├── java/com/example/uas_project/
            │   ├── api/
            │   ├── data/
            │   ├── repository/
            │   ├── ui/
            │   │   ├── navigation/
            │   │   ├── screens/
            │   │   └── theme/
            │   ├── viewmodel/
            │   └── MainActivity.kt
            └── AndroidManifest.xml
```

## Tujuan Pembelajaran

Proyek ini menjadi sarana untuk mempelajari dan menerapkan:

- Pengembangan aplikasi Android menggunakan Kotlin.
- Pembuatan antarmuka menggunakan Jetpack Compose.
- Pengelolaan navigasi dan state aplikasi.
- Integrasi aplikasi Android dengan REST API.
- Pengambilan dan pengiriman data menggunakan JSON.
- Pengelolaan data acara melalui operasi CRUD.

## Pengembang

Proyek akademik untuk mata kuliah Perangkat Bergerak.

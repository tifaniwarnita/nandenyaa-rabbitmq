# NandeNyaa
Membangun aplikasi messaging sederhana dengan menggunakan RabbitMQ server. Buatlah program instant messaging (serupa whatsapp), dengan spesifikasi berikut:

1. Server instant messaging, yang dapat memiliki fungsional:
  1. Menerima registrasi user baru (login name, password).
  2. Menerima penambahan friend untuk sebuah user. Jika user A menambahkan user B sebagai friend, maka otomatis user B mendapat pemberitahuan bahwa A telah menjadi friend B (tidak perlu approval dari B untuk penambahan friend oleh A).
  3. Menerima pembuatan grup baru, dimana user yang membuat grup akan menjadi admin grup tersebut. Pembuatan grup dapat langsung menyertakan anggota grup.
  4. Menambahkan anggota grup baru ke dalam sebuah grup.
  5. Mengeluarkan user dari grup.
  
2. Aplikasi instant messaging client, dengan fungsional
  1. Registrasi user baru.
  2. Login ke sistem (login name, password), setelah login, aplikasi dapat menampilkan notifikasi jika ada pesan baru (baik yang dikirim langsung maupun ke grup).
  3. Mengirimkan pesan ke user lain dan ke grup.
  4. Menambahkan friend.
  5. Membuat grup baru.
  6. Keluar dari grup.

Rancanglah struktur exchange dan queue yang diperlukan pada RabbitMQ untuk implementasi aplikasidi atas. Aplikasi client boleh diimplementasikan sebagai CLI (command line interface) atau GUI (graphical user interface). Aplikasi boleh dikembangkan dengan menggunakan platform/bahasa apa pun (e.g. Java, Python, Erlang, Ruby, PHP, Visual Basic etc).

Waktu pengerjaan: 9 hari.

Tugas dilakukan per-kelompok (2 orang max), dan dikumpulkan Kamis 3 November 2016.

Deliverables:

Sebuah direktori (terkompres) yang berisi:
* README: berisi penjelasan singkat tentang desain aplikasi, petunjuk instalasi/building dan cara menjalankan program, dan daftar tes yang telah dilakukan, serta langkah- langkah melakukan tes
* Direktori berisi file resource (konfigurasi, script) yang digunakan untuk menguji program
* Source code

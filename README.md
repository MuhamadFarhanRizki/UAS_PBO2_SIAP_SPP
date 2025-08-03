# Final Proyek Pemrograman Berorientasi Obyek 2  

**Mata Kuliah:** Pemrograman Berorientasi Obyek 2  
**Dosen Pengampu:** Muhammad Ikhwan Fathulloh

## Kelompok  

- Kelompok: {2} 

- Proyek: {Aplikasi Pembayaran SPP}

- Anggota:  
  - Anggota 1: Aldi Rizkiansyah 
  - Anggota 2: M. Farhan Rizki 
  - Anggota 3: Nur Anisa 


## Judul Studi Kasus  

Sistem Pembayaran SPP Berbasis Web 



## Penjelasan Studi Kasus  

Sistem ini dirancang untuk membantu petugas administrasi sekolah dalam mencatat dan mengelola pembayaran SPP siswa. Sistem mencakup fitur-fitur seperti manajemen data siswa, data SPP, transaksi pembayaran, serta otentikasi user.
Tujuan dari sistem ini adalah menyederhanakan proses pencatatan pembayaran, meminimalisir kesalahan pencatatan manual, serta menyediakan laporan yang rapi dan mudah diakses.


## Penjelasan 4 Pilar OOP dalam Studi Kasus  

### 1. Inheritance : SiswaController.java, AdminController.java, PembayaranController.java  
**Penjelasan:**  
- Inheritance memungkinkan satu class untuk mewarisi sifat dan method dari class lainnya.
- Misalnya, controller seperti SiswaController mewarisi struktur dan prinsip dari Spring MVC Controller, yang menangani request dan response HTTP.
- Semua controller menggunakan prinsip pewarisan tak langsung dari @RestController dan @RequestMapping bawaan framework.

### 2. Encapsulation : Siswa.java, User.java, Pembayaran.java
**Penjelasan:**  
- Enkapsulasi diterapkan dengan membuat properti dalam class sebagai private dan menyediakan `getter` & `setter` untuk mengakses dan memodifikasi data.
```java
  private Long id;
  private String nama;
  private String nisn;

  public String getNama() {
      return nama;
  }

  public void setNama(String nama) {
      this.nama = nama;
  }
```
- Ini melindungi data internal agar tidak bisa diakses langsung dari luar class, dan memastikan validasi bisa dilakukan melalui method yang tersedia.

### 3. Polymorphism : PembayaranController.java, SiswaRepository.java
**Penjelasan:**  
- Polimorfisme terlihat dari penggunaan method yang sama namun dapat memiliki perilaku berbeda.
- Contoh: Method `findById()` pada interface `JpaRepository` bisa digunakan di berbagai repository seperti `SiswaRepository`, `PembayaranRepository`, dll, untuk entitas yang berbeda.
```java
  public interface SiswaRepository extends JpaRepository<Siswa, Long> { }
  public interface PembayaranRepository extends JpaRepository<Pembayaran, Long> { }
```
- Meskipun method-nya sama (`findById`, `save`, `deleteById`), masing-masing menyesuaikan jenis objek yang diproses (Siswa, Pembayaran, dll).

### 4. Abstract : PembayaranRequest.java, Repository Interfaces
**Penjelasan:**  
- Abstraksi menyembunyikan detail implementasi dan hanya menampilkan fungsionalitas penting.  
- Pada `PembayaranRequest.java`, class ini hanya mendefinisikan data yang dibutuhkan untuk proses pembayaran tanpa tahu bagaimana pembayaran diproses di backend.
```java
  public class PembayaranRequest {
      private Long idSiswa;
      private Long idSpp;
      private Integer bulanBayar;
      private Integer jumlahBayar;
      // getter & setter
  }
```
- Interface `JpaRepository` juga contoh abstraksi: kita hanya menggunakan method seperti `save()`, `findAll()` tanpa perlu tahu bagaimana query SQL-nya bekerja.

## Struktur Tabel Aplikasi 
<img width="872" height="451" alt="image" src="https://github.com/user-attachments/assets/1507a701-6f5e-4479-af4c-52c5994f7fe9" />

## Tampilan Aplikasi  

- Tampilan Awal
  <img width="899" height="641" alt="image" src="https://github.com/user-attachments/assets/08404446-97ca-4c10-b91f-287554b3201f" />

- Tampilan Login
  <img width="893" height="640" alt="image" src="https://github.com/user-attachments/assets/87e8a06c-02ae-49b4-a934-9d5021fe5583" />

- Tampilan Admin-Dashboard
  <img width="893" height="637" alt="image" src="https://github.com/user-attachments/assets/9c2fd3bc-b8a7-4ebc-9ce8-5b2567137e0b" />

- Tampilan Admin-Data Siswa
  <img width="898" height="642" alt="image" src="https://github.com/user-attachments/assets/82cef3a9-4c66-4a13-86a6-2f4c367aaf82" />

- Tampilan Admin-Data SPP
  <img width="898" height="637" alt="image" src="https://github.com/user-attachments/assets/7198649b-61dc-494b-9ef9-c6288d999495" />

- Tampilan Admin-Data Bayar
  <img width="900" height="640" alt="image" src="https://github.com/user-attachments/assets/ae7fbaf5-9a36-4771-b390-288327d0ff58" />

- Tampilan Siswa-Dashboard
  <img width="899" height="639" alt="image" src="https://github.com/user-attachments/assets/ec376944-6c43-4b5a-bf21-f1410c804e90" />

- Tampilan Siswa-Bayar SPP
  <img width="898" height="639" alt="image" src="https://github.com/user-attachments/assets/718cbcce-aff9-402a-8f1c-c4346f4a1ea5" />

- Tampilan Siswa-History SPP
  <img width="901" height="639" alt="image" src="https://github.com/user-attachments/assets/4feec91a-a0b9-4692-bc60-21adc5ec6166" />

- Tampilan Siswa-Profil
  <img width="899" height="633" alt="image" src="https://github.com/user-attachments/assets/75096c7a-74c5-4cc7-b294-7afc27c2460d" />

## Demo Proyek  

- **Github:** https://github.com/MuhamadFarhanRizki/UAS_PBO2_SIAP_SPP 
- **Youtube:** https://youtu.be/lSSgJ-9rSnA












## Struktur Tabel Aplikasi  

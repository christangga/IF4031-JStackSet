#IF4031-JStackSet
Implementation of Replicated Stack and Replicated Set using JGroups

By:
- Christ Angga Saputra - 13512019
- Jeffrey Lingga Binangkit - 13512059

##Petunjuk instalasi/building
1. Ada 2 cara untuk instalasi program ini, yaitu:
  - Clone dari `https://github.com/christangga/IF4031-JStackSet`
  - Ekstrak source code

2. Selanjutnya `Import Project` dengan menggunakan Netbeans

3. Resolve dependencies dengan `Add JAR/Library`, kemudian masukkan semua file yang ada dalam folder `lib/`

##Cara menjalankan ReplStack
1. Jalankan `ReplStack.java` dengan klik kanan, pilih `Run File`

###Daftar tes yang telah dilakukan serta langkah2 melakukan tes

####Menambahkan elemen ke stack dan mengecek elemen teratas stack tidak kosong
  - Jalankan 2 client
  - Pada client 1, masukkan perintah `/push halo`
  - Pada client 1 akan muncul `halo has been pushed to stack`
  - Pada client 2 masukkan perintah `/top`
  - Pada client 2 akan muncul `Top of stack is halo`

####Mengecek elemen teratas stack kosong
  - Jalankan 1 client
  - Masukkan perintah `/top`
  - Akan muncul `Stack is empty`

####Mengambil elemen teratas stack tidak kosong
  - Jalankan 2 client
  - Pada client 1, masukkan perintah `/push halo`
  - Pada client 1 akan muncul `halo has been pushed to stack`
  - Pada client 2 masukkan perintah `/pop`
  - Pada client 2 akan muncul `halo has been popped from stack`

####Mengambil elemen teratas stack kosong
  - Jalankan 1 client
  - Masukkan perintah `/pop`
  - Akan muncul `Stack is empty`

####Kombinasi seluruh fungsionalitas
  - Jalankan 3 client
  - Pada client 1, masukkan perintah `/push hai`
  - Pada client 1 akan muncul `hai has been pushed to stack`
  - Pada client 2, masukkan perintah `/top`
  - Pada client 2 akan muncul `Top of stack is hai`
  - Pada client 3, masukkan perintah `/push apa kabar?`
  - Pada client 3 akan muncul `apa kabar? has been pushed to stack`
  - Pada client 2, masukkan perintah `/pop`
  - Pada client 2 akan muncul `apa kabar? has been popped from stack`
  - Pada client 1, masukkan perintah `/top`
  - Pada client 1 akan muncul `Top of stack is hai`
  - Pada client 2, masukkan perintah `/pop`
  - Pada client 2 akan muncul `hai has been popped from stack`
  - Pada client 1, masukkan perintah `/top`
  - Pada client 1 akan muncul `Stack is empty`

##Cara menjalankan ReplSet
1. Jalankan `ReplSet.java` dengan klik kanan, pilih `Run File`

###Daftar tes yang telah dilakukan serta langkah2 melakukan tes

####Menambahkan elemen baru ke set dan mengecek elemen tersebut dalam set
  - Jalankan 2 client
  - Pada client 1, masukkan perintah `/add halo`
  - Pada client 1 akan muncul `halo has been added to set`
  - Pada client 2 masukkan perintah `/contains halo`
  - Pada client 2 akan muncul `halo is found in set`

####Menambahkan elemen yang sudah ada ke dalam set
  - Jalankan 2 client
  - Pada client 1, masukkan perintah `/add halo`
  - Pada client 1 akan muncul `halo has been added to set`
  - Pada client 2 masukkan perintah `/add halo`
  - Pada client 2 akan muncul `halo already exists in set`

####Mengecek elemen yang tidak ada dalam set
  - Jalankan 2 client
  - Pada client 1, masukkan perintah `/add halo`
  - Pada client 1 akan muncul `halo has been added to set`
  - Pada client 2, masukkan perintah `/contains apa kabar?`
  - Pada client 2 akan muncul `apa kabar? is not found in set`

####Menghapus elemen yang ada dari set
  - Jalankan 2 client
  - Pada client 1, masukkan perintah `/add halo`
  - Pada client 1 akan muncul `halo has been added to set`
  - Pada client 2, masukkan perintah `/remove halo`
  - Pada client 2 akan muncul `halo has been removed from set`

####Menghapus elemen yang tidak ada dari set
  - Jalankan 2 client
  - Pada client 1, masukkan perintah `/add halo`
  - Pada client 1 akan muncul `halo has been added to set`
  - Pada client 2, masukkan perintah `/remove apa kabar?`
  - Pada client 2 akan muncul `apa kabar? is not found in set`

####Kombinasi seluruh fungsionalitas
  - Jalankan 3 client
  - Pada client 1, masukkan perintah `/add hai`
  - Pada client 1 akan muncul `hai has been added to set`
  - Pada client 2, masukkan perintah `/contains hai`
  - Pada client 2 akan muncul `hai is found in set`
  - Pada client 3, masukkan perintah `/add hai`
  - Pada client 3 akan muncul `hai already exists in set`
  - Pada client 2, masukkan perintah `/add apa kabar?`
  - Pada client 2 akan muncul `apa kabar? has been added to set`
  - Pada client 1, masukkan perintah `/remove apa kabar?`
  - Pada client 1 akan muncul `apa kabar? has been removed from set`
  - Pada client 2, masukkan perintah `/remove hai`
  - Pada client 2 akan muncul `hai has been removed from set`
  - Pada client 3, masukkan perintah `/remove hai`
  - Pada client 3 akan muncul `hai is not found in set`

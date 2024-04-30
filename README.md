Turizm Acente Projesi

Projemizde Katmanlı mimamri ve Sql veritabanı gibi teknolojiler kullanıldı
Projenin login kısmında çalışan ve admin için ayrı yönlendirmeler yapan bir giriş işlemi bulunmaktadır.
Giriş işleminin ardında kullanıcı yapmak istediği işlemi seçerek otel, rezervasyon, pansiyon ve kullanıcı ekleme gibi işlemler yapabilmektedir.
Kullanıcı ayrıca isterse hali hazırda veritabanındaki veriler üzerinden işlem yapabilir yahur mevcut verileri güncelleyip silebilir ya da yeni veriler eklyebilir.

---VERİTABANI---
Projemizin veritabanı için POSTGRES SQL programı kullanıldı.

---KATMANLI MİMARİ---
Projemizde katmanlı mimari kullanıldı
Projenin kaynak kodları Bussines, Core, Entity, Dao, View adında üç ayrı paket altında birbiriyle extent edilmiş classlar ile yazıldı.

---FORM TASARIMI---
Projenin kullanıcı tarafından hem kolay anlaşılır hemde kullanışlı bir tasarım diline sahip Kullanıcı dostu bir arayüze sahip olması en önemli faktör.
Bu noktada View paketi içerisinde ek ayrı ayrı paketler kullanılarak her sekme için ayrı bir form ve form click için ayrı Class kullanıldı.

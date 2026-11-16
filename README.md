# Pirate Chest Animation (Client-Side)

Minecraft **1.20.1** Fabric mod.

Her sandık / varil / shulker açtığında videodaki **Fortnite Pirate Shop Access** animasyonunu + orijinal sesi oynatır.

## Animasyon
- Başlangıç: küçük eğik altın kare → coin’e dönüşüyor + sarı halka
- Orta: "YOU FOUND PIRATE SHOP ACCESS" + banner’lar
- Bitiş: kısa süre durup fade out

Frame’ler videodan daha agresif chromakey + despill ile temizlendi (yeşil kalıntı yok).

## Kurulum
1. Fabric Loader + Fabric API (1.20.1)
2. `./gradlew build`
3. `build/libs/piratechest-1.0.0.jar` dosyasını mods klasörüne at

## GitHub’dan jar almak
Repo’ya atıp Actions ile build edebilirsin (önceki mesajdaki workflow dosyası çalışır).

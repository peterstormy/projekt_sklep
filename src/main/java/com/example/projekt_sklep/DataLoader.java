package com.example.projekt_sklep;

import com.example.projekt_sklep.model.Category;
import com.example.projekt_sklep.model.Product;
import com.example.projekt_sklep.model.ProductSpec;
import com.example.projekt_sklep.model.User;
import com.example.projekt_sklep.repository.CategoryRepository;
import com.example.projekt_sklep.repository.ProductRepository;
import com.example.projekt_sklep.repository.ProductSpecRepository;
import com.example.projekt_sklep.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductSpecRepository productSpecRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      CategoryRepository categoryRepository,
                      ProductRepository productRepository,
                      ProductSpecRepository productSpecRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productSpecRepository = productSpecRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        loadUsers();
        migrateCategories();
        loadProducts();
    }

    private void loadUsers() {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setImie("Jan");
        admin.setNazwisko("Kowalski");
        admin.setLogin("admin");
        admin.setHaslo(passwordEncoder.encode("admin"));
        admin.setWiek(25);
        admin.setRola("ADMIN");

        User user = new User();
        user.setImie("Adam");
        user.setNazwisko("Nowak");
        user.setLogin("user");
        user.setHaslo(passwordEncoder.encode("user"));
        user.setWiek(22);
        user.setRola("USER");

        User employee = new User();
        employee.setImie("Karol");
        employee.setNazwisko("Wisniewski");
        employee.setLogin("employee");
        employee.setHaslo(passwordEncoder.encode("employee"));
        employee.setWiek(30);
        employee.setRola("EMPLOYEE");

        userRepository.save(admin);
        userRepository.save(user);
        userRepository.save(employee);
    }

    private void migrateCategories() {
        Map<String, String[]> data = new LinkedHashMap<>();
        data.put("smartfony",    new String[]{"smartfony",     "Smartfony różnych producentów – Android i iOS.",                    "2024-01-10"});
        data.put("laptopy",      new String[]{"laptopy",       "Laptopy do pracy, nauki i gier.",                                   "2024-01-10"});
        data.put("monitory",     new String[]{"monitory",      "Monitory do komputerów stacjonarnych i laptopów.",                  "2024-01-10"});
        data.put("telewizory",   new String[]{"telewizory",    "Telewizory różnych rozmiarów i technologii.",                       "2024-01-10"});
        data.put("plyty glowne", new String[]{"plyty glowne",  "Płyty główne do komputerów stacjonarnych.",                        "2024-01-10"});
        data.put("sluchawki",    new String[]{"sluchawki",     "Słuchawki nauszne i douszne dla graczy i audiofili.",              "2024-01-10"});
        data.put("tablety",      new String[]{"tablety",       "Tablety z systemami Android i iPadOS.",                            "2024-01-10"});

        List<Category> all = categoryRepository.findAll();
        for (Category c : all) {
            String key = c.getNazwa().toLowerCase();
            String[] row = data.get(key);
            if (row == null) continue;
            c.setNazwa(row[0]);
            if (c.getOpis() == null) c.setOpis(row[1]);
            if (c.getDataDodania() == null) c.setDataDodania(LocalDate.parse(row[2]));
            c.setAktywna(true);
            categoryRepository.save(c);
        }
    }

    private void loadProducts() {
        if (categoryRepository.count() > 0) return;

        Category smartfony = saveCategory("smartfony",
                "Smartfony różnych producentów – Android i iOS.", LocalDate.of(2024, 1, 10));
        Category laptopy = saveCategory("laptopy",
                "Laptopy do pracy, nauki i gier.", LocalDate.of(2024, 1, 10));
        Category monitory = saveCategory("monitory",
                "Monitory do komputerów stacjonarnych i laptopów.", LocalDate.of(2024, 1, 10));
        Category telewizory = saveCategory("telewizory",
                "Telewizory różnych rozmiarów i technologii.", LocalDate.of(2024, 1, 10));
        Category plytyGlowne = saveCategory("plyty glowne",
                "Płyty główne do komputerów stacjonarnych.", LocalDate.of(2024, 1, 10));
        Category sluchawki = saveCategory("sluchawki",
                "Słuchawki nauszne i douszne dla graczy i audiofili.", LocalDate.of(2024, 1, 10));
        Category tablety = saveCategory("tablety",
                "Tablety z systemami Android i iPadOS.", LocalDate.of(2024, 1, 10));

        // === SMARTFONY ===

        Product iphone17 = saveProduct("Apple iPhone 17 256GB Czarny",
                "Smartfon Apple iPhone 17 256GB, kolor czarny",
                "Apple", 3777.00, 15, smartfony);

        Product xiaomi17t = saveProduct("Xiaomi 17T Pro 5G 12/512GB Black 7000mAh 100W",
                "Smartfon Xiaomi 17T Pro 5G, 12GB RAM, 512GB pamieci, bateria 7000mAh, ladowanie 100W",
                "Xiaomi", 3999.00, 10, smartfony);

        Product nothingPhone = saveProduct("Nothing Phone (4a) 12/256GB Black",
                "Smartfon Nothing Phone 4a, 12GB RAM, 256GB pamieci, kolor czarny",
                "Nothing", 1899.00, 20, smartfony);

        Product motorolaG15 = saveProduct("Motorola moto g15 power 8/256GB Gravity Grey",
                "Smartfon Motorola moto g15 power, 8GB RAM, 256GB pamieci, kolor szary",
                "Motorola", 549.00, 30, smartfony);

        Product vivoY31 = saveProduct("vivo Y31 5G 6/256GB Czarny",
                "Smartfon vivo Y31 5G, 6GB RAM, 256GB pamieci, kolor czarny",
                "vivo", 899.00, 25, smartfony);

        // === LAPTOPY ===

        Product macbookAir = saveProduct("Apple MacBook Air M4/16GB/256/Mac OS Blekitny 8R GPU",
                "Laptop Apple MacBook Air z procesorem M4, 16GB RAM, 256GB SSD, kolor blekitny",
                "Apple", 3899.00, 8, laptopy);

        Product alienware16 = saveProduct("Dell Alienware 16 Aurora Core 5-210H/16GB/512+512/W11 RTX4050",
                "Laptop gamingowy Dell Alienware 16, Intel Core i5-210H, 16GB RAM, 1TB SSD, RTX4050",
                "Dell", 3499.00, 5, laptopy);

        Product hpVictus = saveProduct("HP VICTUS 15 i5-13420H/16GB/512/Win11 RTX3050",
                "Laptop gamingowy HP Victus 15, Intel Core i5-13420H, 16GB RAM, 512GB SSD, RTX3050",
                "HP", 2899.00, 12, laptopy);

        Product hpOmnibook = saveProduct("HP OmniBook 5 Flip x360 Core 7-150U/16GB/1TB/Win11 Touch OLED",
                "Laptop 2w1 HP OmniBook 5 Flip, Intel Core 7-150U, 16GB RAM, 1TB SSD, ekran OLED dotykowy",
                "HP", 3299.00, 7, laptopy);

        // === MONITORY ===

        Product msiMag272 = saveProduct("MSI MAG 272QP QD-OLED X24",
                "Monitor 27 cali MSI MAG 272QP, panel QD-OLED, rozdzielczosc QHD",
                "MSI", 1619.00, 18, monitory);

        Product samsungOdyssey = saveProduct("Samsung Odyssey G6 S27HG612SUX G61SH",
                "Monitor 27 cali Samsung Odyssey G6, panel zakrzywiony, wysoka czestotliwosc odswiezania",
                "Samsung", 1299.00, 22, monitory);

        Product gigabyteG25 = saveProduct("Gigabyte G25F2A",
                "Monitor 24 cali Gigabyte G25F2A, panel IPS, wysoka czestotliwosc odswiezania",
                "Gigabyte", 429.00, 35, monitory);

        // === TELEWIZORY ===

        Product tcl55 = saveProduct("TCL 55P7K 55 4K QLED Google TV Dolby Vision Dolby Atmos",
                "Telewizor 55 cali TCL 55P7K, panel QLED 4K, Google TV, Dolby Vision i Atmos",
                "TCL", 1749.00, 10, telewizory);

        Product hisense55 = saveProduct("Hisense 55E7Q Pro 55 QLED 4K 144Hz VRR VIDAA TV",
                "Telewizor 55 cali Hisense, panel QLED 4K, odswiezanie 144Hz, VRR, Dolby Vision",
                "Hisense", 2199.00, 8, telewizory);

        Product tcl75 = saveProduct("TCL 75P81K 75 QLED 144Hz 4K Google TV Dolby Atmos Vision",
                "Telewizor 75 cali TCL, panel QLED 4K, odswiezanie 144Hz, Google TV",
                "TCL", 3499.00, 5, telewizory);

        // === PLYTY GLOWNE ===

        Product asusB650 = saveProduct("ASUS B650E MAX GAMING WIFI Socket AM5",
                "Plyta glowna ASUS B650E, socket AM5, WiFi 6E, dla procesorow AMD Ryzen serii 7000",
                "ASUS", 549.00, 20, plytyGlowne);

        Product gigabyteB850 = saveProduct("Gigabyte B850 GAMING X WIFI6E Socket AM5",
                "Plyta glowna Gigabyte B850, socket AM5, WiFi 6E, dla procesorow AMD Ryzen serii 9000",
                "Gigabyte", 799.00, 15, plytyGlowne);

        // === SLUCHAWKI ===

        Product logitechProX = saveProduct("Logitech G PRO X Lightspeed",
                "Sluchawki bezprzewodowe Logitech G PRO X Lightspeed, dla graczy, lacznosc bezprzewodowa",
                "Logitech", 479.00, 25, sluchawki);

        // === TABLETY ===

        Product ipadAir = saveProduct("Apple iPad 11 11gen 128GB Wi-Fi Srebrny",
                "Tablet Apple iPad 11 cali, 11 generacja, 128GB pamieci, WiFi, kolor srebrny",
                "Apple", 1489.00, 12, tablety);

        Product lenovoTab = saveProduct("Lenovo Idea Tab 11 8GB/128GB/Android 15 WiFi PEN KB",
                "Tablet Lenovo 11 cali, 8GB RAM, 128GB pamieci, Android 15, WiFi, w zestawie rysik i klawiatura",
                "Lenovo", 949.00, 18, tablety);

        // === SPECYFIKACJE ===

        saveSpecs(iphone17, new LinkedHashMap<>() {{
            put("Procesor", "Apple A19 Bionic");
            put("Pamięć RAM", "8 GB");
            put("Pamięć wbudowana", "256 GB");
            put("Przekątna ekranu", "6,3\"");
            put("Rozdzielczość ekranu", "2622 x 1206 px");
            put("Typ ekranu", "OLED, Super Retina XDR");
            put("Częstotliwość odświeżania", "120 Hz");
            put("Aparat główny", "48 Mpix + 12 Mpix");
            put("Aparat przedni", "12 Mpix");
            put("Bateria", "3561 mAh");
            put("System", "iOS 18");
            put("Łączność", "5G, Wi-Fi 6E, NFC, Bluetooth 5.3");
            put("Wymiary", "150,9 x 71,5 x 7,8 mm");
            put("Waga", "170 g");
        }});

        saveSpecs(xiaomi17t, new LinkedHashMap<>() {{
            put("Procesor", "MediaTek Dimensity 9300+");
            put("Pamięć RAM", "12 GB");
            put("Pamięć wbudowana", "512 GB");
            put("Przekątna ekranu", "6,67\"");
            put("Rozdzielczość ekranu", "2712 x 1220 px");
            put("Typ ekranu", "AMOLED");
            put("Częstotliwość odświeżania", "144 Hz");
            put("Aparat główny", "200 Mpix + 50 Mpix + 12 Mpix");
            put("Aparat przedni", "32 Mpix");
            put("Bateria", "7000 mAh");
            put("Ładowanie", "100 W");
            put("System", "Android 15, HyperOS 2");
            put("Łączność", "5G, Wi-Fi 7, NFC, Bluetooth 5.4");
            put("Waga", "221 g");
        }});

        saveSpecs(nothingPhone, new LinkedHashMap<>() {{
            put("Procesor", "Qualcomm Snapdragon 7s Gen 3");
            put("Pamięć RAM", "12 GB");
            put("Pamięć wbudowana", "256 GB");
            put("Przekątna ekranu", "6,77\"");
            put("Rozdzielczość ekranu", "2392 x 1080 px");
            put("Typ ekranu", "AMOLED");
            put("Częstotliwość odświeżania", "120 Hz");
            put("Aparat główny", "50 Mpix + 50 Mpix");
            put("Aparat przedni", "50 Mpix");
            put("Bateria", "5000 mAh");
            put("Ładowanie", "45 W");
            put("System", "Android 15, Nothing OS 3.1");
            put("Łączność", "5G, Wi-Fi 6E, NFC, Bluetooth 5.4");
            put("Waga", "190 g");
        }});

        saveSpecs(motorolaG15, new LinkedHashMap<>() {{
            put("Procesor", "MediaTek Helio G85");
            put("Pamięć RAM", "8 GB");
            put("Pamięć wbudowana", "256 GB");
            put("Przekątna ekranu", "6,72\"");
            put("Rozdzielczość ekranu", "1600 x 720 px");
            put("Typ ekranu", "IPS LCD");
            put("Częstotliwość odświeżania", "90 Hz");
            put("Aparat główny", "50 Mpix + 8 Mpix + 2 Mpix");
            put("Aparat przedni", "8 Mpix");
            put("Bateria", "6000 mAh");
            put("Ładowanie", "18 W");
            put("System", "Android 15");
            put("Łączność", "4G LTE, Wi-Fi 5, Bluetooth 5.0");
            put("Waga", "207 g");
        }});

        saveSpecs(vivoY31, new LinkedHashMap<>() {{
            put("Procesor", "MediaTek Dimensity 6300");
            put("Pamięć RAM", "6 GB");
            put("Pamięć wbudowana", "256 GB");
            put("Przekątna ekranu", "6,67\"");
            put("Rozdzielczość ekranu", "1080 x 2400 px");
            put("Typ ekranu", "AMOLED");
            put("Częstotliwość odświeżania", "120 Hz");
            put("Aparat główny", "50 Mpix + 2 Mpix");
            put("Aparat przedni", "8 Mpix");
            put("Bateria", "5500 mAh");
            put("Ładowanie", "44 W");
            put("System", "Android 15, Funtouch OS 15");
            put("Łączność", "5G, Wi-Fi 6, NFC, Bluetooth 5.4");
            put("Waga", "185 g");
        }});

        saveSpecs(macbookAir, new LinkedHashMap<>() {{
            put("Procesor", "Apple M4 (8 rdzeni CPU)");
            put("GPU", "8-rdzeniowy GPU");
            put("Pamięć RAM", "16 GB (zunifikowana)");
            put("Pamięć wbudowana", "256 GB SSD");
            put("Przekątna ekranu", "13,6\"");
            put("Rozdzielczość ekranu", "2560 x 1664 px");
            put("Typ ekranu", "IPS Liquid Retina");
            put("System operacyjny", "macOS Sequoia");
            put("Czas pracy na baterii", "do 18 h");
            put("Kamera", "12 Mpix Center Stage");
            put("Łączność", "Wi-Fi 6E, Bluetooth 5.3, Thunderbolt 4");
            put("Wymiary", "304,1 x 215 x 11,3 mm");
            put("Waga", "1,24 kg");
        }});

        saveSpecs(alienware16, new LinkedHashMap<>() {{
            put("Procesor", "Intel Core 5-210H (14 rdzeni, do 4,7 GHz)");
            put("Karta graficzna", "NVIDIA GeForce RTX 4050 6 GB GDDR6");
            put("Pamięć RAM", "16 GB DDR5");
            put("Pamięć wbudowana", "512 GB + 512 GB SSD NVMe");
            put("Przekątna ekranu", "16\"");
            put("Rozdzielczość ekranu", "2560 x 1600 px");
            put("Typ ekranu", "IPS, 165 Hz");
            put("System operacyjny", "Windows 11 Home");
            put("Karta dźwiękowa", "Sound Blaster Pro Studio");
            put("Łączność", "Wi-Fi 6E, Bluetooth 5.3, Ethernet");
            put("Wymiary", "357 x 272 x 22,5 mm");
            put("Waga", "2,85 kg");
        }});

        saveSpecs(hpVictus, new LinkedHashMap<>() {{
            put("Procesor", "Intel Core i5-13420H (8 rdzeni, do 4,6 GHz)");
            put("Karta graficzna", "NVIDIA GeForce RTX 3050 6 GB GDDR6");
            put("Pamięć RAM", "16 GB DDR4");
            put("Pamięć wbudowana", "512 GB SSD NVMe");
            put("Przekątna ekranu", "15,6\"");
            put("Rozdzielczość ekranu", "1920 x 1080 px");
            put("Typ ekranu", "IPS, 144 Hz");
            put("System operacyjny", "Windows 11 Home");
            put("Łączność", "Wi-Fi 6, Bluetooth 5.3, Ethernet");
            put("Wymiary", "359,5 x 254 x 23,5 mm");
            put("Waga", "2,37 kg");
        }});

        saveSpecs(hpOmnibook, new LinkedHashMap<>() {{
            put("Procesor", "Intel Core 7-150U (12 rdzeni, do 5,4 GHz)");
            put("Pamięć RAM", "16 GB LPDDR5");
            put("Pamięć wbudowana", "1 TB SSD NVMe");
            put("Przekątna ekranu", "14\"");
            put("Rozdzielczość ekranu", "2880 x 1800 px");
            put("Typ ekranu", "OLED, dotykowy, 120 Hz");
            put("Tryb pracy", "2 w 1 (laptop/tablet)");
            put("System operacyjny", "Windows 11 Home");
            put("Czas pracy na baterii", "do 15 h");
            put("Łączność", "Wi-Fi 6E, Bluetooth 5.4, Thunderbolt 4");
            put("Wymiary", "314 x 223 x 16,9 mm");
            put("Waga", "1,55 kg");
        }});

        saveSpecs(msiMag272, new LinkedHashMap<>() {{
            put("Przekątna", "27\"");
            put("Typ matrycy", "QD-OLED");
            put("Rozdzielczość", "2560 x 1440 px (QHD)");
            put("Częstotliwość odświeżania", "240 Hz");
            put("Czas reakcji", "0,03 ms (GtG)");
            put("Jasność", "600 cd/m² (szczytowa 1000 cd/m²)");
            put("Kontrast", "1 000 000:1");
            put("HDR", "DisplayHDR True Black 400");
            put("Pokrycie przestrzeni barw", "99% DCI-P3");
            put("Złącza", "2x HDMI 2.1, 1x DisplayPort 1.4, 4x USB-A");
            put("Regulacja", "Wysokość, pochylenie, obrót");
            put("Wymiary bez podstawy", "613 x 364 x 47 mm");
        }});

        saveSpecs(samsungOdyssey, new LinkedHashMap<>() {{
            put("Przekątna", "27\"");
            put("Typ matrycy", "VA, zakrzywiona (1000R)");
            put("Rozdzielczość", "2560 x 1440 px (QHD)");
            put("Częstotliwość odświeżania", "240 Hz");
            put("Czas reakcji", "1 ms (GtG)");
            put("Jasność", "400 cd/m²");
            put("Kontrast", "2500:1");
            put("HDR", "DisplayHDR 600");
            put("Technologie adaptacyjne", "AMD FreeSync Premium Pro, G-Sync Compatible");
            put("Złącza", "1x HDMI 2.1, 1x DisplayPort 1.4, 2x USB-A");
            put("Regulacja", "Wysokość, pochylenie, obrót");
        }});

        saveSpecs(gigabyteG25, new LinkedHashMap<>() {{
            put("Przekątna", "24,5\"");
            put("Typ matrycy", "IPS");
            put("Rozdzielczość", "1920 x 1080 px (FHD)");
            put("Częstotliwość odświeżania", "280 Hz");
            put("Czas reakcji", "1 ms (MPRT)");
            put("Jasność", "350 cd/m²");
            put("Kontrast", "1000:1");
            put("HDR", "HDR Ready");
            put("Technologie adaptacyjne", "AMD FreeSync Premium");
            put("Złącza", "2x HDMI 2.0, 1x DisplayPort 1.2");
            put("Regulacja", "Pochylenie");
        }});

        saveSpecs(tcl55, new LinkedHashMap<>() {{
            put("Przekątna", "55\"");
            put("Typ matrycy", "QLED");
            put("Rozdzielczość", "3840 x 2160 px (4K UHD)");
            put("Częstotliwość odświeżania", "60 Hz");
            put("HDR", "Dolby Vision, HDR10+, HLG");
            put("Dźwięk", "Dolby Atmos, 2x 12 W");
            put("System Smart TV", "Google TV");
            put("Procesor", "AiPQ Lite");
            put("Złącza", "3x HDMI 2.0, 2x USB, Wi-Fi, Bluetooth");
            put("Wymiary bez podstawy", "1227 x 710 x 79 mm");
            put("Waga bez podstawy", "13,6 kg");
        }});

        saveSpecs(hisense55, new LinkedHashMap<>() {{
            put("Przekątna", "55\"");
            put("Typ matrycy", "QLED");
            put("Rozdzielczość", "3840 x 2160 px (4K UHD)");
            put("Częstotliwość odświeżania", "144 Hz");
            put("HDR", "Dolby Vision, HDR10+, HLG");
            put("VRR", "AMD FreeSync Premium, ALLM");
            put("Dźwięk", "Dolby Atmos, 2x 15 W");
            put("System Smart TV", "VIDAA U7");
            put("Złącza", "3x HDMI 2.1, 2x USB, Wi-Fi 6, Bluetooth 5.0");
            put("Wymiary bez podstawy", "1230 x 714 x 85 mm");
            put("Waga bez podstawy", "14,2 kg");
        }});

        saveSpecs(tcl75, new LinkedHashMap<>() {{
            put("Przekątna", "75\"");
            put("Typ matrycy", "QLED");
            put("Rozdzielczość", "3840 x 2160 px (4K UHD)");
            put("Częstotliwość odświeżania", "144 Hz");
            put("HDR", "Dolby Vision, HDR10+, HLG");
            put("Dźwięk", "Dolby Atmos, 2x 15 W");
            put("System Smart TV", "Google TV");
            put("Procesor", "AiPQ Pro");
            put("Złącza", "4x HDMI 2.1, 2x USB, Wi-Fi 6, Bluetooth 5.0");
            put("Wymiary bez podstawy", "1676 x 966 x 88 mm");
            put("Waga bez podstawy", "28,4 kg");
        }});

        saveSpecs(asusB650, new LinkedHashMap<>() {{
            put("Chipset", "AMD B650E");
            put("Socket", "AM5 (LGA1718)");
            put("Obsługiwane procesory", "AMD Ryzen serii 7000/8000/9000");
            put("Pamięć RAM", "4x DDR5, do 192 GB, do 6400 MHz OC");
            put("Sloty PCIe", "1x PCIe 5.0 x16, 1x PCIe 4.0 x16, 1x PCIe 3.0 x1");
            put("Sloty M.2", "2x M.2 PCIe 5.0, 1x M.2 PCIe 4.0");
            put("Złącza SATA", "4x SATA 6 Gb/s");
            put("Sieć", "2.5 GbE LAN, Wi-Fi 6E, Bluetooth 5.3");
            put("Złącza tylne", "1x HDMI 2.1, 1x DP 1.4, 2x USB4 40Gbps, 4x USB 3.2 Gen2");
            put("Złącza audio", "7.1 HD Audio, S/PDIF");
            put("Format", "ATX");
        }});

        saveSpecs(gigabyteB850, new LinkedHashMap<>() {{
            put("Chipset", "AMD B850");
            put("Socket", "AM5 (LGA1718)");
            put("Obsługiwane procesory", "AMD Ryzen serii 9000/8000/7000");
            put("Pamięć RAM", "4x DDR5, do 256 GB, do 8200 MHz OC");
            put("Sloty PCIe", "1x PCIe 5.0 x16, 1x PCIe 4.0 x4");
            put("Sloty M.2", "3x M.2 PCIe 5.0, 1x M.2 PCIe 4.0");
            put("Złącza SATA", "4x SATA 6 Gb/s");
            put("Sieć", "2.5 GbE LAN, Wi-Fi 6E, Bluetooth 5.3");
            put("Złącza tylne", "1x HDMI 2.1, 1x DP 2.0, 2x USB4 40Gbps, 4x USB 3.2 Gen2");
            put("Złącza audio", "7.1 HD Audio, S/PDIF");
            put("Format", "ATX");
        }});

        saveSpecs(logitechProX, new LinkedHashMap<>() {{
            put("Typ", "Nauszne, zamknięte");
            put("Łączność", "Bezprzewodowa LIGHTSPEED 2.4 GHz");
            put("Zasięg", "do 25 m");
            put("Czas pracy na baterii", "do 50 h");
            put("Przetworniki", "50 mm, PRO-G Graphene");
            put("Pasmo przenoszenia", "20 Hz - 20 000 Hz");
            put("Mikrofon", "Odłączany, kardioidalny, -38 dBV/Pa");
            put("Impedancja", "35 Ω");
            put("Złącza", "USB-A (odbiornik LIGHTSPEED), USB-C (ładowanie)");
            put("Kompatybilność", "PC, PS4, PS5");
            put("Waga", "345 g");
        }});

        saveSpecs(ipadAir, new LinkedHashMap<>() {{
            put("Procesor", "Apple M3");
            put("Pamięć RAM", "8 GB");
            put("Pamięć wbudowana", "128 GB");
            put("Przekątna ekranu", "11\"");
            put("Rozdzielczość ekranu", "2388 x 1668 px");
            put("Typ ekranu", "IPS Liquid Retina, 60 Hz");
            put("Aparat główny", "12 Mpix");
            put("Aparat przedni", "12 Mpix (ultraszeroki)");
            put("Bateria", "do 10 h pracy");
            put("System", "iPadOS 18");
            put("Łączność", "Wi-Fi 6E, Bluetooth 5.3, USB-C");
            put("Wymiary", "248,6 x 179,5 x 6,1 mm");
            put("Waga", "477 g");
        }});

        saveSpecs(lenovoTab, new LinkedHashMap<>() {{
            put("Procesor", "MediaTek Helio G88");
            put("Pamięć RAM", "8 GB");
            put("Pamięć wbudowana", "128 GB");
            put("Przekątna ekranu", "11\"");
            put("Rozdzielczość ekranu", "1920 x 1200 px");
            put("Typ ekranu", "IPS, 90 Hz");
            put("Aparat główny", "13 Mpix");
            put("Aparat przedni", "8 Mpix");
            put("Bateria", "7700 mAh");
            put("System", "Android 15");
            put("Akcesoria w zestawie", "Rysik, klawiatura");
            put("Łączność", "Wi-Fi 6, Bluetooth 5.1, USB-C");
            put("Waga", "490 g (sam tablet)");
        }});
    }

    private Category saveCategory(String nazwa, String opis, LocalDate dataDodania) {
        Category c = new Category();
        c.setNazwa(nazwa);
        c.setOpis(opis);
        c.setDataDodania(dataDodania);
        c.setAktywna(true);
        return categoryRepository.save(c);
    }

    private Product saveProduct(String nazwa, String opis, String producent,
                                double cena, int ilosc, Category category) {
        Product p = new Product();
        p.setNazwa(nazwa);
        p.setOpis(opis);
        p.setProducent(producent);
        p.setCena(cena);
        p.setIlosc(ilosc);
        p.setDataDodania(LocalDate.now());
        p.setCategory(category);
        p.setPubliczny(false);
        return productRepository.save(p);
    }

    private void saveSpecs(Product product, Map<String, String> specs) {
        int order = 0;
        for (Map.Entry<String, String> entry : specs.entrySet()) {
            ProductSpec spec = new ProductSpec();
            spec.setKlucz(entry.getKey());
            spec.setWartosc(entry.getValue());
            spec.setKolejnosc(order++);
            spec.setProduct(product);
            productSpecRepository.save(spec);
        }
    }
}

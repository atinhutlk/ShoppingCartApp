CREATE DATABASE IF NOT EXISTS shopping_cart_localization
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE shopping_cart_localization;

CREATE TABLE IF NOT EXISTS cart_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_items INT NOT NULL,
    total_cost DOUBLE NOT NULL,
    language VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_record_id INT,
    item_number INT NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (cart_record_id) REFERENCES cart_records(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS localization_strings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `key` VARCHAR(100) NOT NULL,
    value VARCHAR(255) NOT NULL,
    language VARCHAR(10) NOT NULL
);

INSERT INTO localization_strings (`key`, value, language) VALUES
('title', 'Shopping Cart App', 'en_US'),
('prompt.items', 'Enter item number:', 'en_US'),
('prompt.price', 'Enter item price:', 'en_US'),
('prompt.qty', 'Enter quantity:', 'en_US'),
('button.calculate', 'Calculate', 'en_US'),
('item.total', 'Item total:', 'en_US'),
('total', 'Total cost:', 'en_US'),
('lang.english', 'English', 'en_US'),
('lang.finnish', 'Finnish', 'en_US'),
('lang.swedish', 'Swedish', 'en_US'),
('lang.japanese', 'Japanese', 'en_US'),
('lang.arabic', 'Arabic', 'en_US'),
('error.invalidInput', 'Invalid input', 'en_US'),

('title', 'Ostoskorisovellus', 'fi_FI'),
('prompt.items', 'Syötä tuotenumero:', 'fi_FI'),
('prompt.price', 'Syötä tuotteen hinta:', 'fi_FI'),
('prompt.qty', 'Syötä määrä:', 'fi_FI'),
('button.calculate', 'Laske', 'fi_FI'),
('item.total', 'Tuotteen summa:', 'fi_FI'),
('total', 'Kokonaishinta:', 'fi_FI'),
('lang.english', 'Englanti', 'fi_FI'),
('lang.finnish', 'Suomi', 'fi_FI'),
('lang.swedish', 'Ruotsi', 'fi_FI'),
('lang.japanese', 'Japani', 'fi_FI'),
('lang.arabic', 'Arabia', 'fi_FI'),
('error.invalidInput', 'Virheellinen syöte', 'fi_FI'),

('title', 'Kundvagnsapp', 'sv_SE'),
('prompt.items', 'Ange artikelnummer:', 'sv_SE'),
('prompt.price', 'Ange pris:', 'sv_SE'),
('prompt.qty', 'Ange antal:', 'sv_SE'),
('button.calculate', 'Beräkna', 'sv_SE'),
('item.total', 'Varans totalsumma:', 'sv_SE'),
('total', 'Total kostnad:', 'sv_SE'),
('lang.english', 'Engelska', 'sv_SE'),
('lang.finnish', 'Finska', 'sv_SE'),
('lang.swedish', 'Svenska', 'sv_SE'),
('lang.japanese', 'Japanska', 'sv_SE'),
('lang.arabic', 'Arabiska', 'sv_SE'),
('error.invalidInput', 'Ogiltig inmatning', 'sv_SE'),

('title', 'ショッピングカートアプリ', 'ja_JP'),
('prompt.items', '商品番号を入力してください:', 'ja_JP'),
('prompt.price', '価格を入力してください:', 'ja_JP'),
('prompt.qty', '数量を入力してください:', 'ja_JP'),
('button.calculate', '計算', 'ja_JP'),
('item.total', '商品合計:', 'ja_JP'),
('total', '合計金額:', 'ja_JP'),
('lang.english', '英語', 'ja_JP'),
('lang.finnish', 'フィンランド語', 'ja_JP'),
('lang.swedish', 'スウェーデン語', 'ja_JP'),
('lang.japanese', '日本語', 'ja_JP'),
('lang.arabic', 'アラビア語', 'ja_JP'),
('error.invalidInput', '入力が正しくありません', 'ja_JP'),

('title', 'تطبيق سلة التسوق', 'ar_SA'),
('prompt.items', 'أدخل رقم العنصر:', 'ar_SA'),
('prompt.price', 'أدخل السعر:', 'ar_SA'),
('prompt.qty', 'أدخل الكمية:', 'ar_SA'),
('button.calculate', 'احسب', 'ar_SA'),
('item.total', 'مجموع العنصر:', 'ar_SA'),
('total', 'التكلفة الإجمالية:', 'ar_SA'),
('lang.english', 'الإنجليزية', 'ar_SA'),
('lang.finnish', 'الفنلندية', 'ar_SA'),
('lang.swedish', 'السويدية', 'ar_SA'),
('lang.japanese', 'اليابانية', 'ar_SA'),
('lang.arabic', 'العربية', 'ar_SA'),
('error.invalidInput', 'إدخال غير صالح', 'ar_SA');


# ABOUT PROJECT
Мобільний додаток для замовлення фотосесії
## Котков:
### Робота з основними модулями:
- `PaymentScreen` - реалізація екрану для замовлення фотосесії: можливості обрати дату та час фотосесії, обрати доп. теги, опис замовлення, поля для контактних даних з замовником. При замовленні фотосесії, вона відразу йде у вкладку Scheduled(заплановані) у профілі.
- `SessionsActivity` - реалізація головної сторінки: можливості перегляду фотографій з минулих фотосесій, ознайомлення з резюме фотографа, подивитись сторінки фотографа у соц. мережах, дати власну оцінку фотографу.
- `ProfileScreen`: імплементація можливості перегляду списку замовлених та завершених сесій, зміна ім'я та опису профілю.
- `BottomNavMenu`: імплементація відображення екранів разом з панеллю та імплементація можливості переключення між екранами.
### Робота з допоміжними модулями:
- `SharedViewModel` - клас, який містить у собі власне загальний список замовлених фотосесій (застосовується для контакту між `PaymentScreen` та `ProfileScreen`)
-----------------------------------------------------------------------------------------------
## Дубина:
### Робота з основними модулями:
- `ProfileScreen` - реалізація профілю користувача-замовника.
- `BottomNavMenu` - реалізація навігаційної панелі: основа для переходу між екранами.
### Робота з допоміжними модулями:
- `MenuItem` -
- `ImageWithText` - 

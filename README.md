# JDBC-product-organization
<b><p>Общие требования</p></b>
Взаимодействие с СУБД осуществляется через механизм JDBC. БД, с которой работает проект описывает сущности Накладная, Товар, Организация. Для каждой накладной может существовать несколько позиций. В накладной указывается Организация, а в позиции указывается Товар.
<hr>
<p><b>Накладные</p></b>
<p>№ накладной | Дата накладной |Организация отправитель</p>
<p><b>Позиции накладной</p></b>
<p>Цена | Товар | Количество</p>
<p><b>Организации</p></b>
<p>Название | ИНН | Расчетный счет</p>
<p><b>Товары</p></b>
<p>Наименование | Внутренний код</p>

<h3>Графическое представление БД</h3>
<img src="https://user-images.githubusercontent.com/98654361/202176449-a664e647-f06d-4ee0-ab85-f26e7187026f.png">

<p><b>Необходимо:</p></b>
Написать скрипт создания базы для указанных отношений.
Создать менеджеры, обеспечивающие CRUD операции с сущностями
Написать запросы на построение отчетов:
Выбрать первые 10 поставщиков по количеству поставленного товара
Выбрать поставщиков с количеством поставленного товара выше указанного значения (товар и его количество должны допускать множественное указание).
За каждый день для каждого товара рассчитать количество и сумму полученного товара в указанном периоде, посчитать итоги за период
Рассчитать среднюю цену по каждому товару за период
Вывести список товаров, поставленных организациями за период. Если организация товары не поставляла, то она все равно должна быть отражена в списке.
Написать тесты, на все публичные методы DAO менеджеров и отчёты. 
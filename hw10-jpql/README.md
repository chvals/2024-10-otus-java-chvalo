## Использование Hibernate

# Цель:
На практике освоить основы Hibernate.
Понять как аннотации-hibernate влияют на формирование sql-запросов.


# Описание/Пошаговая инструкция выполнения домашнего задания:
Работа должна использовать базу данных в docker-контейнере .

За основу возьмите пример из вебинара про JPQL (class DbServiceDemo).
Добавьте в Client поля:
адрес (OneToOne)
class Address {
private String street;
}
и телефон (OneToMany)
class Phone {
private String number;
}

Разметьте классы таким образом, чтобы при сохранении/чтении объека Client каскадно сохранялись/читались вложенные объекты.

ВАЖНО.

Hibernate должен создать только три таблицы: для телефонов, адресов и клиентов.
При сохранении нового объекта не должно быть update-ов.
Посмотрите в логи и проверьте, что эти два требования выполняются.
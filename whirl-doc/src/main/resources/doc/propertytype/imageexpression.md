Sql-запрос с ссылками на изображения, хранящиеся внутри платформы whirl. Эти ссылки могут быть взяты внутри Editor`a при нажатии на иконку 'icons'

Пример запроса:
case when id=1 then 'webjars/famfamfam-silk/1.3/icons/medal_bronze_1.png'
when id=4 then 'webjars/famfamfam-silk/1.3/icons/bomb.png'
else 'webjars/famfamfam-silk/1.3/icons/book.png'
end

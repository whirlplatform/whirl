/**
 * @type row_value  Элемент идентифицирующий состояние строки компонентов содержащих списки значений
 */
CREATE TYPE row_value AS
(
    id varchar(32767),
    selected boolean,
    checked boolean,
    expanded boolean
);

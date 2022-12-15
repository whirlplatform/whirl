/**
 * @type row_value  ....
 */
CREATE TYPE row_value AS
(
    id varchar(32767),
    selected boolean,
    checked boolean,
    expanded boolean
);

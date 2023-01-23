[Содержание](index.md)

# **SET_NEXT_EVENT Function**
Устанавливает код следующего исполняемого события

### Parameters
| Name         | Description                                     |
|--------------|-------------------------------------------------|
| p_result     | Переменная результата выполнения события        |
| p_next_event | Событие - строковая переменная, без ограничений |
| *return*     | Переданную переменную с новым параметром        |

### Syntax
     CREATE OR REPLACE FUNCTION set_next_event(
    p_result function_result,
    p_next_event varchar)
    RETURNS function_result
    AS
    $$
    BEGIN
    p_result.next_event := p_next_event;
    RETURN p_result;
    END;

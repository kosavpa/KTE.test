package owl.home.KTE.test.service.util;

/**
 * Перечисление содержащие валюты (рубль и копейка). При запросе итоговой суммы и формировании чека итоговая сумма отображается в копейках
 */
public enum Carrensy {
    RUB("RUB"), KOP("KOP");

    String name;

    Carrensy(String name) {
        this.name = name;
    }
}

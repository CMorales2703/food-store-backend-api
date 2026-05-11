package com.foodstore.common.interfaces;

import java.math.BigDecimal;

/**
 * Interfaz implementada por Pedido para calcular el total a partir de sus detalles.
 */
public interface Calculable {
    BigDecimal calcularTotal();
}

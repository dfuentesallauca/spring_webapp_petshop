package com.petshop.spring_web_petshop.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.petshop.spring_web_petshop.dto.ItemCarrito;
import com.petshop.spring_web_petshop.dto.ProductoVendido;
import com.petshop.spring_web_petshop.model.DetallePedido;
import com.petshop.spring_web_petshop.model.Pedido;
import com.petshop.spring_web_petshop.repository.DetallePedidoRepository;
import com.petshop.spring_web_petshop.repository.PedidoRepository;
import com.petshop.spring_web_petshop.repository.ProductoRepository;
import com.petshop.spring_web_petshop.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Integer confirmar(Integer idUsuario, List<ItemCarrito> carrito) {
        var pedido = new Pedido();
        pedido.setUsuario(usuarioRepository.getReferenceById(idUsuario));
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(carrito.stream().map(ItemCarrito::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add));

        for (var item : carrito) {
            if (productoRepository.descontarStock(item.getIdProducto(), item.getCantidad()) == 0) {
                throw new IllegalStateException("Stock insuficiente para " + item.getNombre());
            }
            var detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(productoRepository.getReferenceById(item.getIdProducto()));
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());
            detalle.setSubtotal(item.getSubtotal());
            pedido.getDetalles().add(detalle);
        }

        return pedidoRepository.save(pedido).getIdPedido();
    }

    public List<Pedido> getByUsuario(Integer idUsuario) {
        return pedidoRepository.findByUsuarioIdUsuarioOrderByFechaDesc(idUsuario);
    }

    public List<Pedido> getByRango(LocalDate desde, LocalDate hasta) {
        return pedidoRepository.findByRango(desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay());
    }

    public List<ProductoVendido> getTopProductos() {
        return detallePedidoRepository.findTopProductos();
    }
}

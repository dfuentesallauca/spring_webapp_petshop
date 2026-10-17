package com.petshop.spring_web_petshop.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petshop.spring_web_petshop.dto.ItemCarrito;
import com.petshop.spring_web_petshop.dto.ProductoFilter;
import com.petshop.spring_web_petshop.service.CategoriaService;
import com.petshop.spring_web_petshop.service.PedidoService;
import com.petshop.spring_web_petshop.service.ProductoService;
import com.petshop.spring_web_petshop.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("tienda")
@RequiredArgsConstructor
public class TiendaController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final PedidoService pedidoService;

    @GetMapping("productos")
    public String productos(@ModelAttribute ProductoFilter filter, Model model) {
        model.addAttribute("lstProductos", productoService.searchActivos(filter));
        model.addAttribute("lstCategorias", categoriaService.getActivas());
        model.addAttribute("filter", filter);
        return "tienda/productos";
    }

    @GetMapping("carrito")
    public String carrito(HttpSession session, Model model) {
        var carrito = getCarrito(session);
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total(carrito));
        return "tienda/carrito";
    }

    @PostMapping("carrito/agregar")
    public String agregar(@RequestParam Integer idProducto, @RequestParam(defaultValue = "1") int cantidad,
            @RequestParam(required = false) Integer idCategoria, HttpSession session, RedirectAttributes flash) {

        var producto = productoService.getOne(idProducto);
        var carrito = getCarrito(session);

        if (producto != null && producto.getActivo() && producto.getStock() > 0) {
            var existente = carrito.stream().filter(i -> i.getIdProducto().equals(idProducto)).findFirst().orElse(null);
            var cantidadActual = existente != null ? existente.getCantidad() : 0;
            var nueva = Math.min(cantidadActual + Math.max(cantidad, 1), producto.getStock());
            if (existente != null) {
                existente.setCantidad(nueva);
            } else {
                carrito.add(new ItemCarrito(idProducto, producto.getNombre(), producto.getPrecio(), nueva));
            }
            flash.addFlashAttribute("alert", Alert.sweetToast("Producto agregado al carrito", "success", 3000));
        }
        return "redirect:/tienda/productos" + (idCategoria != null ? "?idCategoria=" + idCategoria : "");
    }

    @PostMapping("carrito/eliminar")
    public String eliminar(@RequestParam Integer idProducto, HttpSession session) {
        getCarrito(session).removeIf(i -> i.getIdProducto().equals(idProducto));
        return "redirect:/tienda/carrito";
    }

    @PostMapping("carrito/confirmar")
    public String confirmar(HttpSession session, RedirectAttributes flash) {
        var carrito = getCarrito(session);
        if (carrito.isEmpty()) {
            return "redirect:/tienda/carrito";
        }

        try {
            var idPedido = pedidoService.confirmar((Integer) session.getAttribute("idUsuario"), carrito);
            session.setAttribute("pedidoConfirmadoId", idPedido);
            session.setAttribute("pedidoItems", new ArrayList<>(carrito));
            session.setAttribute("pedidoTotal", total(carrito));
            session.removeAttribute("carrito");
            return "redirect:/tienda/pedido-confirmado";
        } catch (Exception e) {
            flash.addFlashAttribute("alert", Alert.sweetAlertError("No se pudo procesar el pedido: stock insuficiente"));
            return "redirect:/tienda/carrito";
        }
    }

    @GetMapping("pedido-confirmado")
    public String pedidoConfirmado(HttpSession session, Model model) {
        var idPedido = session.getAttribute("pedidoConfirmadoId");
        if (idPedido == null) {
            return "redirect:/tienda/productos";
        }
        model.addAttribute("idPedido", idPedido);
        model.addAttribute("items", session.getAttribute("pedidoItems"));
        model.addAttribute("total", session.getAttribute("pedidoTotal"));
        session.removeAttribute("pedidoConfirmadoId");
        session.removeAttribute("pedidoItems");
        session.removeAttribute("pedidoTotal");
        return "tienda/pedido-confirmado";
    }

    @GetMapping("pedidos")
    public String pedidos(HttpSession session, Model model) {
        model.addAttribute("lstPedidos", pedidoService.getByUsuario((Integer) session.getAttribute("idUsuario")));
        return "tienda/pedidos";
    }

    @SuppressWarnings("unchecked")
    private List<ItemCarrito> getCarrito(HttpSession session) {
        var carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    private BigDecimal total(List<ItemCarrito> carrito) {
        return carrito.stream().map(ItemCarrito::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

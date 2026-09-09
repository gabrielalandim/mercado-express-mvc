package br.com.fiap.mercadoexpress.controllers;
import br.com.fiap.mercadoexpress.models.Produto;
import br.com.fiap.mercadoexpress.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@Controller
@RequestMapping("/produtos")
public class ProdutoWebController {
    @Autowired
    private ProdutoService service;
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", service.listarTodos());
        return "lista";
    }
    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("produto", new Produto());
        return "form";
    }
    @PostMapping
    public String salvar(@ModelAttribute Produto produto) {
        service.salvar(produto);
        return "redirect:/produtos";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Produto> produto = service.buscarPorId(id);
        if (produto.isPresent()) {
            model.addAttribute("produto", produto.get());
            return "form";
        }
        return "redirect:/produtos";
    }
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/produtos";
    }
}

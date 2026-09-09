package br.com.fiap.mercadoexpress.services;

import br.com.fiap.mercadoexpress.models.Produto;
import br.com.fiap.mercadoexpress.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    private List<Produto> listaTemporaria = new ArrayList<>();

    public Produto salvar(Produto produto) {
        listaTemporaria.add(produto);

        Produto produtoParaCommit = listaTemporaria.get(listaTemporaria.size() - 1);

        Produto produtoSalvo = repository.save(produtoParaCommit);

        listaTemporaria.remove(produto);

        return produtoSalvo;
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}

package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOPedido;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOPedidoItem;
import com.Back_End_Food_Truck.System_Food_Truck.Model.*;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryPedido;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryProduto;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicePedido {

    @Autowired
    private RepositoryProduto repositoryProduto;

    @Autowired
    private RepositoryPedido repositoryPedido;

    @Autowired
    private RepositoryEndereco repositoryEndereco;

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    public Pedido criarPedido(DTOPedido dtoPedido) {

        Pedido pedido = new Pedido();
        pedido.setDataPedido(dtoPedido.getDataPedido());

        // ✅ Forma de pagamento segura
        FormaPagamento formaPagamento = FormaPagamento.valueOf(
                dtoPedido.getFormaPagamento().toUpperCase().replace(" ", "_")
        );
        pedido.setFormaPagamento(formaPagamento);

        pedido.setObservacao(dtoPedido.getObservacao());

        // ✅ 1. Buscar usuário por EMAIL (melhor prática)
        Usuario usuario = repositoryUsuario.findByEmail(dtoPedido.getEmailUsuario())
                .orElseGet(() -> {
                    Usuario novo = new Usuario();
                    novo.setNome(dtoPedido.getNomeUsuario());
                    novo.setTelefone(dtoPedido.getTelefoneUsuario());
                    novo.setEmail(dtoPedido.getEmailUsuario());
                    novo.setTipo(TipoUsuario.C);
                    novo.setAtivo(true);

                    // ✅ senha opcional para cliente
                    novo.setSenha(null);

                    return repositoryUsuario.save(novo);
                });

        pedido.setUsuario(usuario);

        // ✅ 2. Endereço SOMENTE no pedido (evita duplicação)
        if (dtoPedido.getEndereco() != null) {
            DTOEndereco dtoEnd = dtoPedido.getEndereco();

            Endereco endereco = new Endereco();
            endereco.setRua(dtoEnd.getRua());
            endereco.setCidade(dtoEnd.getCidade());
            endereco.setBairro(dtoEnd.getBairro());
            endereco.setCep(dtoEnd.getCep());
            endereco.setNumero(dtoEnd.getNumero());
            endereco.setComplemento(dtoEnd.getComplemento());
            endereco.setEstado(dtoEnd.getEstado()); // ✅ IMPORTANTE

            repositoryEndereco.save(endereco);

// salva no pedido
            pedido.setEndereco(endereco);

// 🔥 AQUI É A CORREÇÃO
            usuario.setEndereco(endereco);
            repositoryUsuario.save(usuario);
        }

        // ✅ 3. Itens + cálculo do total no backend
        double total = 0;

        if (dtoPedido.getItens() != null && !dtoPedido.getItens().isEmpty()) {

            List<PedidoItem> itens = dtoPedido.getItens().stream()
                    .map(itemDto -> {

                        Produto produto = repositoryProduto.findById(itemDto.getProdutoId())
                                .orElseThrow(() -> new RuntimeException(
                                        "Produto não encontrado: " + itemDto.getProdutoId()
                                ));

                        PedidoItem item = new PedidoItem();
                        item.setProduto(produto);
                        item.setQuantidade(itemDto.getQuantidade());
                        item.setPrecoUnitario(produto.getPreco()); // 🔥 valor real do banco
                        item.setPedido(pedido);

                        return item;
                    })
                    .collect(Collectors.toList());

            // calcula total
            total = itens.stream()
                    .mapToDouble(i -> i.getPrecoUnitario() * i.getQuantidade())
                    .sum();

            pedido.setItens(itens);
        }

        pedido.setPrecoTotal(total); // 🔥 backend controla

        return repositoryPedido.save(pedido);
    }

    // ✅ LISTAR pedidos
    public List<DTOPedido> listarPedidos() {
        return repositoryPedido.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // ✅ BUSCAR por ID
    public Optional<DTOPedido> buscarPorId(Long id) {
        return repositoryPedido.findById(id)
                .map(this::converterParaDTO);
    }

    // ✅ DELETAR
    public boolean deletarPedido(Long id) {
        if (repositoryPedido.existsById(id)) {
            repositoryPedido.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ CONVERTER PARA DTO (com ID agora)
    private DTOPedido converterParaDTO(Pedido pedido) {

        DTOEndereco enderecoDTO = null;

        if (pedido.getEndereco() != null) {
            enderecoDTO = new DTOEndereco(
                    pedido.getEndereco().getCep(),
                    pedido.getEndereco().getBairro(),
                    pedido.getEndereco().getCidade(),
                    pedido.getEndereco().getComplemento(),
                    pedido.getEndereco().getNumero(),
                    pedido.getEndereco().getRua(),
                    pedido.getEndereco().getEstado()
            );
        }

        List<DTOPedidoItem> itensDTO = pedido.getItens().stream()
                .map(item -> new DTOPedidoItem(
                        item.getId(),
                        item.getProduto(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                ))
                .collect(Collectors.toList());

        return new DTOPedido(
                pedido.getId(), // 🔥 ESSENCIAL
                pedido.getDataPedido(),
                pedido.getUsuario().getNome(),
                pedido.getUsuario().getTelefone(),
                pedido.getUsuario().getEmail(),
                enderecoDTO,
                pedido.getFormaPagamento().toString(),
                pedido.getObservacao(),
                pedido.getPrecoTotal(),
                itensDTO
        );
    }
}
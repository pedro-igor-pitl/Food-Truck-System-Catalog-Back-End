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
        pedido.setFormaPagamento(FormaPagamento.valueOf(dtoPedido.getFormaPagamento()));
        pedido.setObservacao(dtoPedido.getObservacao());
        pedido.setPrecoTotal(dtoPedido.getPrecoTotal());

        // 1. Buscar usuário pelo telefone
        Usuario usuario = repositoryUsuario.findByTelefone(dtoPedido.getTelefoneUsuario())
                .orElseGet(() -> {
                    Usuario novo = new Usuario();
                    novo.setNome(dtoPedido.getNomeUsuario());
                    novo.setTelefone(dtoPedido.getTelefoneUsuario());
                    novo.setEmail(dtoPedido.getEmailUsuario());
                    novo.setTipo(TipoUsuario.C); // Usuário tipo cliente
                    novo.setAtivo(true);
                    // Garantir senha não nula
                    novo.setSenha("*");

                    // Salvar usuário com endereço se houver
                    if (dtoPedido.getEndereco() != null) {
                        DTOEndereco dtoEnd = dtoPedido.getEndereco();
                        Endereco endereco = new Endereco();
                        endereco.setRua(dtoEnd.getRua());
                        endereco.setCidade(dtoEnd.getCidade());
                        endereco.setBairro(dtoEnd.getBairro());
                        endereco.setCep(dtoEnd.getCep());
                        endereco.setNumero(dtoEnd.getNumero());
                        endereco.setComplemento(dtoEnd.getComplemento());
                        repositoryEndereco.save(endereco);
                        novo.setEndereco(endereco);
                    }

                    return repositoryUsuario.save(novo);
                });

        pedido.setUsuario(usuario); // 🔑 Agora sempre terá usuário

        // 2. Endereço do pedido (se diferente do usuário)
        if (dtoPedido.getEndereco() != null) {
            Endereco enderecoPedido = new Endereco();
            DTOEndereco dtoEnd = dtoPedido.getEndereco();
            enderecoPedido.setRua(dtoEnd.getRua());
            enderecoPedido.setCidade(dtoEnd.getCidade());
            enderecoPedido.setCep(dtoEnd.getCep());
            enderecoPedido.setNumero(dtoEnd.getNumero());
            enderecoPedido.setBairro(dtoEnd.getBairro());
            enderecoPedido.setComplemento(dtoEnd.getComplemento());
            enderecoPedido.setEstado(dtoEnd.getEstado());
            pedido.setEndereco(enderecoPedido);
        }

        // 3. Itens
        if (dtoPedido.getItens() != null && !dtoPedido.getItens().isEmpty()) {
            List<PedidoItem> itens = dtoPedido.getItens().stream()
                    .map(itemDto -> {
                        Produto produto = repositoryProduto.findById(itemDto.getProdutoId())
                                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDto.getProdutoId()));

                        PedidoItem item = new PedidoItem();
                        item.setProduto(produto);
                        item.setQuantidade(itemDto.getQuantidade());
                        item.setPrecoUnitario(itemDto.getPrecoUnitario());
                        item.setPedido(pedido);
                        return item;
                    })
                    .collect(Collectors.toList());
            pedido.setItens(itens);
        }

        return repositoryPedido.save(pedido);
    }

    // LISTAR todos os pedidos
    public List<DTOPedido> listarPedidos() {
        return repositoryPedido.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // BUSCAR pedido por ID
    public Optional<DTOPedido> buscarPorId(Long id) {
        return repositoryPedido.findById(id)
                .map(this::converterParaDTO);
    }

    // DELETAR pedido
    public boolean deletarPedido(Long id) {
        if (repositoryPedido.existsById(id)) {
            repositoryPedido.deleteById(id);
            return true;
        }
        return false;
    }

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
                        item.getProduto(), // ← agora passa o Produto diretamente
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                ))
                .collect(Collectors.toList());


        return new DTOPedido(
                pedido.getId(),
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

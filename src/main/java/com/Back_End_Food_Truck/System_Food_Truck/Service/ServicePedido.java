package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOPedido;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOPedidoItem;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProduto;
import com.Back_End_Food_Truck.System_Food_Truck.Model.*;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryPedido;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryProduto;
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

    public Pedido criarPedido(DTOPedido dtoPedido) {
        Pedido pedido = new Pedido();
        pedido.setDataPedido(dtoPedido.getDataPedido());
        pedido.setFormaPagamento(FormaPagamento.valueOf(dtoPedido.getFormaPagamento()));
        pedido.setObservacao(dtoPedido.getObservacao());
        pedido.setPrecoTotal(dtoPedido.getPrecoTotal());

        // Endereço
        if (dtoPedido.getEndereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setRua(dtoPedido.getEndereco().getRua());
            endereco.setCidade(dtoPedido.getEndereco().getCidade());
            endereco.setCep(dtoPedido.getEndereco().getCep());
            endereco.setNumero(dtoPedido.getEndereco().getNumero());
            endereco.setBairro(dtoPedido.getEndereco().getBairro());
            endereco.setComplemento(dtoPedido.getEndereco().getComplemento());
            pedido.setEndereco(endereco);
        }

        // Itens
        if (dtoPedido.getItens() != null && !dtoPedido.getItens().isEmpty()) {
            List<PedidoItem> itens = dtoPedido.getItens().stream()
                    .map(itemDto -> {
                        Produto produto = repositoryProduto.findById(itemDto.getProdutoId())
                                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDto.getProdutoId()));

                        PedidoItem item = new PedidoItem();
                        item.setProduto(produto);
                        item.setQuantidade(itemDto.getQuantidade());
                        item.setPrecoUnitario(itemDto.getPrecoUnitario());
                        item.setPedido(pedido); // associa ao pedido
                        return item;
                    })
                    .collect(Collectors.toList());
            pedido.setItens(itens);
        }

        // salva tudo de uma vez
        return repositoryPedido.save(pedido);
    }


        // Resto dos métodos (listar, buscar, deletar) permanecem iguais



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
        DTOEndereco enderecoDTO = new DTOEndereco(
                pedido.getEndereco().getCep(),
                pedido.getEndereco().getBairro(),
                pedido.getEndereco().getCidade(),
                pedido.getEndereco().getComplemento(),
                pedido.getEndereco().getNumero(),
                pedido.getEndereco().getRua()
        );

        List<DTOPedidoItem> itensDTO = pedido.getItens().stream()
                .map(item -> new DTOPedidoItem(
                        item.getId(),
                        new DTOProduto(
                                item.getProduto().getAtivo(),
                                item.getProduto().getNome(),
                                item.getProduto().getDescricao(),
                                item.getProduto().getImagemUrl(),
                                item.getProduto().getPreco(),
                                item.getProduto().getCategoria().getDescricao(),
                                item.getProduto().getCategoria().getAtivo(),
                                item.getProduto().getCategoria().getNome()
                        ),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                ))
                .collect(Collectors.toList());

        return new DTOPedido(
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
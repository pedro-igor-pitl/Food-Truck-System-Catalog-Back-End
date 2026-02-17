package com.Back_End_Food_Truck.System_Food_Truck.Repository;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOCatalogo;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryProduto extends JpaRepository<Produto, Long> {

    @Query("""
        SELECT new com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOCatalogo(
            p.id,
            c.nome,
            p.nome,
            p.descricao,
            p.preco,
            p.imagemUrl
        )
        FROM Produto p
        JOIN p.categoria c
        WHERE p.ativo = true
        ORDER BY c.nome, p.nome
    """)
    List<DTOCatalogo> listarCatalogo();
}

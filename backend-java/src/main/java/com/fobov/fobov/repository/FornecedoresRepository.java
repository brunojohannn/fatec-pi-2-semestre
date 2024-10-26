package com.fobov.fobov.repository;

import com.fobov.fobov.model.Categoria;
import com.fobov.fobov.model.Fornecedores;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class FornecedoresRepository{
    private final DataSource DATA_SOURCE;

    public FornecedoresRepository(DataSource dataSource) {
        this.DATA_SOURCE = dataSource;
    }

    public List <Fornecedores> findAll(){
        List<Fornecedores> fornecedores = new ArrayList<>();
        String sql = "SELECT id_fornecedores, nome FROM fornecedores";

        try{
            Connection connection = DATA_SOURCE.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Fornecedores fornecedores = new Fornecedores();
                fornecedores.setId(resultSet.getInt("id_fornecedores"));
                fornecedores.setNome(resultSet.getString("nome"));
                fornecedores.add(fornecedores);

            }
            
        }
        catch(Exception e){
                e.printStackTrace();
            }

        return fornecedores;
    }

    public Fornecedores findById(int id_fornecedores){
        Fornecedores fornecedores = new Fornecedores();
        String sql = "SELECT id_fornecedores, nome FROM fornecedores WHERE id_fornecedores = ?";
        
        try{
            Connection connection = DATA_SOURCE.getConnection();
            PreparedStatement preparedStatement = connection.preparedStatement(sql);
            preparedStatement.setInt(1, id_fornecedores);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fornecedores.setId(resultSet.getInt("id_fornecedores"));
                fornecedores.setNome(resultSet.getNome("nome"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return fornecedores;
    }

    public boolean save (Fornecedores fornecedores){
        String sql = "INSERT INTO fornecedores (nome) VALUES (?)";

        try {
            Connection connection = DATA_SOURCE.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fornecedores.getNome());

            preparedStatement.executeUpdate();
            return true;
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(int id_fornecedores, Fornecedores fornecedores){
        String sql = "UPDATE fornecedores SET nome = ? WHERE id_fornecedores = ?";

        try{
            Connection connection = DATA_SOURCE.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setStrinf (1, fornecedores.getNome());
            preparedStatement.setInt(2, id_fornecedores);

            preparedStatement.executeUpdate();
            return true;
            
        }
        catch(Exception e){
            e.printStackTrace();;
        }

        return false;
    }

    public boolean delete(int id_fornecedores){
        String sql = "DELETE FROM fornecedores WHERE id_fornecedores = ?";

        try {
            Connection connection = DATA_SOURCE.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id_fornecedores);

            preparedStatement.executeUpdate();
            return true;

        }
        
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
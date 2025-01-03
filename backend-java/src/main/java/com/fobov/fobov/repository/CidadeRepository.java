package com.fobov.fobov.repository;

import com.fobov.fobov.interfaces.Crud;
import com.fobov.fobov.model.Cidade;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CidadeRepository implements Crud<Cidade, Integer> {
    private final DataSource DATA_SOURCE;

    public CidadeRepository(DataSource dataSource) {
        this.DATA_SOURCE = dataSource;
    }

    public List<Cidade> findAll() {
        List<Cidade> cidadeList = new ArrayList<>();
        String sql = "SELECT id_cidades, nome, id_estados FROM cidades";

        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Cidade cidade = new Cidade();
                cidade.setId(resultSet.getInt("id_cidades"));
                cidade.setNome(resultSet.getString("nome"));
                cidade.setIdEstado(resultSet.getInt("id_estados"));
                cidadeList.add(cidade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cidadeList;
    }

    public Cidade findById(Integer id) {
        Cidade cidade = null;
        String sql = "SELECT id_cidades, nome, id_estados FROM cidades WHERE id_cidades = ?";

        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                cidade = new Cidade();
                cidade.setId(resultSet.getInt("id_cidades"));
                cidade.setNome(resultSet.getString("nome"));
                cidade.setIdEstado(resultSet.getInt("id_estados"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cidade;
    }

    public boolean save(Cidade cidade) {
        String sql = "INSERT INTO cidades (nome) VALUES (?)";

        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cidade.getNome());

            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(Integer id, Cidade cidade) {
        String sql = "UPDATE cidades SET nome = ? WHERE id_cidades = ?";

        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cidade.getNome());
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(Integer id) {
        String sql = "DELETE FROM cidades WHERE id_cidades = ?";

        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
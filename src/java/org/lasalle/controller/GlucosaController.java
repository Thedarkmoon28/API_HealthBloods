package org.lasalle.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.lasalle.connection.ConeccionMysql;
import org.lasalle.model.Medida;

public class GlucosaController {

    public List<Medida> getMedida() throws SQLException {
        List<Medida> lista = new ArrayList<>();

        String sql = "SELECT id_lectura, id_usuario, valor_mg_dl, tipo_lectura, "
                + "notas, fecha_registro FROM lectura_glucosa";

        ConeccionMysql connMysql = new ConeccionMysql();

        try (Connection conn = connMysql.open();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Medida medida = new Medida();
                medida.setIdMedida(rs.getInt("id_lectura"));
                medida.setIdUsuario(rs.getInt("id_usuario"));
                medida.setGlucosa(rs.getInt("valor_mg_dl"));
                medida.setContexto(rs.getString("tipo_lectura"));
                medida.setNotas(rs.getString("notas"));
                medida.setFechaHora(rs.getDate("fecha_registro"));
                lista.add(medida);
            }
        }

        return lista;
    }

    public int insertarMedida(Medida medida) throws SQLException {
        String sql = "INSERT INTO lectura_glucosa "
                + "(id_usuario, valor_mg_dl, tipo_lectura, notas, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?)";

        ConeccionMysql connMysql = new ConeccionMysql();

        try (Connection conn = connMysql.open();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setInt(1, medida.getIdUsuario());
            pstm.setInt(2, medida.getGlucosa());
            pstm.setString(3, medida.getContexto());
            pstm.setString(4, medida.getNotas());
            pstm.setDate(5, medida.getFechaHora());

            if (pstm.executeUpdate() == 0) {
                throw new SQLException("No se pudo registrar la medida");
            }

            try (ResultSet keys = pstm.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException("No se pudo obtener el id de la medida registrada");
    }
}

package org.lasalle.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.lasalle.connection.ConeccionMysql;
import org.lasalle.model.Usuario;

public class UsuarioController {

    public List<Usuario> getUsuarios() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, primer_apellido, segundo_apellido, "
                + "fecha_nacimiento, tipo_diabetes, fecha_registro, email FROM users";

        ConeccionMysql connMysql = new ConeccionMysql();

        try (Connection conn = connMysql.open();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Usuario user = new Usuario();

                user.setIdUsuario(rs.getInt("id_usuario"));
                user.setNombre(rs.getString("nombre"));
                user.setPrimerApellido(rs.getString("primer_apellido"));
                user.setSegundoApellido(rs.getString("segundo_apellido"));
                user.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                user.setTipoDiabetes(rs.getString("tipo_diabetes"));
                user.setFechaRegistro(rs.getDate("fecha_registro"));
                user.setEmail(rs.getString("email"));

                lista.add(user);
            }
        }

        return lista;
    }

    public int insertarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO users "
                + "(nombre, primer_apellido, segundo_apellido, fecha_nacimiento, "
                + "tipo_diabetes, fecha_registro, email) "
                + "VALUES (?, ?, ?, ?, ?, CURDATE(), ?)";

        ConeccionMysql connMysql = new ConeccionMysql();

        try (Connection conn = connMysql.open();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, usuario.getNombre());
            pstm.setString(2, usuario.getPrimerApellido());
            pstm.setString(3, usuario.getSegundoApellido());
            pstm.setDate(4, usuario.getFechaNacimiento());
            pstm.setString(5, usuario.getTipoDiabetes());
            pstm.setString(6, usuario.getEmail());

            if (pstm.executeUpdate() == 0) {
                throw new SQLException("No se pudo registrar el usuario");
            }

            try (ResultSet keys = pstm.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException("No se pudo obtener el id del usuario registrado");
    }
}

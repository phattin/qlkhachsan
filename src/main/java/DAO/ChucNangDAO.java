package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Connection.DatabaseConnection;
import DTO.ChucNangDTO;

public class ChucNangDAO {
    public static ArrayList<ChucNangDTO> getAllChucNang() {
        ArrayList<ChucNangDTO> cnList = new ArrayList<>();
        String query = "SELECT * FROM chucnang";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cnList.add(new ChucNangDTO(
                        rs.getString("MaChucNang"),
                        rs.getString("TenChucNang")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnList;
    }

    public static ChucNangDTO getChucNangByMa(String maChucNang) {
        String query = "SELECT * FROM chucnang WHERE MaChucNang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maChucNang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ChucNangDTO(
                        rs.getString("MaChucNang"),
                        rs.getString("TenChucNang")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy
    }

    public String getMaByTenChucNang(String tenChucNang) {
        String query = "SELECT MaChucNang FROM chucnang WHERE TenChucNang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tenChucNang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("MaChucNang");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy
    }

    public ArrayList<ChucNangDTO> getChucNangByMaChucVu(String maChucVu) {
        ArrayList<ChucNangDTO> cnList = new ArrayList<>();
        String query = "SELECT * FROM chucnang JOIN phanquyen ON chucnang.MaChucNang = phanquyen.MaChucNang WHERE phanquyen.MaChucVu = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maChucVu);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cnList.add(new ChucNangDTO(
                        rs.getString("MaChucNang"),
                        rs.getString("TenChucNang")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maChucVu);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cnList.add(new ChucNangDTO(
                        rs.getString("MaChucNang"),
                        rs.getString("TenChucNang")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnList;
    }
}
